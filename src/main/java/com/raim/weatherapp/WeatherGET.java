package com.raim.weatherapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherGET {
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);
        if(locationData == null) return null;
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString ="https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=Europe%2FBerlin";

        try{
            HttpURLConnection conn = fetchApiResponce(urlString);
            if(conn == null) return null;
            if (conn.getResponseCode() != 200) {
                System.out.println("Couldn't connect to API!");
                return null;
            }
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONParser jsonParser = new JSONParser();
            JSONObject resultJsonObject = (JSONObject) jsonParser.parse(resultJson.toString());

            JSONObject hourly = (JSONObject) resultJsonObject.get("hourly");

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weatherCode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            JSONArray windSpeedData = (JSONArray) hourly.get("windspeed_10m");
            double windSpeed = (double) windSpeedData.get(index);

            return constructJsonRes(temperature, weatherCondition, humidity,windSpeed);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static JSONObject constructJsonRes(double temperatureData, String weatherData,
                                        long humidityData, double windSpeedData){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("temperature",temperatureData);
        jsonObject.put("weather",weatherData);
        jsonObject.put("humidity",humidityData);
        jsonObject.put("windspeed",windSpeedData);
        System.out.println(JSONObject.toJSONString(jsonObject));
        return jsonObject;
    }
    private static String convertWeatherCode(long wCode) {
        String weatherCondition = "";
        if(wCode == 0L){
            weatherCondition = "Clear";
        }else if (wCode > 0L && wCode <= 3L) {
            weatherCondition = "Cloudy";
        }else if ((wCode > 51L && wCode <= 67L) || (wCode >= 80L && wCode <= 99L)) {
            weatherCondition = "Rain";
        }else if (wCode >= 71L && wCode <= 77L) {
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }

    public static int findIndexOfCurrentTime(JSONArray timeArr){
        String currentTime = getCurrentTime();
        for (int i = 0; i < timeArr.size(); i++) {
            String time = (String) timeArr.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        return localDateTime.format(dateTimeFormatter);
    }

    public static JSONArray getLocationData(String locationName){
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try{
            HttpURLConnection conn = fetchApiResponce(urlString);

            if(conn != null && conn.getResponseCode() != 200){
                System.out.println("Couldn't connect to API!");
                return null;
            }else if(conn != null){
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObject = (JSONObject) parser.parse(String.valueOf(resultJson));

                return (JSONArray) resultsJsonObject.get("results");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection fetchApiResponce(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
