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

    /** When login is triggered from checkout, the app continues to the shipping address screen. */
    @Step("Log in as {username} to continue checkout")
    public ShippingPage loginToCheckout(String username, String password) {
        submit(username, password);
        return new ShippingPage(driver);
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
        // The keyboard can cover the button on small screens
        hideKeyboard();
        tap(loginButton);
    }
}
