package com.solidgate.utils;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Locale;

public class DriverSetup {

    public static void setupWebDriver() {
        Configuration.browser = "chrome";
        Configuration.headless = false; // Change to true for CI/CD
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=" + new Locale.Builder().setLanguage("uk").setRegion("UA").build().toLanguageTag());
        Configuration.browserCapabilities = options;

    }


}