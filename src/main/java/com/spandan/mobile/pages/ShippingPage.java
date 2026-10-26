package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;

/** Checkout step 1: shipping address. A long form, so every field is scrolled into view first. */
public class ShippingPage extends BasePage {

    public ShippingPage(AndroidDriver driver) {
        super(driver);
        waitForVisible(id("fullNameET"));
    }

    @Step("Enter shipping address for {fullName}")
    public PaymentPage enterAddress(String fullName, String address, String city, String zip, String country) {
        scrollAndType("fullNameET", fullName);
        scrollAndType("address1ET", address);
        scrollAndType("cityET", city);
        scrollAndType("zipET", zip);
        scrollAndType("countryET", country);
        scrollAndTap("paymentBtn");
        return new PaymentPage(driver);
    }
}
