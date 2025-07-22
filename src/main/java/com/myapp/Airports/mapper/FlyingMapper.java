package com.myapp.Airports.mapper;

import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.model.Flying;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlyingMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Flying toEntity(FlyingDTO dto) {
        Flying entity = new Flying();
        entity.setFlightId(dto.getFlightId());
        entity.setFlightNo(dto.getFlightNo());
        entity.setScheduledDeparture(dto.getScheduledDeparture().format(FORMATTER));
        entity.setScheduledArrival(dto.getScheduledArrival().format(FORMATTER));
        entity.setDepartureAirport(dto.getDepartureAirport());
        entity.setArrivalAirport(dto.getArrivalAirport());
        entity.setStatus(dto.getStatus());
        entity.setAircraftCode(dto.getAircraftCode());

        if (dto.getActualDeparture() != null) {
            entity.setActualDeparture(dto.getActualDeparture().format(FORMATTER));
        }
        if (dto.getActualArrival() != null) {
            entity.setActualArrival(dto.getActualArrival().format(FORMATTER));
        }

        return entity;
    }

    public static FlyingDTO toDto(Flying entity) {
        FlyingDTO dto = new FlyingDTO();
        dto.setFlightId(entity.getFlightId());
        dto.setFlightNo(entity.getFlightNo());
        dto.setScheduledDeparture(LocalDateTime.parse(entity.getScheduledDeparture(), FORMATTER));
        dto.setScheduledArrival(LocalDateTime.parse(entity.getScheduledArrival(), FORMATTER));
        dto.setDepartureAirport(entity.getDepartureAirport());
        dto.setArrivalAirport(entity.getArrivalAirport());
        dto.setStatus(entity.getStatus());
        dto.setAircraftCode(entity.getAircraftCode());

        if (entity.getActualDeparture() != null) {
            dto.setActualDeparture(LocalDateTime.parse(entity.getActualDeparture(), FORMATTER));
        }
        if (entity.getActualArrival() != null) {
            dto.setActualArrival(LocalDateTime.parse(entity.getActualArrival(), FORMATTER));
        }

        return dto;
    }
}
