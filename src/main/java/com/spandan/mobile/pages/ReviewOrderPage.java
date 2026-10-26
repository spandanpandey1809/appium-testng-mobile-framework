package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/** Checkout step 3: review the order, then place it. */
public class ReviewOrderPage extends BasePage {

    private final By orderList = id("placeOrderRV");
    private final By completeTitle = id("completeTV");

    public ReviewOrderPage(AndroidDriver driver) {
        super(driver);
        waitForVisible(orderList);
    }

    /** Places the order and returns the confirmation title, e.g. "Checkout Complete". */
    @Step("Place order")
    public String placeOrder() {
        scrollAndTap("paymentBtn");
        return textOf(completeTitle);
    }
}
