package de.hsrm.mi.web.projekt.services.tour;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.entities.tour.Tour;

@Service
public interface TourService {
    List<Tour> holeAlleTouren();
    List<Benutzer> holeAlleAnbieter();
    List<Ort> holeAlleOrte();
    Optional<Tour> holeTourMitId(long id);
    Tour speichereTour(Tour t);
    Tour speichereTourangebot(long anbieterid, Tour tour, long startortid, long zielortid);
    void loescheTourMitId(long id);
    void makeDummys();
}
