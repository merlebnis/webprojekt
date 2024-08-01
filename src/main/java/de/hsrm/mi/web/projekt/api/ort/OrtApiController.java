package de.hsrm.mi.web.projekt.api.ort;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.services.ort.OrtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/ort")
public class OrtApiController {
    @Autowired
    OrtService ortService;
    private Logger logger = LoggerFactory.getLogger(OrtApiController.class);

    //GET Abfrage auf /api/ort gibt alle Orte der Datenbank als JSON zurück
    @GetMapping
    public List<OrtDTO> getOrte(){
        List<Ort> alleOrte = ortService.holeAlleOrte();
        List<OrtDTO> alleDTO = new ArrayList<>();

        for (Ort ort : alleOrte) {
            alleDTO.add(OrtDTO.fromOrt(ort));
        }

        logger.info("Api Abfrage alle Orte: " + alleOrte);
        return alleDTO;
    }

    //GET Abfrage auf /api/ort/{id} gibt Ort mit id als JSON zurück
    @GetMapping("/{id}")
    public OrtDTO getOrt(@PathVariable("id") int id) {
        Optional<Ort> optOrt = ortService.holeOrtMitId(id);

        if(!optOrt.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Ort o = optOrt.get();

        logger.info("Api Abfrage Ort " + o);
        return OrtDTO.fromOrt(o);
    }
    
}
