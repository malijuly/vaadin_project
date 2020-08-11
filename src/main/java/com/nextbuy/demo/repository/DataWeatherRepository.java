package com.nextbuy.demo.repository;

import com.nextbuy.demo.model.DataWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataWeatherRepository extends JpaRepository<DataWeather, Integer> {
}