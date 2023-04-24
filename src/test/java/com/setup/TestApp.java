package com.setup;

import com.browserstack.local.Local;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.net.URL;
import java.util.HashMap;

public class TestApp {
    public static AndroidDriver<MobileElement> driver;
    Local bsLocal;
    static String userName = System.getenv("BROWSERSTACK_USERNAME");
    static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    static String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
    static String buildName = System.getenv("BROWSERSTACK_BUILD_NAME");
    static String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
    static String app = System.getenv("BROWSERSTACK_APP_ID");
    private static final String BROWSERSTACK_HUB_URL = "hub-cloud.browserstack.com";

    @Test
    public void test_setup() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("app", app);
        caps.setCapability("device", "Samsung Galaxy S8");
        caps.setCapability("build", buildName);
        caps.setCapability("browserstack.local", browserstackLocal);
        caps.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
        driver = new AndroidDriver<MobileElement>(new URL("https://" + userName + ":" + accessKey + "@" + BROWSERSTACK_HUB_URL + "/wd/hub"),caps);
        Thread.sleep(3000);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws Exception {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        if (result.getStatus() == ITestResult.SUCCESS) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Test Passed\"}}");
        }
        else
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Test Failed\"}}");
        Thread.sleep(3000);
        driver.quit();
    }
}
