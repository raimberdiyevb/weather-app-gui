package com.raim.weatherapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;

public class WeatherController {
    public ImageView image1;
    public ImageView image2;
    public ImageView image3;


    @FXML
    public ImageView searchButton;
    @FXML
    public TextField searchTextField;
    @FXML
    public Label temperatureLabel;
    @FXML
    public ImageView windspeedImage;
    @FXML
    public ImageView humidityImage;
    @FXML
    public Label weatherConditionLabel;
    @FXML
    public Label humidityLabel;
    @FXML
    public Label windspeedLabel;
    @FXML
    public ImageView weatherConditionImage;
    /*
        jsonObject.put("temperature",temperatureData);    double
        jsonObject.put("weather",weatherData);            String
        jsonObject.put("humidity",humidityData);          long
        jsonObject.put("windspeed",windSpeedData);        long
     */

    public void searchButtonClicked(MouseEvent event) {
        System.out.println("Search Button Clicked");
        String inputCity = searchTextField.getText();
        inputCity = inputCity.trim();
        JSONObject weatherData = WeatherGET.getWeatherData(inputCity);

        //temperature
        updateTemperatureComponents((Double) weatherData.get("temperature"));
        //humidity
        updateHumidityComponents((Long) weatherData.get("humidity"));
        //windspeed
        updateWindSpeedComponents((double) weatherData.get("windspeed"));
        //weatherCondition
        updateWeatherComponents((String) weatherData.get("weather"));
    }
    private void updateTemperatureComponents(double temperature){
        DecimalFormat format = new DecimalFormat("##.#");
        temperatureLabel.setText(format.format(temperature) + " C");
    }
    private void updateWeatherComponents(String weatherCondition){
        System.out.println("Current Weather Condition : " + weatherCondition);
        Image newImage;
        String imageUrl = switch (weatherCondition) {
            case "Clear"  -> "/com/raim/weatherapp/weatherapp_images/clear.png";
            case "Cloudy" -> "/com/raim/weatherapp/weatherapp_images/cloudy.png";
            case "Rain"   -> "/com/raim/weatherapp/weatherapp_images/rain.png";
            default       -> "/com/raim/weatherapp/weatherapp_images/snow.png";
        };
        System.out.println("URL for an image : " + imageUrl);
        newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrl)));

        weatherConditionImage.setImage(newImage);
        weatherConditionLabel.setText(weatherCondition);
    }
    private void updateHumidityComponents(long humidity){
        humidityLabel.setText(humidity + " %");
    }
    private void updateWindSpeedComponents(double windSpeed){
        windspeedLabel.setText(windSpeed + " km/h");
    }
}