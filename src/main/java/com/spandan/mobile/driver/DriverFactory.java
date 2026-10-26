package com.spandan.mobile.driver;

import com.spandan.mobile.config.ConfigManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Starts one Appium session (AndroidDriver over UiAutomator2) per thread.
 * Sessions are expensive on a phone, so tests reuse one per class and restart the app between tests.
 */
public final class DriverFactory {

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        Path app = Paths.get(ConfigManager.get("app.path")).toAbsolutePath();
        if (!Files.exists(app)) {
            throw new IllegalStateException("App not found at " + app + ". See README: download the APK into apps/");
        }

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(ConfigManager.get("device.name"))
                .setApp(app.toString())
                // The app opens on a splash screen before the main activity; accept any of its activities.
                .setAppWaitActivity("*")
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(ConfigManager.getInt("new.command.timeout.seconds")));

        try {
            DRIVER.set(new AndroidDriver(new URL(ConfigManager.get("appium.url")), options));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid appium.url", e);
        }
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No Appium session for this thread. Call initDriver() first.");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    /** Kills and relaunches the app so every test starts from a clean state (logged out, empty cart). */
    public static void restartApp() {
        String appPackage = ConfigManager.get("app.package");
        AndroidDriver driver = getDriver();
        driver.terminateApp(appPackage);
        driver.activateApp(appPackage);
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
