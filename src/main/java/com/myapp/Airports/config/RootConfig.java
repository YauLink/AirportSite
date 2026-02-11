package com.myapp.Airports.config;

import com.myapp.Airports.storage.api.IAirportsRepository;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import com.myapp.Airports.view.FlyingsView;
import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.AirportView;
import com.myapp.Airports.view.api.IFlyingsView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@ComponentScan("com.myapp.Airports.config")
public class RootConfig {

    @Bean
    public IAirportsView airportsView(IAirportsRepository repository){
        return new AirportView(repository);
    }

    @Bean
    public IFlyingsView flyingsView(IFlyingsRepository repository){
        return new FlyingsView(repository);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

