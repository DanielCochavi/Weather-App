package com.daniel.weatherapp;

import javafx.util.Pair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Geolocation {
    private final String geolocationUrl;

    public Geolocation(String geolocationUrl) {
        this.geolocationUrl = geolocationUrl;
    }

    public Pair<Double, Double> getGeolocation() throws IOException {
        URL url = new URL(this.geolocationUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        JSONObject jsonObj = new JSONObject(content.toString());
        double latitude, longitude;

        if (jsonObj.isNull("latitude") && !jsonObj.isNull("longitude")) {
            longitude = jsonObj.getDouble("longitude");
            return new Pair<>(0.0, longitude);
        } else if (!jsonObj.isNull("latitude") && jsonObj.isNull("longitude")) {
            latitude = jsonObj.getDouble("latitude");
            return new Pair<>(latitude, 0.0);
        }
        latitude = jsonObj.getDouble("latitude");
        longitude = jsonObj.getDouble("longitude");
        return new Pair<>(latitude, longitude);
    }
}
