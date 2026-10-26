package com.spandan.mobile.pages;

import com.spandan.mobile.config.ConfigManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/** Side drawer menu. Items are found by accessibility id, scrolling the drawer if needed. */
public class MenuPage extends BasePage {

    private static final String LOGIN_ITEM = "Login Menu Item";
    private static final String LOGOUT_ITEM = "Logout Menu Item";

    private final By menuList = id("menuRV");

    public MenuPage(AndroidDriver driver) {
        super(driver);
        waitForVisible(menuList);
    }

    @Step("Open Log In from the menu")
    public LoginPage openLogin() {
        tap(scrollToItem(LOGIN_ITEM));
        return new LoginPage(driver);
    }

    public boolean showsLogout() {
        return isPresent(scrollToItem(LOGOUT_ITEM));
    }

    /**
     * Small screens don't show the whole menu, so let UiAutomator scroll the drawer list
     * until the item is visible instead of hard-coding swipe coordinates.
     */
    private By scrollToItem(String description) {
        String listId = ConfigManager.get("app.package") + ":id/menuRV";
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().resourceId(\"" + listId + "\"))"
                        + ".scrollIntoView(new UiSelector().description(\"" + description + "\"))");
    }
}
