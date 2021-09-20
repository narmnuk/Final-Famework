package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    LoginPage loginPage;

    @BeforeMethod
    public void setUp() {

        loginPage = new LoginPage(getDriver());
    }

    @Test(testName = "Google Test", groups = {"regression"})
    public void googleTest() {

        getDriver().get("https://www.google.com");
        getExtentTest().info("Opening Google");
        screenshotPic();

        Assert.assertEquals(getDriver().getTitle(), "Google");
        loginPage.sleep(2000);
    }

    @Test(testName = "Amazon Test", groups = {"regression"})
    public void amazonTest() {

        getDriver().get("https://www.amazon.com");
        getExtentTest().info("Opening Amazon");
        screenshotPic();

        Assert.assertEquals(getDriver().getTitle(), "Amazon.com. Spend less. Smile more.");
        loginPage.sleep(2000);
    }

    @Test(testName = "Ebay Test", groups = {"regression"})
    public void ebayTest() {

        getDriver().get("https://www.ebay.com");
        getExtentTest().info("Opening Ebay");
        screenshotPic();

        Assert.assertEquals(getDriver().getTitle(), "Electronics, Cars, Fashion, Collectibles & More | eBay");
        loginPage.sleep(2000);
    }
}