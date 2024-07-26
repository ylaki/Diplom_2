package orders;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static utils.Utils.randomNumber;

public class Order {

    private static final String ORDER_ENDPOINT = "api/orders";
    private static final String INGREDIENTS_ENDPOINT = "api/ingredients";

    @Step("Create order")
    public Response createOrder(String payload, String bearerToken) {
        RequestSpecification request = given()
                .header("Content-type", "application/json")
                .body(payload);

        if (bearerToken != null && !bearerToken.isEmpty()) {
            request.header("Authorization", bearerToken);
        }

        return request.when().post(ORDER_ENDPOINT);
    }

    @Step("Create order")
    public Response getOrders(String bearerToken) {
        RequestSpecification request = given()
                .header("Content-type", "application/json");

        if (bearerToken != null && !bearerToken.isEmpty()) {
            request.header("Authorization", bearerToken);
        }

        return request.when().get(ORDER_ENDPOINT);
    }

    @Step("Get ingredients")
    public Response getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(INGREDIENTS_ENDPOINT);
    }

        public String payloadGenerate() {
            // Parse JSON response
            Response ingredientsBody = getIngredients();
            String responseString = ingredientsBody.getBody().asString(); // Extracting response as String
            JsonObject responseObject = new com.google.gson.JsonParser().parse(responseString).getAsJsonObject();
            JsonArray data = responseObject.getAsJsonArray("data");

            // Generating indexes for ingredients array
            int index1 = randomNumber(10);
            int index2 = randomNumber(10);

            // Ensure ingredientID1 and ingredientID2 are always different
            while (index1 == index2) {
                index2 = randomNumber(10);
            }

            // Getting ingredients IDs using indexes
            JsonObject currentData1 = data.get(index1).getAsJsonObject();
            String ingredientID1 = currentData1.get("_id").getAsString();

            JsonObject currentData2 = data.get(index2).getAsJsonObject();
            String ingredientID2 = currentData2.get("_id").getAsString();

            return ("{\"ingredients\": [\"" + ingredientID1 + "\",\"" + ingredientID2 + "\"]}");
        }
    }