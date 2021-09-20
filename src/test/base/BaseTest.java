package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.Screenshot;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private static final ThreadLocal<WebDriver> allDrivers = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> allExtentTests = new ThreadLocal<>();
    private ExtentReports extentReports;
    private final String configFilePath = "src/test/data/config/config.properties";

    @BeforeMethod()
    public void setUpDriver(Method method) {

        initializeDriver(ConfigReader.readProperty(configFilePath, "browser"));

        ExtentTest extentTest = extentReports.createTest(getTestName(method));
        allExtentTests.set(extentTest);
        getTestGroup(method);

//        getDriver().get(ConfigReader.readProperty(configFilePath, "url"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (result.getStatus() == ITestResult.SUCCESS) {
            getExtentTest().pass("PASSED");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            getExtentTest().fail("FAILED");
            getExtentTest().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            getExtentTest().fail("SKIPPED");
            getExtentTest().fail(result.getThrowable());
        }

        getDriver().quit();
    }

    @BeforeSuite
    public void startReporter() {

        extentReports = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports.html");

        sparkReporter.config().setDocumentTitle("Orange HRM");
        sparkReporter.config().setReportName("OrangeHRM Tests");
        extentReports.attachReporter(sparkReporter);
    }

    @AfterSuite
    public void closeReporter() {

        extentReports.flush();
    }

    public String getTestName(Method method) {

        Test testClass = method.getAnnotation(Test.class);
        if (testClass.testName().length() > 0)
            return testClass.testName();
        return method.getName();
    }

    public void getTestGroup(Method method) {

        Test testClass = method.getAnnotation(Test.class);
        for (String e : testClass.groups()) {
            getExtentTest().assignCategory(e);
        }
    }

    public void logScreenshotPic(String title) {

        getExtentTest().info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.takeScreenshot(getDriver())).build());
    }

    public void screenshotPic() {

        getExtentTest().info(MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.takeScreenshot(getDriver())).build());
    }

    public void initializeDriver(String browser) {

        WebDriver driver = null;
        switch (browser) {
            case "Chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "Firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
        }

        allDrivers.set(driver);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {

        return allDrivers.get();
    }

    public ExtentTest getExtentTest() {

        return allExtentTests.get();
    }
}