package com.example.demo.backend;

public class WeatherStation {

    double temperature;
    double humidity;    //wilgotnosc powitrza
    double pressure;    //cisnienie potwietrza
    double height;  //wysokosc
    double UVLevel; //natezenie promieniowania UV

    public WeatherStation(double temperature, double humidity, double pressure, double height, double UVLevel) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.height = height;
        this.UVLevel = UVLevel;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getUVLevel() {
        return UVLevel;
    }

    public void setUVLevel(double UVLevel) {
        this.UVLevel = UVLevel;
    }
}
