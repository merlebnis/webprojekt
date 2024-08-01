package de.hsrm.mi.web.projekt.services.geo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class GeoServiceImpl implements GeoService{

    @Override
    public List<GeoAdresse> findeAdressen(String ort) {
        if(ort == null){
            Logger logger = LoggerFactory.getLogger(GeoServiceImpl.class);
            logger.warn("GeoService hat Ort == null erhalten");
            return new ArrayList<GeoAdresse>();
        }
        WebClient webClient = WebClient.create("https://nominatim.openstreetmap.org");
        List<GeoAdresse> adressen = webClient.get()
                                    .uri(uriBuilder -> uriBuilder
                                    .path("/search")
                                    .queryParam("q", ort)
                                    .queryParam("countrycodes", "de")
                                    .queryParam("format", "json")
                                    .build())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(GeoAdresse.class)
                                .collectList()
                                .block();
        
        for (int i = 0; i < adressen.size(); i++) {
            if(!adressen.get(i).addresstype().equals("city") && !adressen.get(i).addresstype().equals("town") && !adressen.get(i).addresstype().equals("village")){
                adressen.remove(i);
            }
        }
        return adressen;
    }

}
