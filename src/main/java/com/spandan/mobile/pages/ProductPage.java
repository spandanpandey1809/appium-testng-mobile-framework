package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Product details. The screen is a ScrollView with a large image on top, so the quantity row and
 * Add To Cart button start below the fold and must be scrolled into view. The header (cart badge) stays fixed.
 */
public class ProductPage extends BasePage {

    private final By cartBadge = id("cartTV");
    private final By cartButton = id("cartRL");

    public ProductPage(AndroidDriver driver) {
        super(driver);
        waitAndScrollTo("cartBt");
    }

    @Step("Increase quantity")
    public ProductPage increaseQuantity() {
        scrollAndTap("plusIV");
        return this;
    }

    public int quantity() {
        return Integer.parseInt(waitAndScrollTo("noTV").getText());
    }

    @Step("Add to cart")
    public ProductPage addToCart() {
        scrollAndTap("cartBt");
        waitForVisible(cartBadge);
        return this;
    }

    public int cartCount() {
        return isPresent(cartBadge) ? Integer.parseInt(textOf(cartBadge)) : 0;
    }

    @Step("Open cart")
    public CartPage openCart() {
        tap(cartButton);
        return new CartPage(driver);
    }
}
