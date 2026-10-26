package com.spandan.mobile.pages;

import com.spandan.mobile.config.ConfigManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Shared actions with explicit waits. Locators prefer resource-id and accessibility id over XPath. */
public abstract class BasePage {

    private static final String PACKAGE = ConfigManager.get("app.package");

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getInt("explicit.wait.seconds")));
    }

    /** Builds a full resource-id locator, e.g. id("loginBtn") -> com.saucelabs.mydemoapp.android:id/loginBtn */
    protected static By id(String resourceId) {
        return AppiumBy.id(PACKAGE + ":id/" + resourceId);
    }

    protected static By accessibilityId(String description) {
        return AppiumBy.accessibilityId(description);
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement field = waitForVisible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String textOf(By locator) {
        return waitForVisible(locator).getText();
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Scrolls the screen's scrollable container until the element with this resource-id is on screen.
     * Android only exposes on-screen views, so long forms need this before typing or tapping.
     */
    protected WebElement scrollToId(String resourceId) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                        + ".scrollIntoView(new UiSelector().resourceId(\"" + PACKAGE + ":id/" + resourceId + "\"))"));
    }

    /**
     * Waits for a screen to load, then scrolls to the element. Use for elements that start below the fold,
     * where a plain visibility wait would time out because the element is not on screen yet.
     */
    protected WebElement waitAndScrollTo(String resourceId) {
        return wait.ignoring(NoSuchElementException.class).until(d -> scrollToId(resourceId));
    }

    /** Scrolls to a field, then types into it. */
    protected void scrollAndType(String resourceId, String text) {
        WebElement field = scrollToId(resourceId);
        field.clear();
        field.sendKeys(text);
    }

    /** Scrolls to a button, closes the keyboard if it covers it, then taps it. */
    protected void scrollAndTap(String resourceId) {
        hideKeyboard();
        waitAndScrollTo(resourceId).click();
    }

    protected void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (RuntimeException ignored) {
            // keyboard was not shown
        }
    }
}
