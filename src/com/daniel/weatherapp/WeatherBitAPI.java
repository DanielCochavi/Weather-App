package com.daniel.weatherapp;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class WeatherBitAPI extends WeatherAPI {

    private final String weatherBitAPIKey = "030f75e61de14c1d9412c62f76795c5a";
    private final String weatherBitUrl = "http://api.weatherbit.io/v2.0/current?";

    public WeatherBitAPI() throws IOException {
        super();
    }

    @Override
    public String getWeatherAPI() throws URISyntaxException, IOException {

        URIBuilder url = new URIBuilder(this.weatherBitUrl);
        url.setParameter("lat", String.valueOf(lat)).setParameter("lon", String.valueOf(lon))
                .setParameter("key", this.weatherBitAPIKey);

        URL paramUrl = url.build().toURL();

        return createHttpReq(paramUrl);
    }

    @Override
    public boolean isRaining(String jsonWeatherBitAPI) {
        if (!jsonWeatherBitAPI.isEmpty()) {
            JSONObject jsonObj = new JSONObject(jsonWeatherBitAPI);
            if (jsonObj.getJSONArray("data").getJSONObject(0).has("weather")) {
                int rainCodeValue = jsonObj.getJSONArray("data").getJSONObject(0).getJSONObject("weather")
                        .getInt("code");

                if ((rainCodeValue >= 500 && rainCodeValue <= 522) || (rainCodeValue >= 200 && rainCodeValue <= 202)) {
                    return true;
                }
                return false;
            } else {
                System.out.println("No such entity array name 'data' or 'weather' in the json content");
                return false;
            }
        } else {
            System.out.println("Content is empty or null");
            return false;
        }
    }
}
