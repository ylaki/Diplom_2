import client.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import models.UserData;
import org.junit.Before;
import org.junit.Test;
import static client.UserGenerator.randomUser;
public class UpdateUserData {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    StepsTest stepsTest = new StepsTest();
    private User user;
    private int CurrentStatusCode;

    @Step("Set BaseURI")
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = new User();
    }

    @Test
    @DisplayName("Update user data")
    @Description("Update user with authorisation")
    public void updateUser() {
        UserData userData = randomUser();
        user.create(userData);
        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();
        UserData loginCredentialsNew = new UserData()
                .withName("TestTestUpdate123")
                .withEmail("test@stasafe123.net")
                .withPassword("TestTestUpdate123123");
        String bearerToken = body.path("accessToken");
        Response responseUpdate = user.update(bearerToken, loginCredentialsNew);
        ResponseBody bodyUpdate = responseUpdate.getBody();
        String bodyUpdateAsString = bodyUpdate.asString();
        CurrentStatusCode = responseUpdate.statusCode();
        stepsTest.compareStatusCode200(CurrentStatusCode);
        stepsTest.comparePayloadSuccess(bodyUpdateAsString);
        user.delete(bearerToken);
    }

    @Test
    @DisplayName("Update user data")
    @Description("Update user without authorisation")
    public void updateUserNoAuth() {
        UserData userData = randomUser();
        user.create(userData);
        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();
        UserData loginCredentialsNew = new UserData()
                .withName("TestTestUpdate456")
                .withEmail("test@stasafe456.net")
                .withPassword("TestTestUpdate123456");
        String bearerToken = body.path("accessToken");
        Response responseUpdate = user.update(null, loginCredentialsNew);
        ResponseBody bodyUpdate = responseUpdate.getBody();
        String bodyUpdateAsString = bodyUpdate.asString();
        CurrentStatusCode = responseUpdate.statusCode();
        stepsTest.compareStatusCode401(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyUpdateAsString);
        user.delete(bearerToken);
    }
}
