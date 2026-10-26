package com.spandan.mobile.pages;

import com.spandan.mobile.config.ConfigManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Home screen: the product catalog, with the shared header (menu, sort, cart). */
public class CatalogPage extends BasePage {

    private final By title = id("productTV");
    private final By productList = id("productRV");
    private final By productImages = id("productIV");
    private final By productPrices = id("priceTV");
    private final By menuButton = id("menuIV");
    private final By sortButton = id("sortIV");
    private final By sortPriceAscending = id("priceAscCL");
    private final By cartButton = id("cartRL");
    private final By cartBadge = id("cartTV");

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

    @Step("Open the first product")
    public ProductPage openFirstProduct() {
        waitForVisible(productImages);
        findAll(productImages).get(0).click();
        return new ProductPage(driver);
    }

    @Step("Sort products by price, low to high")
    public CatalogPage sortByPriceAscending() {
        tap(sortButton);
        tap(sortPriceAscending);
        return waitUntilLoaded();
    }

    /** Prices of the products currently on screen, e.g. "$ 29.99" -> 29.99 */
    public List<Double> visiblePrices() {
        waitForVisible(productPrices);
        return findAll(productPrices).stream()
                .map(WebElement::getText)
                .map(text -> Double.parseDouble(text.replaceAll("[^0-9.]", "")))
                .toList();
    }

    /**
     * Scrolls the product grid until a product whose title contains the text is on screen.
     * Uses UiScrollable (a real swipe gesture on the list) rather than fixed coordinates.
     */
    @Step("Scroll the catalog to '{titlePart}'")
    public boolean scrollToProduct(String titlePart) {
        String listId = ConfigManager.get("app.package") + ":id/productRV";
        return !findAll(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().resourceId(\"" + listId + "\"))"
                        + ".scrollIntoView(new UiSelector().textContains(\"" + titlePart + "\"))")).isEmpty();
    }

    /** Number on the cart badge; the badge is hidden when the cart is empty. */
    public int cartCount() {
        return isPresent(cartBadge) ? Integer.parseInt(textOf(cartBadge)) : 0;
    }

    @Step("Open cart")
    public CartPage openCart() {
        tap(cartButton);
        return new CartPage(driver);
    }
}
