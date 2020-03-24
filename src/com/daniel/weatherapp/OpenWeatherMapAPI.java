package com.daniel.weatherapp;

import java.io.IOException;
import java.net.*;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

public class OpenWeatherMapAPI extends WeatherAPI {

    private final String openWeatherMapAPIKey = "092e26613fb539437347719df4761e0a";
    private final String openWeatherMapUrl = "http://api.openweathermap.org/data/2.5/weather?";

    public OpenWeatherMapAPI() throws IOException {
        super();
    }

    @Override
    public String getWeatherAPI() throws URISyntaxException, IOException {

        URIBuilder url = new URIBuilder(this.openWeatherMapUrl);
        url.setParameter("lat", String.valueOf(this.lat)).setParameter("lon", String.valueOf(this.lon))
                .setParameter("appid", this.openWeatherMapAPIKey);

        URL paramUrl = url.build().toURL();

        return createHttpReq(paramUrl);
    }

    @Override
    public boolean isRaining(String jsonOpenWeatherMapAPI) {
        if (!jsonOpenWeatherMapAPI.isEmpty()) {
            JSONObject jsonObj = new JSONObject(jsonOpenWeatherMapAPI);
            if (jsonObj.has("weather") && jsonObj.has("id")) {
                int rainCodeValue = jsonObj.getJSONArray("weather").getJSONObject(0).getInt("id");

                if (rainCodeValue >= 500 && rainCodeValue <= 531) {
                    return true;
                }
                return false;
            } else {
                System.out.println("No such entity array name 'weather' in the json content");
                return false;
            }
        } else {
            System.out.println("Content is empty or null");
            return false;
        }
    }
}
