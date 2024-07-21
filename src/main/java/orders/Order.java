package orders;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
public class Order {

    private static final String ORDER_ENDPOINT = "api/orders";

    @Step("Create order")
    public Response createOrder(String payload,String bearerToken)
    {
        RequestSpecification request = given()
                .header("Content-type", "application/json")
                .body(payload);

        if (bearerToken != null && !bearerToken.isEmpty()) {
            request.header("Authorization", bearerToken);
        }

        return request.when().post(ORDER_ENDPOINT);
    }

    @Step("Create order")
    public Response getOrders(String bearerToken)
    {
        RequestSpecification request = given()
                .header("Content-type", "application/json");

        if (bearerToken != null && !bearerToken.isEmpty()) {
            request.header("Authorization", bearerToken);
        }

        return request.when().get(ORDER_ENDPOINT);
    }

}
