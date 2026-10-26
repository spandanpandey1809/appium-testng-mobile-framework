package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CartPage extends BasePage {

    private final By itemCount = id("itemsTV");
    private final By totalPrice = id("totalPriceTV");
    private final By checkoutButton = id("cartBt");
    private final By removeButton = id("removeBt");
    private final By emptyCartTitle = id("noItemTitleTV");

    public CartPage(AndroidDriver driver) {
        super(driver);
    }

    /** "2 Items" -> 2 */
    public int itemCount() {
        return Integer.parseInt(textOf(itemCount).replaceAll("\\D", ""));
    }

    public double totalPrice() {
        return Double.parseDouble(textOf(totalPrice).replaceAll("[^0-9.]", ""));
    }

    @Step("Remove the first item")
    public CartPage removeFirstItem() {
        tap(removeButton);
        return this;
    }

    public String emptyCartMessage() {
        return textOf(emptyCartTitle);
    }

    /** Logged-out users are sent to Log In first. */
    @Step("Proceed to checkout")
    public LoginPage proceedToCheckout() {
        tap(checkoutButton);
        return new LoginPage(driver);
    }
}
