package com.spandan.mobile.tests;

import com.spandan.mobile.pages.CartPage;
import com.spandan.mobile.pages.CatalogPage;
import com.spandan.mobile.pages.ProductPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("My Demo App - Android")
@Feature("Catalog and cart")
public class CatalogAndCartTest extends BaseTest {

    @Test(groups = {"mobile", "smoke"})
    @Description("Adding a product shows 1 on the cart badge and 1 item in the cart")
    public void addProductToCart() {
        ProductPage product = catalog().openFirstProduct().addToCart();
        assertEquals(product.cartCount(), 1, "Cart badge");

        CartPage cart = product.openCart();
        assertEquals(cart.itemCount(), 1, "Items in cart");
        assertTrue(cart.totalPrice() > 0, "Cart total should be positive");
    }

    @Test(groups = {"mobile", "regression"})
    @Description("Increasing quantity before adding puts that many units in the cart")
    public void addMultipleUnits() {
        ProductPage product = catalog().openFirstProduct().increaseQuantity();
        assertEquals(product.quantity(), 2, "Quantity selector");

        product.addToCart();
        assertEquals(product.cartCount(), 2, "Cart badge");
        assertEquals(product.openCart().itemCount(), 2, "Items in cart");
    }

    @Test(groups = {"mobile", "regression"})
    @Description("Removing the only item leaves an empty cart")
    public void removeItemEmptiesCart() {
        CartPage cart = catalog().openFirstProduct().addToCart().openCart().removeFirstItem();
        assertEquals(cart.emptyCartMessage(), "No Items");
    }

    @Test(groups = {"mobile", "regression"})
    @Description("Sorting by price low to high orders the visible products by price")
    public void sortByPriceAscending() {
        List<Double> prices = catalog().sortByPriceAscending().visiblePrices();

        List<Double> expected = new ArrayList<>(prices);
        expected.sort(Comparator.naturalOrder());
        assertTrue(prices.size() > 1, "Expected several products on screen");
        assertEquals(prices, expected, "Prices should be in ascending order");
    }

    @Test(groups = {"mobile", "regression"})
    @Description("Scrolling (swipe gesture) reaches a product at the bottom of the catalog")
    public void scrollToLastProduct() {
        CatalogPage catalog = catalog();
        assertTrue(catalog.scrollToProduct("Test.allTheThings() T-Shirt"),
                "Product at the bottom of the catalog should be reachable by scrolling");
    }
}
