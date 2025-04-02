package exchangeRatesApiTests;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.demo.api.ExchangeRateApi;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

@Epic("XML API Testing")
@Feature("Test Exchange Rate XML based API endpoints")
public class ExchangeRatesApiTests {

    private static final Logger logger = LogManager.getLogger(ExchangeRatesApiTests.class);
    private ExchangeRateApi exchangeRateApi;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        exchangeRateApi = new ExchangeRateApi();
    }

    @Test(description = "Get call to exchange rates table",groups = {"smoke"},priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify get exchange rates table api endpoint is working fine")
    public void testExchangeRatesByTable() {
        logger.info("Starting testExchangeRatesByTable...");
        Response response = exchangeRateApi.getExchangeRatesByTable("A");
        XmlPath xmlPath = new XmlPath(response.asString());
        assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
        assertEquals(xmlPath.getString("ArrayOfExchangeRatesTable.ExchangeRatesTable.Table"), "A");
    }

    @Test(description = "validate exchange rates table scheme",groups = {"smoke"},priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the response of exchange rates table api is matching the schema")
    public void validateExchangeRatesByTableSchema() {
        logger.info("Starting validateExchangeRatesByTableSchema...");
        Response response = exchangeRateApi.getExchangeRatesByTable("A");
        response.then().assertThat().body(matchesXsdInClasspath("schemas/xml/exchangeRatesSchema.xsd"));
    }
    
}
