package com.demo.api;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.base.BaseApi;
import com.demo.utils.ConfigReader;

import io.restassured.response.Response;

public class ExchangeRateApi extends BaseApi {

    private static final Logger logger = LogManager.getLogger(ExchangeRateApi.class);
    private final Map<String, String> exchangeRateApiEndpoints;

    public ExchangeRateApi(){
        super("application/xml");
        this.exchangeRateApiEndpoints = ConfigReader.getPath("exchangeRatesApi");
    }

    public Response getExchangeRatesByTable(String table){
        logger.info("Fetching exchange based on table...");
        Response response = getRequest(exchangeRateApiEndpoints.get("exchangerates").replace("{table}", table));
        logger.info("Received response with status: " + response.getStatusCode());
        return response;
    }
    
}
