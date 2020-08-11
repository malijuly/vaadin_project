package com.nextbuy.demo.controller;

import com.nextbuy.demo.model.DataWeather;
import com.nextbuy.demo.repository.DataWeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/api/weather")
public class DataWeatherController {

    @Autowired
    private DataWeatherRepository repository;

    @GetMapping("/{id}")
    public DataWeather findById(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/")
    @ResponseBody
    public Collection<DataWeather> findDataWeathers() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DataWeather updateDataWeather(@PathVariable("id") final String id, @RequestBody final DataWeather dataWeather) {
        return dataWeather;
    }

}