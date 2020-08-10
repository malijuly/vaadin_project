package com.example.demo.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;


@Route("login")
@PageTitle("Login | WeatherStation")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    LoginForm login = new LoginForm();

    //constructor
    public LoginView() {

        addClassName("login-view");
        setSizeFull();

        //set in the center
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");

        add(
                //header
                new H1("Please sign in"),
                login
        );
    }

    //from BeforeEnterObserver
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        if(!beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .getOrDefault("error",Collections.emptyList())
            .isEmpty()){
            login.setError(true);
        }

    }
}
