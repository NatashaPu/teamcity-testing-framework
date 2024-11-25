package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateBuildConfigurationPage;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;

@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUiTest{
    private static final String REPO_URL_BUILD = "https://github.com/NatashaPu/workshop";

    @Test(description = "User should be able to create a build type", groups = {"Positive"})
    public void userCreatesBuildType () {

        //подготовка окружения
        loginAs(testData.getUser());
        var userCheckRequest = new CheckRequests(Specifications.authSpec(testData.getUser()));
        var projectId = userCheckRequest.<Project>getRequest(PROJECTS).create(testData.getProject());

        //взаимодействие с UI
        CreateBuildConfigurationPage .open(projectId.getId())
                .createForm(REPO_URL_BUILD)
                .setupBuildType(testData.getBuildType().getName());

        //проверка состояния API
        //корректность отправки данных с UI на API
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("name:" + testData.getBuildType().getName());
        softy.assertNotNull(createdBuildType);

        //проверка состояния UI
        //корректность считывания данных и отображения данных на UI
        BuildTypePage.open(createdBuildType.getId())
                .title.shouldHave(Condition.exactText(testData.getBuildType().getName()));
    }

    @Test(description = "User shouldn't be able to create a build type without name", groups = {"Negstive"})
    public void userCreateBuildTypeWithEmptyName() {
        //подготовка окружения
        loginAs(testData.getUser());
        var userCheckRequest = new CheckRequests(Specifications.authSpec(testData.getUser()));
        var projectId = userCheckRequest.<Project>getRequest(PROJECTS).create(testData.getProject());

        //взаимодействие с UI и проверка состояния UI
        CreateBuildConfigurationPage.open(projectId.getId())
                .createForm(REPO_URL_BUILD)
                .errorMessageEmptyBuildTypeName("");
    }
}
