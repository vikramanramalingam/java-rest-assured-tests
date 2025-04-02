package com.demo.base;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.utils.ConfigReader;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApi {
    protected static final Logger logger = LogManager.getLogger(BaseApi.class);
    protected String baseUrl;
    protected Map<String, String> headers;
    protected String authType;

    public BaseApi(String contentType){
        this("None", contentType);
    }

    public BaseApi(String authType, String contentType) {
        this.baseUrl = ConfigReader.getBaseUrl();
        this.authType = (authType != null && !authType.isEmpty()) ? authType : "NONE";
        System.out.println("The auth type is"+ authType);
        RestAssured.baseURI = baseUrl;
        RestAssured.config = RestAssuredConfig.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", 5000)
                    .setParam("http.socket.timeout", 5000));
        this.headers = getDefaultHeaders(contentType);
    }

    private RequestSpecification applyAuthentication(RequestSpecification requestSpec) {
        switch (authType.toLowerCase()) {
            case "basic":
                String username = ConfigReader.getProperty("basicAuth.username");
                String password = ConfigReader.getProperty("basicAuth.password");
                requestSpec.auth().preemptive().basic(username, password);
                break;
            
            case "apikey":
                String apiKey = ConfigReader.getProperty("api.key");
                requestSpec.header("Authorization", "Bearer " + apiKey);
                break;

            case "bearer":
                String bearerToken = ConfigReader.getProperty("bearer.token");
                requestSpec.header("Authorization", "Bearer " + bearerToken);
                break;
                
            case "none":
                logger.info("No authentication required");
                break;

            default:
                throw new IllegalArgumentException("Invalid authentication type: " + authType);
        }
        return requestSpec;
    }


    private Map<String, String> getDefaultHeaders(String contentType) {
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", contentType);
        defaultHeaders.put("Accept", contentType);
        return defaultHeaders;
    }

    protected void addHeaders(Map<String, String> additionalHeaders) {
        if (additionalHeaders != null) {
            this.headers.putAll(additionalHeaders);
        }
    }

    protected RequestSpecification getRequestSpec() {
        logger.info("Setting request headers: " + headers);
        RequestSpecification requestSpec = given()
                .filter(new AllureRestAssured())
                .headers(headers);

        return applyAuthentication(requestSpec);
    }

    protected Response getRequest(String endpoint) {
        logger.info("Sending GET request to: " + RestAssured.baseURI + endpoint);
        Response response = getRequestSpec()
                .when().get(endpoint)
                .then().extract().response();
        logger.info("Response Status Code for GET request is: " + response.getStatusCode());
        return response;
    }

    protected Response postRequest(String endpoint, Object payload) {
        logger.info("Sending POST request to: " + RestAssured.baseURI + endpoint);
        logger.info("Request Payload: {}", payload);
        Response response = getRequestSpec()
                .body(payload)
                .when().post(endpoint)
                .then().log().all().extract().response();
        logger.info("Response Status Code for POST request is: " + response.getStatusCode());
        return response;
    }

    protected Response putRequest(String endpoint, Object payload) {
        logger.info("Sending PUT request to: " + RestAssured.baseURI + endpoint);
        logger.info("Request Payload: {}", payload);
        Response response = getRequestSpec()
                .body(payload)
                .when().put(endpoint)
                .then().log().all().extract().response();
        logger.info("Response Status Code for PUT request is: " + response.getStatusCode());
        return response;
    }

    protected Response patchRequest(String endpoint, Object payload) {
        logger.info("Sending PATCH request to: " + RestAssured.baseURI + endpoint);
        logger.info("Request Payload: {}", payload);
        Response response = getRequestSpec()
                .body(payload)
                .when().patch(endpoint)
                .then().log().all().extract().response();
        logger.info("Response Status Code for Patch request is: " + response.getStatusCode());
        return response;
    }

    protected Response deleteRequest(String endpoint) {
        logger.info("Sending DELETE request to: " + RestAssured.baseURI + endpoint);
        Response response = getRequestSpec()
                .when().delete(endpoint)
                .then().log().all().extract().response();
        logger.info("Response Status Code for DELETE request is: " + response.getStatusCode());
        return response;
    }
}
