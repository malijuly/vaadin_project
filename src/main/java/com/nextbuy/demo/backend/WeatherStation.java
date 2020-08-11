package com.nextbuy.demo.backend;

public class WeatherStation {

    String temperature;
    String humidity;    //wilgotnosc powitrza
    String pressure;    //cisnienie potwietrza
    String height;      //wysokosc
    String UVLevel;     //natezenie promieniowania UV


    public WeatherStation(String temperature, String humidity, String pressure, String height, String UVLevel) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.height = height;
        this.UVLevel = UVLevel;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getUVLevel() {
        return UVLevel;
    }

    public void setUVLevel(String UVLevel) {
        this.UVLevel = UVLevel;
    }


}
