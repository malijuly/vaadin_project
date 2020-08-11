package com.nextbuy.demo.model;

import org.springframework.data.repository.query.Param;

import javax.persistence.*;


//DB Structure
//INSERT INTO TABLE
@Entity //class can be mapped to table
@Table(name = "DataWeather")
public class DataWeather {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    String temperature;
    String humidity;    //wilgotnosc powitrza
    String pressure;    //cisnienie potwietrza
    String height;      //wysokosc
    String UVLevel;     //natezenie promieniowania UV

    public DataWeather(String temperature, String humidity, String pressure, String height, String UVLevel) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.height = height;
        this.UVLevel = UVLevel;
    }

    public DataWeather(int id, String temperature, String humidity, String pressure, String height, String UVLevel) {

        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.height = height;
        this.UVLevel = UVLevel;
    }

    public DataWeather() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    void update(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName) {

    }



}
