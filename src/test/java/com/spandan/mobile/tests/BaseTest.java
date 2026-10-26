package com.spandan.mobile.tests;

import com.spandan.mobile.driver.DriverFactory;
import com.spandan.mobile.pages.CatalogPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * One Appium session per test class (starting a session on a phone takes several seconds);
 * the app is killed and relaunched before each test so tests never depend on each other.
 */
public abstract class BaseTest {

    @BeforeClass(alwaysRun = true)
    public void startSession() {
        DriverFactory.initDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void freshApp() {
        DriverFactory.restartApp();
    }

    /** On failure, attach a screenshot and the screen's UI hierarchy (page source) for debugging. */
    @AfterMethod(alwaysRun = true)
    public void captureEvidence(ITestResult result) {
        if (result.getStatus() != ITestResult.FAILURE || !DriverFactory.hasDriver()) {
            return;
        }
        try {
            byte[] png = driver().getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot - " + result.getName(), "image/png", new ByteArrayInputStream(png), "png");
            Allure.addAttachment("Page source - " + result.getName(), "text/xml",
                    new ByteArrayInputStream(driver().getPageSource().getBytes(StandardCharsets.UTF_8)), "xml");
        } catch (RuntimeException e) {
            // Evidence capture must never hide the real failure
            System.err.println("Could not capture failure evidence: " + e.getMessage());
        }
    }

    @AfterClass(alwaysRun = true)
    public void endSession() {
        DriverFactory.quitDriver();
    }

    protected AndroidDriver driver() {
        return DriverFactory.getDriver();
    }

    protected CatalogPage catalog() {
        return new CatalogPage(driver()).waitUntilLoaded();
    }
}
