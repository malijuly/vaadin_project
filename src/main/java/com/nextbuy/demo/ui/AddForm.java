package com.nextbuy.demo.ui;

import com.nextbuy.demo.model.DataWeather;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

public class AddForm extends FormLayout {

    TextField temperature = new TextField(("Temperature"));
    TextField humidity = new TextField(("Himudity"));
    TextField pressure = new TextField(("Pressure"));
    TextField height = new TextField(("Height"));
    TextField UVLevel = new TextField(("UV Level"));

    Button save = new Button("SAVE");
    Button delete = new Button("DELETE");
    Button close = new Button("CLOSE");

    Binder<DataWeather> binder = new BeanValidationBinder<>(DataWeather.class);


    public AddForm(){

        addClassName("list-view");

        binder.bindInstanceFields(this);

        add(
                temperature,
                humidity,
                pressure,
                height,
                UVLevel,
                createButtonsLayout()
        );
    }

    private Component createButtonsLayout(){

        save.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);


        return new HorizontalLayout(save, delete, close);
   }



}
