package com.nextbuy.demo.ui;

import com.nextbuy.demo.model.DataWeather;
import com.nextbuy.demo.repository.DataWeatherRepository;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

//    WeatherStation weatherStation = new WeatherStation("9","9","9","9","9");

    //final AddForm form;
    Grid<DataWeather> grid = new Grid<>(DataWeather.class);
    DataWeather dataWeather = new DataWeather(1,"9","9","9","9","9");


    private DataWeatherRepository weatherRepository;
    //TextField filterText = new TextField();   //to filter text

    @Autowired
    public MainView(DataWeatherRepository weatherRepository) {


        //insert to table
        this.weatherRepository = weatherRepository;
        weatherRepository.save(dataWeather);

        //create grid
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        //configureFilter();
        add(grid);

        //new ContactForm -- AddForm
//        form = new AddForm();
//        Div content = new Div(grid,form);
//        content.addClassName("content");
//        content.setSizeFull();
//        add(content);



        //end enter data manually

        addManually();
        updateList();

    }

    private void updateList() {

        grid.setItems(weatherRepository.findAll());  //dodawnie elementow do tablicy
    }

    void configureGrid(){
        //grid.addClassName("contact=grid");
        grid.setSizeFull();
        grid.setColumns("temperature", "humidity", "pressure", "height", "UVLevel");    //kolejnosc ustawienia kolumn
    }

    //search by date
//    private void configureFilter(){
//
//        filterText.setPlaceholder("Filter by date");
//        filterText.setClearButtonVisible(true);
//        filterText.setValueChangeMode(ValueChangeMode.LAZY);
//        //what happen when text change
//        filterText.addValueChangeListener(e -> updateList());
//
//    }

    void addManually(){

        //enter data manually
        Button addRandomElementButton = new Button("ADD");
        Label label = new Label("Enter data manually");

        TextField temperatureTextField = new TextField("Temperature");
        TextField humidityTextField = new TextField("Humidity");
        TextField pressureTextField = new TextField("Pressure");
        TextField heightTextField = new TextField("Height");
        TextField UVLevelTextField = new TextField("UV level");

        dataWeather.setTemperature(temperatureTextField.getValue());

        addRandomElementButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {

            //add value from txtField
            dataWeather.setTemperature(temperatureTextField.getValue());
            dataWeather.setHumidity(humidityTextField.getValue());
            dataWeather.setPressure(pressureTextField.getValue());
            dataWeather.setHeight(heightTextField.getValue());
            dataWeather.setUVLevel(UVLevelTextField.getValue());

            weatherRepository.save(new DataWeather(dataWeather.getTemperature(),
                    dataWeather.getHumidity(),
                    dataWeather.getPressure(),
                    dataWeather.getHeight(),
                    dataWeather.getUVLevel()));

            grid.setItems(weatherRepository.findAll());
        });
        add(label, temperatureTextField, humidityTextField, pressureTextField, heightTextField, UVLevelTextField, addRandomElementButton);

    }


}
