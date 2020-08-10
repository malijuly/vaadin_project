package com.example.demo.model;

import javax.persistence.*;
import java.util.Objects;

//@Entity //class can be mapped to table
//@Table(name = "DataWeather")
public class DataWeather {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    String temperature;
//    String humidity;    //wilgotnosc powitrza
//    String pressure;    //cisnienie potwietrza
//    String height;      //wysokosc
//    String UVLevel;     //natezenie promieniowania UV
//
//    public DataWeather() {
//    }
//
//    public DataWeather(Long id, String temperature, String humidity, String pressure, String height, String UVLevel) {
//
//        this.id = id;
//        this.temperature = temperature;
//        this.humidity = humidity;
//        this.pressure = pressure;
//        this.height = height;
//        this.UVLevel = UVLevel;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(String temperature) {
//        this.temperature = temperature;
//    }
//
//    public String getHumidity() {
//        return humidity;
//    }
//
//    public void setHumidity(String humidity) {
//        this.humidity = humidity;
//    }
//
//    public String getPressure() {
//        return pressure;
//    }
//
//    public void setPressure(String pressure) {
//        this.pressure = pressure;
//    }
//
//    public String getHeight() {
//        return height;
//    }
//
//    public void setHeight(String height) {
//        this.height = height;
//    }
//
//    public String getUVLevel() {
//        return UVLevel;
//    }
//
//    public void setUVLevel(String UVLevel) {
//        this.UVLevel = UVLevel;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 79 * hash + Objects.hashCode(this.id);
//        hash = 79 * hash + Objects.hashCode(this.name);
//        hash = 79 * hash + this.population;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final City other = (City) obj;
//        if (this.population != other.population) {
//            return false;
//        }
//        if (!Objects.equals(this.name, other.name)) {
//            return false;
//        }
//        return Objects.equals(this.id, other.id);
//    }
//
//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder("City{");
//        sb.append("id=").append(id);
//        sb.append(", name='").append(name).append('\'');
//        sb.append(", population=").append(population);
//        sb.append('}');
//        return sb.toString();
//    }
}
