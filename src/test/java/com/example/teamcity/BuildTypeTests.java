package com.example.teamcity;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.api.generators.TestDataGenerator.generator;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTests extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequest = new CheckRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequest.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckRequest.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequest.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "BuildType name is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequest = new CheckRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequest.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckRequest.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));

    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject());
        testData.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));
        superUserCheckRequests.<User>getRequest(USERS).create(testData.getUser());
        var userCheckRequest = new CheckRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequest.getRequest(BUILD_TYPES).create(testData.getBuildType());
        var createdBuildType = userCheckRequest.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "BuildType name is not correct");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject());
        var project2 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(generate(Project.class));
        testData.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));
        superUserCheckRequests.<User>getRequest(USERS).create(testData.getUser());
        var user2 = generator().getUser();
        user2.setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));
        new UncheckedBase(Specifications.authSpec(user2), BUILD_TYPES)
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Incorrect username or password.\n" +
                        "To login manually go to \"/login.html\" page"));
    }
}
