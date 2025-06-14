package com.myapp.Airports.view;

import com.myapp.Airports.storage.api.*;
import com.myapp.Airports.model.*;
import com.myapp.Airports.view.api.IAirportsView;

import java.util.List;

public class AirportView implements IAirportsView {
    private IAirportsRepository repository;


    public void AirportsView(IAirportsRepository repository) {
        this.repository = repository;
    }

    public List<Airport> getAll() {
        return this.repository.findAll();
    }

}
