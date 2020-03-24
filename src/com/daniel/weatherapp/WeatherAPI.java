package com.daniel.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

abstract public class WeatherAPI implements Runnable{

    public Geolocation geolocation = new Geolocation(WeatherApp.ipStackUrl);
    public final double lat;
    public final double lon;

    public WeatherAPI() throws IOException {
        this.lat = geolocation.getGeolocation().getKey(); // get latitude
        this.lon = geolocation.getGeolocation().getValue(); // get longitude
    }

    public abstract String getWeatherAPI() throws URISyntaxException, IOException;

    public abstract boolean isRaining(String jsonAPI);

    public String createHttpReq(URL paramUrl) throws IOException {
        HttpURLConnection con = (HttpURLConnection) paramUrl.openConnection();

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        // Handling errors
        if  (con.getResponseCode() != 200) {
            return "Http error code " +  con.getResponseCode();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        return content.toString();
    }

    @Override
    public void run() {
        try {
            isRaining(getWeatherAPI());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}
