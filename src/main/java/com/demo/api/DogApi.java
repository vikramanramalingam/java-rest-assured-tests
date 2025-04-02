package com.demo.api;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.base.BaseApi;
import com.demo.utils.ConfigReader;

import io.restassured.response.Response;

public class DogApi extends BaseApi{

    private static final Logger logger = LogManager.getLogger(DogApi.class);
    private final Map<String, String> dogApiEndpoints;

    public DogApi(){
        super("application/json");
        this.dogApiEndpoints = ConfigReader.getPath("dogApi");
    }

    public Response listAllBreeds(){
        logger.info("Fetching all dog breeds...");
        Response response = getRequest(dogApiEndpoints.get("listallbreeds"));
        logger.info("Received response with status: " + response.getStatusCode());
        return response;
    }

    public Response getBreedImages(String breedName) {
        logger.info("Fetching breed images based on the given breed.");
        String formattedEndpoint = dogApiEndpoints.get("getImagesByBreed").replace("{breed}", breedName);
        Response response = getRequest(formattedEndpoint);
        logger.info("Received response with status: " + response.getStatusCode());
        return response;
    }

    public Response getSubBreeds(String breedName) {
        logger.info("Fetching sub breeds based on the given breed.");
        String formattedEndpoint = dogApiEndpoints.get("getSubBreeds").replace("{breed}", breedName);
        Response response = getRequest(formattedEndpoint);
        logger.info("Received response with status: " + response.getStatusCode());
        return response;
    }
    
}
