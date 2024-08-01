package de.hsrm.mi.web.projekt.ui.tour;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.entities.tour.Tour;
import de.hsrm.mi.web.projekt.services.tour.TourService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@SessionAttributes(names = {"tourformular", "tour"})
@RequestMapping("/admin")
public class TourController {
    @Autowired
    private TourService tourService;
    private TourFormular tourformular;
    private Tour tour;

    private String tourbearbeitenhtml = "tour/tourbearbeiten";
    private String tourenhtml = "tour/tourliste";
    private String tourString = "admin/tour";
    Logger logger = LoggerFactory.getLogger(TourController.class);

    //Tourformular wird initialisiert
    @ModelAttribute("tourformular")
    public void initFormular(Model m){
        tourformular = new TourFormular();
        logger.debug("Formular erstellt");
        m.addAttribute("tourformular", tourformular); 
    }

    //Tour wird initialisiert 
    @ModelAttribute("tour")
    public void initTour(Model m){
        tour = new Tour();
        logger.debug("Tour erstellt");
        m.addAttribute("tour", tour);
    }

    //Get Anfrage auf /admin
    @GetMapping
    public String startRedirect() {
        return "forward:/" + tourString;
    }
    

    //Get Anfrage auf /tour
    @GetMapping("/tour")
    public String tourGet(Locale locale, Model m) {
        List<Tour> alleTouren = tourService.holeAlleTouren();
        var dateformat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        m.addAttribute("sprache", locale.getDisplayLanguage());
        m.addAttribute("dateformat", dateformat);
        m.addAttribute("alleTouren", alleTouren);
        return tourenhtml;
    }


    //Get Anfrage auf /tour/n
    @GetMapping("/tour/{tourid}")
    public String benutzerNGet(Locale locale, Model m, @ModelAttribute("tourformular") TourFormular tourformular, @ModelAttribute("tour") Tour tour, @PathVariable("tourid") Long tourid){    
        //fülle Auswahl
        List<Benutzer> alleAnbieter = new ArrayList<>();
        alleAnbieter.add(new Benutzer(0));
        alleAnbieter.addAll(tourService.holeAlleAnbieter());

        List<Ort> alleOrte = new ArrayList<>();
        alleOrte.add(new Ort(0));
        alleOrte.addAll(tourService.holeAlleOrte());

        m.addAttribute("sprache", locale.getDisplayLanguage());

        //überprüfe ob Tour mit ID in der Datenbank ist
        if(tourid > 0){
            Optional<Tour> opt = tourService.holeTourMitId(tourid);
            if(opt.isPresent()){
                tour = opt.get();
                m.addAttribute("tour", tour);
                tourformular.fromTour(tour);
                tourformular.setAlleAnbieter(alleAnbieter);
                tourformular.setAlleOrte(alleOrte);
                m.addAttribute("tourformular", tourformular);
                logger.info("Tourformular geladen : " + tourformular);
            }else{ //falls nicht redirect zu tour/0
                logger.warn("Tour zu id " + tourid + " nicht gefunden, redirekt zu /0");
                return "forward:/" + tourString + "/0";
            }
        }else{ 
            initFormular(m);
            this.tourformular.setAlleAnbieter(alleAnbieter);
            this.tourformular.setAlleOrte(alleOrte);
            m.addAttribute("tourformular", this.tourformular);
            initTour(m);
            m.addAttribute("tour", this.tour);
        }

        m.addAttribute("tourid", tourid);
        return tourbearbeitenhtml;
    }

    //Get Anfrage auf /tour/n/del
    @GetMapping("/tour/{tourid}/del")
    public String deleteGet(Locale locale, Model m, @PathVariable("tourid") Long tourid) {
        //lösche Tour
        tourService.loescheTourMitId(tourid);
        logger.info("Tour gelöscht mit id "+ tourid);

        //Zeige übrige Touren
        List<Tour> alleTouren = tourService.holeAlleTouren();
        m.addAttribute("alleTouren", alleTouren);
        return "forward:/" + tourString;
    }

    //POST auf das Tourformular
    @PostMapping("/tour/{tourid}")
    public String tourPost( @PathVariable("tourid") int tourid,
                            @Valid @ModelAttribute("tourformular") TourFormular tourformular,
                            BindingResult result,
                            @ModelAttribute("tour") Tour tour,
                            Model m) {
        
        //Fehler im Formular erkennen
        if(tourformular.getAnbieter() == null || tourformular.getAnbieter().getId() == 0){
            result.rejectValue("anbieter","tour.auswahl.fehlt");
        }else if(tourformular.getStartOrt() == null || tourformular.getStartOrt().getId() == 0){
            result.rejectValue("startOrt","tour.auswahl.fehlt");
        }else if(tourformular.getZielOrt() == null || tourformular.getZielOrt().getId() == 0){
            result.rejectValue("zielOrt","tour.auswahl.fehlt");
        }

        if(result.hasErrors()){
            logger.warn("Fehler im Formular : " + tourformular.toString());
            m.addAttribute("tourformular", tourformular);
            m.addAttribute("tourid", tourid);
            return tourbearbeitenhtml;
        }

        //Logging
        logger.info("Tourformular neue Werte: " + tourformular);

        //DB Aufrufe
        tourformular.toTour(tour);

        String info;
        try {
            tour = tourService.speichereTourangebot(tourformular.getAnbieter().getId(), tour, tourformular.getStartOrt().getId(), tourformular.getZielOrt().getId());
            info = "";
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            info = e.getMessage();

            m.addAttribute("info", info);
            m.addAttribute("tourformular", tourformular);
            m.addAttribute("tourid", tourid);
            return tourbearbeitenhtml;
        }

        Long longid = tour.getId();

        //Model Attribute updaten
        m.addAttribute("info", info);
        m.addAttribute("tour", tour);
        tourformular.fromTour(tour);
        m.addAttribute("tourformular", tourformular);
        m.addAttribute("tourid", longid);
        return "redirect:/" + tourString + "/" + longid;
    }

    @GetMapping("/makeTouren")
    public String getMethodName(Model m) {

        tourService.makeDummys();

        //Zeige alle Touren
        List<Tour> alleTouren = tourService.holeAlleTouren();
        m.addAttribute("alleTouren", alleTouren);
        return "forward:/" + tourString;
    }
}
