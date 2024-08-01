package de.hsrm.mi.web.projekt.services.ort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.hsrm.mi.web.projekt.entities.ort.OrtRepository;

@Configuration
public class OrtServiceConfiguration {
    @Autowired
    OrtRepository repo;

    @Bean
    public OrtService ortService(){
        OrtService o = new OrtServiceImpl(repo);
        return o;
    }
}
