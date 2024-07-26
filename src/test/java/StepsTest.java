import io.qameta.allure.Step;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class StepsTest {
    String successfulResponse = "\"success\":true";
    String failedResponse = "\"success\":false";
    String userAlreadyExistsResponse = "User already exists";
    String missingFieldsResponse = "Email, password and name are required fields";
//    private User user;

    @Step("Compare status code 200 with body")
    public void compareStatusCode200(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_OK, CurrentStatusCode);
    }

    @Step("Compare status code 403 with response")
    public void compareStatusCode403(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_FORBIDDEN, CurrentStatusCode);
    }

    @Step("Compare status code 400 with response")
    public void compareStatusCode400(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_BAD_REQUEST, CurrentStatusCode);
    }

    @Step("Compare expected response with success:true")
    public void comparePayloadSuccess(String bodyAsString) {
        assertTrue(bodyAsString.contains(successfulResponse));
    }

    @Step("Compare expected response with success:false")
    public void comparePayloadFalse(String bodyAsString) {
        assertTrue(bodyAsString.contains(failedResponse));
    }

    @Step("Compare expected response with returned message")
    public void comparePayloadAlreadyExists(String bodyAsString) {
        assertTrue(bodyAsString.contains(userAlreadyExistsResponse));
    }

    @Step("Compare expected response with returned message")
    public void comparePayloadMissingField(String bodyAsString) {
        assertTrue(bodyAsString.contains(missingFieldsResponse));
    }

    @Step("Compare status code 401 with response")
    public void compareStatusCode401(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_UNAUTHORIZED, CurrentStatusCode);
    }

    @Step("Compare status code 500 with response")
    public void compareStatusCode500(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_INTERNAL_SERVER_ERROR, CurrentStatusCode);
    }
}
