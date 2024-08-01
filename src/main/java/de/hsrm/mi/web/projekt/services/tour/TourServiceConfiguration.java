package de.hsrm.mi.web.projekt.services.tour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.hsrm.mi.web.projekt.entities.tour.TourRepository;

@Configuration
public class TourServiceConfiguration {
    @Autowired
    TourRepository repo;

    @Bean
    public TourService tourService(){
        TourService t = new TourServiceImpl(repo);
        return t;
    }
}
