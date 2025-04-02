package com.demo.api;

import java.io.File;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.base.BaseApi;
import com.demo.utils.ConfigReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import io.restassured.response.Response;

public class UserApi extends BaseApi {

    private static final Logger logger = LogManager.getLogger(UserApi.class);
    private final Map<String, String> userApiEndpoints;
    private final String payloadDirectory = System.getProperty("user.dir") + "/src/test/resources/payloads/json/";
    private Map<String, Object> lastCreatedUserPayload;

    public UserApi() {
        super("application/json");
        this.userApiEndpoints = ConfigReader.getPath("petStoreApi", "user");
    }

    public Response createUser(String fileName) throws Exception {
        this.lastCreatedUserPayload = null;
        Map<String, Object> payload = generateUserPayload(payloadDirectory + fileName);
        this.lastCreatedUserPayload = payload;
        String userJson = new ObjectMapper().writeValueAsString(payload);
        logger.info("Creating a new user with payload: {}", userJson);
        Response response = postRequest(userApiEndpoints.get("createUser"), userJson);
        logger.info("Received response with status: {}", response.getStatusCode());
        return response;
    }

    public Response getUser(String username){
        logger.info("Fetching user by username...");
        Response response = getRequest(userApiEndpoints.get("user").replace("{username}", username));
        logger.info("Received response with status: " + response.getStatusCode());
        return response;
    }

    public Response updateUser(String username, String fileName) throws Exception {
        this.lastCreatedUserPayload = null;
        Map<String, Object> payload = generateUserPayload(payloadDirectory + fileName);
        this.lastCreatedUserPayload = payload;
        String userJson = new ObjectMapper().writeValueAsString(payload);
        logger.info("Updating user {} with payload: {}", username, userJson);
        Response response = putRequest(userApiEndpoints.get("user").replace("{username}", username), userJson);
        logger.info("Received response with status: {}", response.getStatusCode());
        return response;
    }

    public Response deleteUser(String username){
        logger.info("Deleting user by username...");
        Response response = deleteRequest(userApiEndpoints.get("user").replace("{username}", username));
        logger.info("Received response with status: " + response.getStatusCode());
        return response;
    }

    public Map<String, Object> getLastCreatedUserPayload() {
        return lastCreatedUserPayload;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> generateUserPayload(String filePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = objectMapper.readValue(new File(filePath), Map.class);
        Faker faker = new Faker();
        payload.put("id", System.currentTimeMillis());
        payload.put("username", faker.name().username());
        payload.put("firstName", faker.name().firstName());
        payload.put("lastName", faker.name().lastName());
        payload.put("email", faker.internet().emailAddress());
        payload.put("phone", faker.phoneNumber().cellPhone());
        payload.put("password", faker.internet().password(8, 16));
        return payload;
    }
    


    
}
