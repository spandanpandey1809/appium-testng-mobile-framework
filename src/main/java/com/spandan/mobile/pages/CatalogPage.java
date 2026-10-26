package com.spandan.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Home screen: the product catalog, with the shared header (menu, cart). */
public class CatalogPage extends BasePage {

    private final By title = id("productTV");
    private final By productList = id("productRV");
    private final By menuButton = id("menuIV");

    public CatalogPage(AndroidDriver driver) {
        super(driver);
    }

    /** Waits past the splash screen until the catalog is shown. */
    public CatalogPage waitUntilLoaded() {
        wait.until(ExpectedConditions.textToBe(title, "Products"));
        waitForVisible(productList);
        return this;
    }

    public boolean isLoaded() {
        return "Products".equals(textOf(title));
    }

    @Step("Open side menu")
    public MenuPage openMenu() {
        tap(menuButton);
        return new MenuPage(driver);
    }
}
