package com.daniel.weatherapp;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;

public class WeatherStackAPI extends WeatherAPI {

    private final String weatherStackAPIKey = "62a0600eaf5739cc0928549e2048c6d1";
    private final String weatherStackAPIUrl = "http://api.weatherstack.com/current?";

    public WeatherStackAPI() throws IOException {
        super();
    }

    @Override
    public String getWeatherAPI() throws URISyntaxException, IOException {
        URIBuilder url = new URIBuilder(this.weatherStackAPIUrl);
        url.setParameter("access_key", this.weatherStackAPIKey).setParameter("query", lat +
                (",") + lon);
        URL paramUrl = url.build().toURL();

        return createHttpReq(paramUrl);
    }

    @Override
    public boolean isRaining(String jsonWeatherStackAPI) {
        if (!jsonWeatherStackAPI.isEmpty()) {
            JSONObject jsonObj = new JSONObject(jsonWeatherStackAPI);
            if (jsonObj.has("request") && jsonObj.has("current")) {
                int rainCodeValue = jsonObj.getJSONObject("current").getInt("weather_code");

                if (rainCodeValue >= 293 && rainCodeValue <= 314 || (rainCodeValue >= 353 && rainCodeValue <= 359) ||
                        (rainCodeValue >= 386 && rainCodeValue <= 389)) {
                    return true;
                }
                return false;
            } else {
                System.out.println("No such entity array name 'request' or 'current' in the json content");
                return false;
            }
        } else {
            System.out.println("Content is empty or null");
            return false;
        }
    }
}
