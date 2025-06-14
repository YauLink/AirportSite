package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IAirportsRepository extends JpaRepository<Airport, String> {
}
