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
    public long count(FlyingFilter filter){
        return this.repository.countByArrivalAirportOrDepartureAirport(
                filter.getAirportOut(),
                filter.getAirportIn()
        );
    }

    @Override
    public List<Flying> getList(FlyingFilter filter) {
        return this.repository.findAllByArrivalAirportOrDepartureAirport(
                filter.getAirportOut(),
                filter.getAirportIn(),
                PageRequest.of(filter.getPage(), 20)
        );
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
