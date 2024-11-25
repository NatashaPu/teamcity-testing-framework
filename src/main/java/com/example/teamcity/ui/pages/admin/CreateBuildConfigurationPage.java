package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildConfigurationPage extends CreateBasePage{
    private static final String BUILD_CONFIGURATION_SHOE_MODE = "createBuildTypeMenu";
    private SelenideElement buildTypeNameInput = $("#buildTypeName");
    private SelenideElement defaultBranchInput = $("#branch");
    private SelenideElement errorEmptyBuildTypeName =  $("#error_buildTypeName");

    public static CreateBuildConfigurationPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_CONFIGURATION_SHOE_MODE),CreateBuildConfigurationPage.class);
    }

    public CreateBuildConfigurationPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public void setupBuildType(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
    }

    public void errorMessageEmptyBuildTypeName(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        errorEmptyBuildTypeName.should(Condition.visible);
    }


}
