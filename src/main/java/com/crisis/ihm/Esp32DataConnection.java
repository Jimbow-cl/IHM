package com.crisis.ihm;

import com.crisis.ihm.utils.Mqtt;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Esp32DataConnection {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "http://222.175.15.19:8080/api/donnees-salle";

    public static void main(String[] args) throws IOException {
        getEsp32Data();
    }

    public static Esp32Data getEsp32Data() throws IOException {
        URL obj = new URL(GET_URL);
        java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == java.net.HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String raw = response.toString();
            try {
                String values = Mqtt.decrypt(raw, "Bs!6@rx?stdrSGmN7KMXE!PjqK!T8DK@MX7dDi85");
                //String values = Mqtt.decrypt(raw, "tatayoyo@");
                System.out.println(values);
                // Séparation des valeurs reçues
                String explodedValues[] = values.split("-");
                return new Esp32Data(Long.parseLong(explodedValues[0]), Long.parseLong(explodedValues[1]));
            } catch (Exception e) {
                System.out.println("GET request not worked");
                throw new RuntimeException(e);

            }

        }
        return null;
    }
}