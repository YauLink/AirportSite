package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.model.BoardingPassId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IBoardingPassRepository extends JpaRepository<BoardingPass, BoardingPassId> {

    @Query("select coalesce(max(bp.boardingNo), 0) from BoardingPass bp where bp.id.flightId = :flightId")
    Integer findMaxBoardingNoByFlightId(@Param("flightId") Integer flightId);
}

