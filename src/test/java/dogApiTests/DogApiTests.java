package dogApiTests;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.demo.api.DogApi;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import utils.DataProviderUtil;

@Epic("Dog API Testing")
@Feature("Test Dog API endpoints")
public class DogApiTests {

    private static final Logger logger = LogManager.getLogger(DogApiTests.class);
    private DogApi dogApi;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        dogApi = new DogApi();
    }

    @Test(description = "Get call to Get By Breeds Api",groups = {"smoke"},priority = 1)
    @AllureId("71")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify get by breeds api endpoint is working fine")
    public void testGetByBreeds() {
        logger.info("Starting getByBreedsTest...");
        Response response = dogApi.getBreedImages("hound");
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(description = "Get call to Get All Breeds Api",groups = {"smoke"},priority = 2)
    @AllureId("72")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify get all breeds api endpoint is working fine")
    public void testGetAllBreeds() {
        logger.info("Starting getAllBreedsTest...");
        Response response = dogApi.listAllBreeds();
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(description = "Get call to Get By Sub Breeds Api",groups = {"smoke"},priority = 3)
    @AllureId("73")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify get sub breeds api endpoint is working fine")
    public void testGetSubBreeds() {
        logger.info("Starting getSubBreedsTest...");
        Response response = dogApi.getSubBreeds("bulldog");
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(description = "Validate Json response of get all breeds api endpoint",groups = {"smoke"},priority = 4)
    @AllureId("74")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the json response of the get all breeds api endpoint")
    public void validateJsonArraySize(){
        logger.info("Starting validate json array for getAllBreeds api endpoint...");
        Response response = dogApi.listAllBreeds();
        String breedsJson = response.getBody().asString();
        Map<String, Object> breeds = response.jsonPath().getMap("message");
        logger.info("Parsing JSON response and validating breed list size...");
        Assert.assertNotNull(breedsJson, "Response json is null!");
        Assert.assertNotNull(breeds, "Breeds list is null!");
        Assert.assertTrue(breeds.size() > 0, "Breeds list is empty!");
        logger.info("Validation successful! JSON contains " + breeds.size() + " breeds.");
    }

    @Test(description = "Validate Json response using hamcrest matchers",groups = {"sanity"},priority = 5)
    @AllureId("75")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the json response using hamcrest matchers")
    public void useHamcrestMatchers(){
        logger.info("Starting validate json array using hamcrest matchers");
        Response response = dogApi.listAllBreeds();
        Map<String, Object> breeds = response.jsonPath().getMap("message");
        logger.info("Parsing JSON response and validating breed list size...");
        assertThat("Response should not be null", response.getBody().asString(), notNullValue());
        assertThat("Breeds list should not be null", breeds, notNullValue());
        assertThat("Breeds list should not be empty", breeds.size(), greaterThan(0));
        logger.info("Validation successful! JSON contains " + breeds.size() + " breeds.");
    }

    @Test(description = "Validate request chaining",groups = {"sanity"},priority = 6)
    @AllureId("76")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that we can get the breed name from get all breeds api and use it in get by breeds endpoint")
    public void requestChaining(){
        logger.info("Starting request chaining...");
        Response response = dogApi.listAllBreeds();
        Map<String, Object> breedsMap = response.jsonPath().getMap("message");
        List<String> breeds = breedsMap.keySet().stream().toList();
        String randomBreed = breeds.get(new Random().nextInt(breeds.size()));
        Response byBreedsResponse = dogApi.getBreedImages(randomBreed);
        List<String> images = byBreedsResponse.jsonPath().get("message");
        logger.info("Total images fetched for {}: {}", randomBreed, images.size());
        assertThat("Image list should not be empty for breed:", images, is(not(empty())));
        assertThat("Check the image url ends with jpp:", images, everyItem(endsWith(".jpg")));
        logger.info("Request chaining completed successfully.");
    }

    @Test(dataProvider = "dogBreedsData",dataProviderClass = DataProviderUtil.class, description = "Data Driven testing for get by breeds api endpoint",groups = {"sanity"},priority = 7)
    @AllureId("77")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that for get by breeds api endpoint we can do data driven testing")
    public void dataDrivenTests(String breed){
        logger.info("Starting dataDrivenTests for get by breeds api...");
        Allure.step("Executing API call for breed: " + breed);
        logger.info("Starting dataDrivenTests for breed: " + breed);
        Response response = dogApi.getBreedImages(breed);
        Allure.step("Validating response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }
}
