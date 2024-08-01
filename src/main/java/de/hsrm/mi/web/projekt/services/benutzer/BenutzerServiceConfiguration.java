package de.hsrm.mi.web.projekt.services.benutzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;

@Configuration
public class BenutzerServiceConfiguration {
    @Autowired
    BenutzerRepository repo;

    @Bean
    public BenutzerService benutzerService(){
        BenutzerService b = new BenutzerServiceImpl(repo);
        return b; 
    }

}
