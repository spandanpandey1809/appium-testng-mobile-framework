package com.spandan.mobile.tests;

import com.spandan.mobile.pages.CatalogPage;
import com.spandan.mobile.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("My Demo App - Android")
@Feature("Login")
public class LoginTest extends BaseTest {

    // Public demo users shown on the app's own login screen
    private static final String VALID_USER = "bod@example.com";
    private static final String LOCKED_USER = "alice@example.com";
    private static final String PASSWORD = "10203040";

    @Test(groups = {"mobile", "smoke"})
    @Description("Valid user logs in, lands on the catalog, and the menu offers Log Out")
    public void validUserCanLogIn() {
        CatalogPage home = catalog().openMenu().openLogin().loginAs(VALID_USER, PASSWORD);

        assertTrue(home.isLoaded(), "Catalog should show after login");
        assertTrue(home.openMenu().showsLogout(), "Menu should offer Log Out once logged in");
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][]{
                // username, password, which error field, expected message
                {"", PASSWORD, "username", "Username is required"},
                {VALID_USER, "", "password", "Enter Password"},
                {LOCKED_USER, PASSWORD, "password", "Sorry this user has been locked out."}
        };
    }

    @Test(groups = {"mobile", "regression"}, dataProvider = "invalidLogins")
    @Description("Invalid logins show the right error under the right field")
    public void invalidLoginShowsError(String username, String password, String field, String expected) {
        LoginPage login = catalog().openMenu().openLogin().loginExpectingError(username, password);

        String actual = field.equals("username") ? login.usernameError() : login.passwordError();
        assertEquals(actual, expected);
    }
}
