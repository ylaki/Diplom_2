import client.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import models.UserData;
import orders.Order;
import org.junit.Before;
import org.junit.Test;
import static client.UserGenerator.randomUser;

public class CreateOrderTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private Order order;
    StepsTest stepsTest = new StepsTest();
    private User user;
    private int CurrentStatusCode;
    private String payload = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa72\"]}";
    private String noIngredientsPayload = "{\"ingredients\": []}";
    private String invalidIngredientsPayload = "{\"ingredients\": [\"invalid id1\",\"invalid id2\"]}";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = new User();
        order = new Order();
    }

    @Test
    @DisplayName("Create order")
    @Description("Create order with authorization")
    public void CreateOrderWithAuthAndIngredientsTest() {
        UserData userData = randomUser();
        user.create(userData);

        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();

        String bearerToken = body.path("accessToken");

        Response orderBody = order.createOrder(payload,bearerToken);
        String bodyAsString = orderBody.asString();
        CurrentStatusCode = orderBody.statusCode();
        stepsTest.compareStatusCode200(CurrentStatusCode);
        stepsTest.comparePayloadSuccess(bodyAsString);

        user.delete(bearerToken);
    }

    @Test
    @DisplayName("Create order")
    @Description("Create order without authorization")
    public void CreateOrderWithoutAuthAndWithIngredientsTest() {
        Response orderBody = order.createOrder(payload,null);
        String bodyAsString = orderBody.asString();
        CurrentStatusCode = orderBody.statusCode();
        stepsTest.compareStatusCode200(CurrentStatusCode);
        stepsTest.comparePayloadSuccess(bodyAsString);
    }

    @Test
    @DisplayName("Create order")
    @Description("Create order without ingredients")

    public void CreateOrderWithAuthAndWithoutIngredientsTest() {
        UserData userData = randomUser();
        user.create(userData);

        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();

        String bearerToken = body.path("accessToken");

        Response orderBody = order.createOrder(noIngredientsPayload,bearerToken);
        String bodyAsString = orderBody.asString();
        CurrentStatusCode = orderBody.statusCode();
        stepsTest.compareStatusCode400(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);

        user.delete(bearerToken);
    }

    @Test
    @DisplayName("Create order")
    @Description("Create order with invalid ingredients ids")
    public void CreateOrderWithAuthAndInvalidIngredientsTest() {
        UserData userData = randomUser();
        user.create(userData);

        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();

        String bearerToken = body.path("accessToken");

        Response orderBody = order.createOrder(invalidIngredientsPayload,bearerToken);
        CurrentStatusCode = orderBody.statusCode();
        stepsTest.compareStatusCode500(CurrentStatusCode);

        user.delete(bearerToken);
    }
}

