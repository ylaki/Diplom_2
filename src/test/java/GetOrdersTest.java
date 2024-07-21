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

public class GetOrdersTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String payload = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa72\"]}";
    private Order order;
    private int CurrentStatusCode;
    StepsTest stepsTest = new StepsTest();
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = new User();
        order = new Order();
    }

    @Test
    @DisplayName("Get orders list")
    @Description("Create orders list by authorized user")
    public void GetOrdersList() {
        UserData userData = randomUser();
        user.create(userData);

        UserData loginCredentials = new UserData()
                .withEmail(userData.getEmail())
                .withPassword(userData.getPassword());
        Response loginResponse = user.login(loginCredentials);
        ResponseBody body = loginResponse.getBody();

        String bearerToken = body.path("accessToken");

        order.createOrder(payload,bearerToken);
        Response getOrders = order.getOrders(bearerToken);
        String bodyAsString = getOrders.asString();
        CurrentStatusCode = getOrders.statusCode();
        stepsTest.compareStatusCode200(CurrentStatusCode);
        stepsTest.comparePayloadSuccess(bodyAsString);

        user.delete(bearerToken);
    }

    @Test
    @DisplayName("Get orders list")
    @Description("Create orders list without authorization")
    public void GetOrdersListWithoutAuth() {
        order.createOrder(payload,null);
        Response getOrders = order.getOrders(null);
        String bodyAsString = getOrders.asString();
        CurrentStatusCode = getOrders.statusCode();
        stepsTest.compareStatusCode401(CurrentStatusCode);
        stepsTest.comparePayloadFalse(bodyAsString);
    }
}
