package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAircraftRepository extends JpaRepository<Aircraft, String> {
}
