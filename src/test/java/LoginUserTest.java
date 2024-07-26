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

public class LoginUserTest {

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
    @DisplayName("Login")
    @Description("Login as existing user")
    public void loginUser() {
        UserData userData = randomUser();
        Response response = user.create(userData);

        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();
        String bodyAsString = body.asString();
        CurrentStatusCode = response.statusCode();

        stepsTest.compareStatusCode200(CurrentStatusCode);
        stepsTest.comparePayloadSuccess(bodyAsString);

        String bearerToken = body.path("accessToken");
        user.delete(bearerToken);
    }

    @Test
    @DisplayName("Invalid login")
    @Description("Login with invalid credentials")
    public void loginUserWithInvalidCreds() {
        UserData loginCredentials = new UserData()
                .withEmail("08ощль")
                .withPassword("3564л9087нрштоь");
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();
        String bodyAsString = body.asString();
        CurrentStatusCode = loginResponse.statusCode();

        stepsTest.compareStatusCode401(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);
    }
}
