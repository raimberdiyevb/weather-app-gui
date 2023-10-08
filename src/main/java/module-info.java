module com.raim.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.raim.weatherapp to javafx.fxml;
    exports com.raim.weatherapp;
}