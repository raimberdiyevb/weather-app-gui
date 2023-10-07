package com.raim.weatherapp;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class WeatherController {
    public ImageView image1;
    public ImageView image2;
    public ImageView image3;


    @FXML
    public ImageView searchButton;

    public void searchButtonClicked(MouseEvent event) {
        System.out.println("Search Button Clicked");
    }
}