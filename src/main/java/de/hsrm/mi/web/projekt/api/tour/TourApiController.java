package de.hsrm.mi.web.projekt.api.tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.hsrm.mi.web.projekt.entities.tour.Tour;
import de.hsrm.mi.web.projekt.services.tour.TourDTD;
import de.hsrm.mi.web.projekt.services.tour.TourService;

@RestController
@RequestMapping("/api/tour")
public class TourApiController {
    @Autowired
    TourService tourService;
    private Logger logger = LoggerFactory.getLogger(TourApiController.class);

    //GET Abfrage auf /api/ort gibt alle Orte der Datenbank als JSON zurück
    @GetMapping
    public List<TourDTD> getTouren(){
        List<Tour> alleTouren = tourService.holeAlleTouren();
        List<TourDTD> alleDTO = new ArrayList<>();

        for (Tour tour : alleTouren) {
            alleDTO.add(TourDTD.fromTour(tour));
        }

        logger.info("Api Abfrage alle Touren: " + alleTouren);
        return alleDTO;
    }
}
