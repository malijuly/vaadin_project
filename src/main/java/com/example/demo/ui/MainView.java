package com.example.demo.ui;
import com.example.demo.backend.WeatherStation;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("")  //--klasa zarzadzana przez vadina   //w nawiasie daje sie nazwe to co dajemy po sleszu http://localhost:8080/abc
public class MainView extends VerticalLayout {



    Grid<WeatherStation> grid = new Grid<>(WeatherStation.class);
    WeatherStation ws = new WeatherStation("10","10","10","10","10");



    public MainView() {

        //tworzenie tabeli
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        add(grid);

        updateList();


    }

    private void updateList() {

        grid.setItems(ws);  //dodawnie elementow do tablicy
    }

    void configureGrid(){
        //grid.addClassName("contact=grid");
        grid.setSizeFull();
        //grid.setColumns("frstName","lastName", "email");    //kolejnosc ustawienia kolumn
    }


}
