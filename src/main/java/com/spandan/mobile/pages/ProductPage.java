package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/** Product details: quantity selector and Add To Cart. The header (cart badge) stays visible. */
public class ProductPage extends BasePage {

    private final By increaseQuantity = id("plusIV");
    private final By quantity = id("noTV");
    private final By addToCartButton = id("cartBt");
    private final By cartBadge = id("cartTV");
    private final By cartButton = id("cartRL");

    public ProductPage(AndroidDriver driver) {
        super(driver);
        waitForVisible(addToCartButton);
    }

    @Step("Increase quantity")
    public ProductPage increaseQuantity() {
        tap(increaseQuantity);
        return this;
    }

    public int quantity() {
        return Integer.parseInt(textOf(quantity));
    }

    @Step("Add to cart")
    public ProductPage addToCart() {
        tap(addToCartButton);
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
