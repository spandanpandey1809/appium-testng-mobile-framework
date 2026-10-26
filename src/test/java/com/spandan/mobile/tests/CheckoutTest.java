package com.spandan.mobile.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("My Demo App - Android")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    @Test(groups = {"mobile", "smoke"})
    @Description("Logged-out user adds a product, logs in at checkout, enters address and card, and places the order")
    public void completeCheckoutAsLoggedOutUser() {
        String confirmation = catalog()
                .openFirstProduct()
                .addToCart()
                .openCart()
                .proceedToCheckout()
                .loginToCheckout("bod@example.com", "10203040")
                .enterAddress("Spandan Pandey", "12 MG Road", "Lucknow", "226001", "India")
                // Public test card number; the demo app does not charge anything
                .enterCard("Spandan Pandey", "4111111111111111", "12/30", "123")
                .placeOrder();

        assertEquals(confirmation, "Checkout Complete");
    }
}
