package com.myapp.Airports.view;

import com.myapp.Airports.view.api.IFlyingsView;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class FlyingsView implements IFlyingsView {
    private final IFlyingsRepository repository;

    public FlyingsView(IFlyingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public long count(FlyingFilter filter) {
        return repository.countByDepartureAirportAndArrivalAirport(
                filter.getAirportOut(),
                filter.getAirportIn()
        );
    }

    @Override
    public List<Flying> getList(FlyingFilter filter) {
        System.out.println("DEBUG: getList called -> departure=" + filter.getAirportOut() +
                ", arrival=" + filter.getAirportIn());

        List<Flying> flights = repository.findAllByDepartureAirportAndArrivalAirport(
                filter.getAirportOut(),
                filter.getAirportIn(),
                PageRequest.of(filter.getPage() - 1, 20)
        );

        System.out.println("DEBUG: flights returned = " + flights.size());
        flights.forEach(f -> System.out.println(f.getFlightNo() + ": " + f.getDepartureAirport() + "->" + f.getArrivalAirport()));

        return flights;
    }

    @Override
    public Flying get(Integer key) {
        return this.repository.getById(key);
    }

    @Transactional
    public void save(Flying flying){
        this.repository.save(flying);
    }
}
