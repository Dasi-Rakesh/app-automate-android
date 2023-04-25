package com.setup;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;

public class TestGeoLocation {
    public static WebDriver driver;
    static String userName = System.getenv("BROWSERSTACK_USERNAME");
    static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    static String buildName = System.getenv("BROWSERSTACK_BUILD_NAME");
    private static final String BROWSERSTACK_HUB_URL = "hub-cloud.browserstack.com";

    @BeforeMethod
    public void setup() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("build", buildName);
        caps.setCapability("browserstack.geoLocation", "FR");
        caps.setCapability("os", "Windows");
        caps.setCapability("os_version", "11");
        caps.setCapability("browser", "Chrome");
        caps.setCapability("browser_version", "110");
        caps.setCapability("name", "IP Location Test");
        driver = new AndroidDriver<MobileElement>(new URL("https://" + userName + ":" + accessKey + "@" + BROWSERSTACK_HUB_URL + "/wd/hub"), caps);
        Thread.sleep(3000);
    }

    @Test
    public void test_Location() {
        driver.get("http://whatismyip.akamai.com/advanced");
        String text = driver.findElement(By.xpath("//html/body")).getText();
        System.out.println("Location: " + text);
        Assert.assertTrue(text.contains("Location: PARIS, IDF, FR"), "Incorrect Location");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws Exception {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        if (result.getStatus() == ITestResult.SUCCESS) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Test Passed\"}}");
        } else
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Test Failed\"}}");
        Thread.sleep(3000);
        driver.quit();
    }
}
