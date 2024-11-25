package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class BuildTypePage extends BasePage{

    private static final String BUILD_TYP_URL = "/buildConfiguration/%s";
    public SelenideElement title = $("body > div:nth-child(6) > div:nth-child(7) > div:nth-child(1) > div:nth-child(3) > main:nth-child(2) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > h1:nth-child(2) > span:nth-child(2)");
    public static BuildTypePage open(String buildTypeId) {
        return Selenide.open(BUILD_TYP_URL.formatted(buildTypeId), BuildTypePage.class);
    }
}
