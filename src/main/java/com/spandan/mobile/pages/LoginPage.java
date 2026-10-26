package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final By usernameField = id("nameET");
    private final By passwordField = id("passwordET");
    private final By loginButton = id("loginBtn");
    private final By usernameError = id("nameErrorTV");
    private final By passwordError = id("passwordErrorTV");

    public LoginPage(AndroidDriver driver) {
        super(driver);
        waitForVisible(loginButton);
    }

    @Step("Log in as {username}")
    public CatalogPage loginAs(String username, String password) {
        submit(username, password);
        return new CatalogPage(driver).waitUntilLoaded();
    }

    @Step("Submit credentials expecting an error")
    public LoginPage loginExpectingError(String username, String password) {
        submit(username, password);
        return this;
    }

    public String usernameError() {
        return textOf(usernameError);
    }

    public String passwordError() {
        return textOf(passwordError);
    }

    private void submit(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        // The keyboard can cover the button on small screens; hiding it fails harmlessly if it isn't open
        try {
            driver.hideKeyboard();
        } catch (RuntimeException ignored) {
            // keyboard was not shown
        }
        tap(loginButton);
    }
}
