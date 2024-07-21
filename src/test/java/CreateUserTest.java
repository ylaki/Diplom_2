import client.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import models.UserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static client.UserGenerator.*;

public class CreateUserTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    StepsTest stepsTest = new StepsTest();
    private UserData userData;
    private User user;
    private int CurrentStatusCode;

    @Step("Set BaseURI")
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = new User();
    }

    @Test
    @DisplayName("Create new user")
    @Description("Create new unique user")
    public void createUser() {
        UserData userData = randomUser();
        Response response = user.create(userData);
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        CurrentStatusCode = response.statusCode();

        stepsTest.compareStatusCode200(CurrentStatusCode);
        stepsTest.comparePayloadSuccess(bodyAsString);

        String bearerToken = body.path("accessToken");
        user.delete(bearerToken);
    }


    @Test
    @DisplayName("Create existing user")
    @Description("Create user with existing login")
    public void createUserWithExistingLogin() {

        UserData userData = randomUser();
        Response originalresponse = user.create(userData);
        ResponseBody originalbody = originalresponse.getBody();

        user.create(userData);
        Response response = user.create(userData);
        CurrentStatusCode = response.statusCode();
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();

        stepsTest.compareStatusCode403(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);
        stepsTest.comparePayloadAlreadyExists(bodyAsString);

        String bearerToken = originalbody.path("accessToken");
        user.delete(bearerToken);
    }

    @Test
    @DisplayName("Create new user")
    @Description("Create user without email")
    public void createUserWithoutEmail() {
        UserData userData = noEmailUser();

        Response response = user.create(userData);
        CurrentStatusCode = response.statusCode();
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        stepsTest.compareStatusCode403(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);
        stepsTest.comparePayloadMissingField(bodyAsString);
    }

    @Test
    @DisplayName("Create new user")
    @Description("Create user without password")
    public void createUserWithoutPassword() {
        UserData userData = noPasswordUser();

        Response response = user.create(userData);
        CurrentStatusCode = response.statusCode();
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        stepsTest.compareStatusCode403(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);
        stepsTest.comparePayloadMissingField(bodyAsString);
    }

    @Test
    @DisplayName("Create new user")
    @Description("Create user without name")
    public void loginExistingUser() {
        UserData userData = noNameUser();

        Response response = user.create(userData);
        CurrentStatusCode = response.statusCode();
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        stepsTest.compareStatusCode403(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);
        stepsTest.comparePayloadMissingField(bodyAsString);
    }

    @Step("Delete user")
    @After
    public void tearDown() {
    }
}
