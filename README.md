# Appium + TestNG Mobile Automation Framework

![Android Tests](https://github.com/spandanpandey1809/appium-testng-mobile-framework/actions/workflows/android.yml/badge.svg)

Android app test automation in Java with **Appium 2 (UiAutomator2)**, TestNG and Allure. Every push runs the suite on an Android emulator in GitHub Actions.

Companion to my web/API framework: [restassured-selenium-testng-framework](https://github.com/spandanpandey1809/restassured-selenium-testng-framework).

| Layer | Tools |
| --- | --- |
| Mobile automation | Appium 2, UiAutomator2 driver, Appium Java client 9 |
| Test runner | TestNG: groups, data providers |
| Design | Page Object Model, explicit waits, resource-id and accessibility-id locators |
| Reporting | Allure with screenshot and UI hierarchy (page source) on failure |
| CI/CD | GitHub Actions with a hardware-accelerated Android emulator (API 31) |
| Build | Maven, Java 17 |

## App under test

[My Demo App for Android](https://github.com/saucelabs/my-demo-app-android) by Sauce Labs, a public practice shopping app (catalog, product details, cart, checkout, login). CI downloads the latest release APK on every run.

## Test coverage

| Area | Scenarios |
| --- | --- |
| Login | Valid login (menu then offers Log Out); empty username, empty password, locked-out user (data-driven) |
| Catalog | Sort by price (low to high), scroll gesture to the last product |
| Cart | Add a product (badge and cart count), add several units, remove the last item (empty cart) |
| Checkout | Logged-out user: cart, log in at checkout, shipping address, card details, place order, confirmation |

## How to run locally

You need Java 17+, Maven, Node.js, Android Studio (for the emulator and `adb`) and Appium 2:

```bash
npm install -g appium@2
appium driver install uiautomator2
appium                                   # leave running, listens on port 4723
```

1. Start an Android emulator from Android Studio (Device Manager) and check it appears in `adb devices`.
2. Download the latest `mda-x.y.z.apk` from the [app releases](https://github.com/saucelabs/my-demo-app-android/releases) and save it as `apps/mda.apk`.
3. Run:

```bash
mvn test                                 # full suite
mvn test -Dsuite=smoke.xml               # smoke tests only
mvn test -Ddevice.name=<adb device id>   # pick a device
mvn allure:serve                         # open the report
```

## Design decisions

- **One Appium session per test class, fresh app per test:** starting a session on a phone is slow, so the session is reused and the app is killed and relaunched before every test. Tests stay independent without paying the session cost each time.
- **Stable locators:** resource-id and accessibility id first; no XPath.
- **Scrolling without coordinates:** menu items, catalog products and long checkout forms are reached with `UiScrollable`, so tests work on any screen size. Android only exposes on-screen views, so every checkout field is scrolled into view before typing.
- **Fluent checkout flow:** each page action returns the next page, so the whole checkout reads as one chain in the test.
- **Failure evidence:** screenshot plus the screen's UI hierarchy attached to Allure, so a failure can be debugged without re-running it.
