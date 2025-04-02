package petStoreTests;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.demo.api.UserApi;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Epic("Pet Store API Testing")
@Feature("User API endpoints")
public class UserApiTests {

    private static final Logger logger = LogManager.getLogger(UserApiTests.class);
    private UserApi userApi;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        userApi = new UserApi();
    }

    @Test(groups = {"smoke"},priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that on trying to fetch random user proper error is returned")
    public void getRandomUser() throws Exception {
        logger.info("Starting get random user test...");
        Response response = userApi.getUser("xys13");
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");
        assertThat("Status code mismatch!", response.getStatusCode(), is(404));
        assertThat("Error message mismatch!", jsonPath.getString("message"), equalTo("User not found"));
    }

    @Test(groups = {"smoke"},priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that new user can be created and the same user can be fetched")
    public void createNewUser() throws Exception {
        logger.info("Starting create new user test...");
        Response userResponse = userApi.createUser("createUser.json");
        await().atMost(30, TimeUnit.SECONDS).pollInterval(5, TimeUnit.SECONDS).until(() -> userApi.getUser(userApi.getLastCreatedUserPayload().get("username").toString()).getStatusCode() == 200);
        Map<String, Object> requestPayload = userApi.getLastCreatedUserPayload();
        String username = (String) requestPayload.get("username");
        Response getUserResponse = userApi.getUser(username);
        JsonPath jsonPath = getUserResponse.jsonPath();
        assertThat("Status code mismatch!", userResponse.getStatusCode(), is(200));
        assertThat("Firstname mismatch!", jsonPath.getString("firstName"), equalTo(requestPayload.get("firstName")));
        assertThat("LastName mismatch!", jsonPath.getString("lastName"), equalTo(requestPayload.get("lastName")));
        assertThat("Email mismatch!", jsonPath.getString("email"), equalTo(requestPayload.get("email")));
        assertThat("Password mismatch!", jsonPath.getString("password"), equalTo(requestPayload.get("password")));
    }

    @Test(groups = {"smoke"},priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that new user can be created and the same user can be updated")
    public void updateUser() throws Exception {
        logger.info("Starting update user test...");
        userApi.createUser("createUser.json");
        await().atMost(15, TimeUnit.SECONDS).pollInterval(2, TimeUnit.SECONDS).until(() -> userApi.getUser(userApi.getLastCreatedUserPayload().get("username").toString()).getStatusCode() == 200);
        Map<String, Object> requestPayload = userApi.getLastCreatedUserPayload();
        String username = (String) requestPayload.get("username");
        Response updateUserResponse = userApi.updateUser(username,"createUser.json");
        await().atMost(15, TimeUnit.SECONDS).pollInterval(2, TimeUnit.SECONDS).until(() -> userApi.getUser(userApi.getLastCreatedUserPayload().get("username").toString()).getStatusCode() == 200);
        Map<String, Object> updatedRequestPayload = userApi.getLastCreatedUserPayload();
        String updatedUsername = (String) updatedRequestPayload.get("username");
        Response getUserResponse = userApi.getUser(updatedUsername);
        JsonPath jsonPath = getUserResponse.jsonPath();
        assertThat("Status code mismatch!", updateUserResponse.getStatusCode(), is(200));
        assertThat("Firstname mismatch!", jsonPath.getString("firstName"), equalTo(updatedRequestPayload.get("firstName")));
        assertThat("LastName mismatch!", jsonPath.getString("lastName"), equalTo(updatedRequestPayload.get("lastName")));
        assertThat("Email mismatch!", jsonPath.getString("email"), equalTo(updatedRequestPayload.get("email")));
        assertThat("Password mismatch!", jsonPath.getString("password"), equalTo(updatedRequestPayload.get("password")));
    }

    @Test(groups = {"smoke"},priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that new user can be created and the same user can be deleted")
    public void deleteUser() throws Exception {
        logger.info("Starting delete user test...");
        userApi.createUser("createUser.json");
        await().atMost(30, TimeUnit.SECONDS).pollInterval(5, TimeUnit.SECONDS).until(() -> userApi.getUser(userApi.getLastCreatedUserPayload().get("username").toString()).getStatusCode() == 200);
        Map<String, Object> requestPayload = userApi.getLastCreatedUserPayload();
        String username = (String) requestPayload.get("username");
        Response deleteUserResponse = userApi.deleteUser(username);
        Thread.sleep(5000);
        Response getUserResponse = userApi.getUser(username);
        JsonPath jsonPath = getUserResponse.jsonPath();
        assertThat("Status code mismatch!", deleteUserResponse.getStatusCode(), is(200));
        assertThat("Status code mismatch!", getUserResponse.getStatusCode(), is(404));
        assertThat("Error message mismatch!", jsonPath.getString("message"), equalTo("User not found"));
    }

    @Test(groups = {"smoke"},priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that scheme matches properly on making get user api endpoint")
    public void schemaValidate() throws Exception {
        logger.info("Starting scheme validation user test...");
        userApi.createUser("createUser.json");
        await().atMost(30, TimeUnit.SECONDS).pollInterval(5, TimeUnit.SECONDS).until(() -> userApi.getUser(userApi.getLastCreatedUserPayload().get("username").toString()).getStatusCode() == 200);
        Map<String, Object> requestPayload = userApi.getLastCreatedUserPayload();
        String username = (String) requestPayload.get("username");
        Response getUserResponse = userApi.getUser(username);
        getUserResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/json/userSchema.json"));
    }
    
}
