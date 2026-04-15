package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.model.BoardingPassId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBoardingPassRepository extends JpaRepository<BoardingPass, BoardingPassId> {
}

