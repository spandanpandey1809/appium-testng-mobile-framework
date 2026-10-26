package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;

/** Checkout step 2: card details. "Billing address same as shipping" is ticked by default. */
public class PaymentPage extends BasePage {

    public PaymentPage(AndroidDriver driver) {
        super(driver);
        waitForVisible(id("cardNumberET"));
    }

    @Step("Enter card details")
    public ReviewOrderPage enterCard(String holder, String number, String expiry, String securityCode) {
        scrollAndType("nameET", holder);
        scrollAndType("cardNumberET", number);
        scrollAndType("expirationDateET", expiry);
        scrollAndType("securityCodeET", securityCode);
        scrollAndTap("paymentBtn");
        return new ReviewOrderPage(driver);
    }
}
