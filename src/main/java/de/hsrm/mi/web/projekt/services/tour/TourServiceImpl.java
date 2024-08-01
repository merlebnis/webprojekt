package de.hsrm.mi.web.projekt.services.tour;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.entities.tour.Tour;
import de.hsrm.mi.web.projekt.entities.tour.TourRepository;
import de.hsrm.mi.web.projekt.messaging.AenderungsTyp;
import de.hsrm.mi.web.projekt.messaging.EventTyp;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtService;
import de.hsrm.mi.web.projekt.services.benutzer.BenutzerService;
import de.hsrm.mi.web.projekt.services.ort.OrtService;

public class TourServiceImpl implements TourService{
    private Logger logger = LoggerFactory.getLogger(TourServiceImpl.class);
    private final TourRepository tourRepo;
    @Autowired
    private BenutzerService benutzerService;
    @Autowired
    private OrtService ortService;
    @Autowired
    private FrontendNachrichtService nachrichtService;

    @Autowired
    public TourServiceImpl(TourRepository tourRepo){
        this.tourRepo = tourRepo;
    }

    @Override
    public List<Tour> holeAlleTouren() {
        logger.info("Alle Touren nach Datum ausgegeben");
        return tourRepo.findAll(Sort.by("abfahrDateTime"));
    }

    @Override
    public Optional<Tour> holeTourMitId(long id) {
        Optional<Tour> opt = tourRepo.findById(id);

        if(opt.isPresent()) {
            logger.info("Tour mit id " + id + " gefunden");
        }else {
            logger.warn("Tour mit id " + id + " nicht gefunden");
        }

        return opt;
    }

    @Override
    public Tour speichereTour(Tour t) {
        t = tourRepo.save(t);
        logger.info("Tour gespeichert " + t);

        nachrichtService.sendEvent(new FrontendNachrichtEvent(EventTyp.TOUR, t.getId(), AenderungsTyp.UPDATE));
        return t; 
    }

    @Override
    public Tour speichereTourangebot(long anbieterid, Tour tour, long startortid, long zielortid) {
        Optional<Benutzer> anbieter = benutzerService.holeBenutzerMitId(anbieterid);
        if(anbieter.isPresent()){
            tour.setAnbieter(anbieter.get());
        }else{
            logger.warn("Anbieter nicht gefunden zu id " + anbieterid);
            throw new RuntimeException("Anbieter zu id nicht gefunden.");
        }
       
        Optional<Ort> startOrt = ortService.holeOrtMitId(startortid);
        Optional<Ort> zielOrt = ortService.holeOrtMitId(zielortid);
        if(startOrt.isPresent() && zielOrt.isPresent()){
            tour.setStartOrt(startOrt.get());
            tour.setZielOrt(zielOrt.get());
        }else{
            if(!startOrt.isPresent()){
                logger.warn("Startort nicht gefunden zu id " + startortid);
            }
            if(!zielOrt.isPresent()){
                logger.warn("Zielort nicht gefunden zu id " + zielortid);
            }
            
            throw new RuntimeException("Ziel oder Start zu id nicht gefunden.");
        }


        Tour neueTour = tourRepo.save(tour);
        logger.info("Neue Tour " + neueTour);
        nachrichtService.sendEvent(new FrontendNachrichtEvent(EventTyp.TOUR, neueTour.getId(), AenderungsTyp.CREATE));
        return neueTour;
    }

    @Override
    public void loescheTourMitId(long id) {
        Optional<Tour> t = holeTourMitId(id);

        if(t.isPresent()){
            tourRepo.delete(t.get());
            logger.info("Tour erfolgreich gelöscht " + t.get());
            nachrichtService.sendEvent(new FrontendNachrichtEvent(EventTyp.TOUR, t.get().getId(), AenderungsTyp.DELETE));
        }else{
            logger.warn("Tour mit id " + id + " nicht gefunden.");
        }
    }

    @Override
    public List<Benutzer> holeAlleAnbieter() {
        return benutzerService.holeAlleBenutzer();
    }

    @Override
    public List<Ort> holeAlleOrte() {
        return ortService.holeAlleOrte();
    }


    @Override
    public void makeDummys() {
        Benutzer b1 = new Benutzer();
        b1.setName("Merle");
        b1.setEmail("merle.marie.erdmann@gmail.com");
        b1.setGeburtstag(LocalDate.of(2002, 02, 06));
        b1.setPasswort("17");

        benutzerService.speichereBenutzer(b1);

        Ort o1 = new Ort();
        o1.setName("Wiesbaden");
        o1.setGeolaenge(8.2416556);
        o1.setGeobreite(50.0820384);

        ortService.speichereOrt(o1);

        Ort o2 = new Ort();
        o2.setName("Oberdachstetten");
        o2.setGeobreite(49.4158584);
        o2.setGeolaenge(10.4234580);

        ortService.speichereOrt(o2);

        Tour t1 = new Tour();
        t1.setAbfahrDateTime(LocalDateTime.of(2024, 9, 1, 12, 30));
        t1.setAnbieter(b1);
        t1.setStartOrt(o1);
        t1.setZielOrt(o2);
        t1.setPlaetze(3);
        t1.setPreis(20);
        t1.setInfo("Eine Fahrt von Wiesbaden nach Oberdachstetten");

        speichereTour(t1);
    }

}
