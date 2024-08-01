package de.hsrm.mi.web.projekt.ui.benutzer;




import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.services.benutzer.BenutzerService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;





@Controller
@SessionAttributes(names ={"benutzerformular", "benutzer"})
@RequestMapping("/admin/benutzer")
public class BenutzerController {
    private static final int maxwunsch = 5;
    @Autowired
    private BenutzerService benutzerService;

    private String benutzerhtml = "benutzer/benutzerbearbeiten";
    private String benutzerlistehtml = "benutzer/benutzerliste";
    private String benutzerString = "admin/benutzer";
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);

    //Benutzerformular wird initialisiert
    @ModelAttribute("benutzerformular")
    public void initFormular(Model m){
        BenutzerFormular benutzerformular = new BenutzerFormular();
        benutzerformular.setMax(maxwunsch);
        logger.debug("Formular erstellt");
        m.addAttribute("benutzerformular", benutzerformular); 
    }

    //Benutzer wird initialisiert 
    @ModelAttribute("benutzer")
    public void initBenutzer(Model m){
        Benutzer benutzer = new Benutzer();
        logger.debug("Benutzer erstellt");
        m.addAttribute("benutzer", benutzer);
    }
    
    //Get Anfrage auf /benutzer
    @GetMapping
    public String benutzerGet(Locale locale, Model m) {
        List<Benutzer> alleNutzer = benutzerService.holeAlleBenutzer();
        var dateformat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        m.addAttribute("dateformat", dateformat);
        m.addAttribute("alleNutzer", alleNutzer);
        return benutzerlistehtml;
    }
    

    //Get Anfrage auf /benutzer/n
    @GetMapping("/{benutzerid}")
    public String benutzerNGet(Locale locale, Model m, @ModelAttribute("benutzerformular") BenutzerFormular benutzerformular, @ModelAttribute("benutzer") Benutzer benutzer, @PathVariable("benutzerid") Long benutzerid){    
        if(benutzerid > 0){
            Optional<Benutzer> opt = benutzerService.holeBenutzerMitId(benutzerid);
            if(opt.isPresent()){
                benutzer = opt.get();
                m.addAttribute("benutzer", benutzer);
                benutzerformular.fromBenutzer(benutzer);
                m.addAttribute("benutzerformular", benutzerformular);
                logger.info("Benutzerformular geladen : " + benutzerformular);
            }else{
                logger.warn("Benutzer zu id " + benutzerid + " nicht gefunden, redirekt zu /0");
                m.addAttribute("sprache", locale.getDisplayLanguage());
                m.addAttribute("maxwunsch", maxwunsch);
                return "forward:/" + benutzerString + "/0";
            }
        }else{
            initFormular(m);
            initBenutzer(m);
        }
        m.addAttribute("sprache", locale.getDisplayLanguage());
        m.addAttribute("benutzerid", benutzerid);
        m.addAttribute("maxwunsch", maxwunsch);
        return benutzerhtml;
    }
    
    //Get Anfrage auf /benutzer/n/del
    @GetMapping("/{benutzerid}/del")
    public String deleteGet(Locale locale, Model m, @PathVariable("benutzerid") Long benutzerid) {
        //lösche Benutzer
        benutzerService.loescheBenutzerMitId(benutzerid);
        logger.info("Benutzer gelöscht mit id "+ benutzerid);

        //Zeige übrige Benutzer
        List<Benutzer> alleNutzer = benutzerService.holeAlleBenutzer();
        m.addAttribute("alleNutzer", alleNutzer);
        return "forward:/" + benutzerString;
    }


    //Benutzer bearbeiten
    @PostMapping("/{benutzerid}")
    public String benutzerPost( @PathVariable("benutzerid") int benutzerid, 
                                @Valid @ModelAttribute("benutzerformular") BenutzerFormular benutzerformular,
                                BindingResult result,
                                @ModelAttribute("benutzer") Benutzer benutzer,
                                Model m) {
        
        if(benutzerformular.getPwort() == null || benutzerformular.getPwort().equals("")){
            if(benutzer == null || benutzer.getPasswort() == null){
                result.rejectValue("pwort", "benutzerformular.passwort.ungesetzt", "Passwort wurde noch nicht gesetzt");
            }
        }

        if(result.hasErrors()){
            logger.warn("Fehler im Formular : " + benutzerformular.toString());
            m.addAttribute("benutzerformular", benutzerformular);
            m.addAttribute("maxwunsch", maxwunsch);
            m.addAttribute("benutzerid", benutzerid);
            return benutzerhtml;
        }

        //Logging
        logger.info("Benutzerformular : " + benutzerformular.toString());

        //DB Aufrufe
        benutzerformular.toBenutzer(benutzer);
        //Passwort setzen (Sonderbehandlung)
        if(benutzerformular.getPwort() == null){
            String pwort = "17";
            Optional<Benutzer> opt = benutzerService.holeBenutzerMitId(benutzerid);
            if(opt.isPresent()){
                pwort = opt.get().getPasswort();
            }
            benutzerformular.setPwort(pwort);
        }
        benutzer.setPasswort(benutzerformular.getPwort());

        //Speichern
        String info;
        try {
            benutzer = benutzerService.speichereBenutzer(benutzer);
            info = "";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            info = e.getMessage();

            m.addAttribute("info", info);
            m.addAttribute("benutzerformular", benutzerformular);
            m.addAttribute("maxwunsch", maxwunsch);
            m.addAttribute("benutzerid", benutzerid);
            return benutzerhtml;
        }

        Long longid = benutzer.getId();

        //Model Attribute updaten
        m.addAttribute("info", info);
        m.addAttribute("benutzer", benutzer);
        benutzerformular.fromBenutzer(benutzer);
        m.addAttribute("benutzerformular", benutzerformular);
        m.addAttribute("benutzerid", longid);
        m.addAttribute("maxwunsch", maxwunsch);
        return "redirect:/" + benutzerString + "/" + longid;
    }
    
    //Get Anfrage auf /benutzer/n/hx/feld/feldname
    @GetMapping("/{benutzerid}/hx/feld/{feldname}")
    public String schnipselGet(Locale locale, Model m, @PathVariable("benutzerid") Long benutzerid, @PathVariable("feldname") String feldname) {
        Optional<Benutzer> opt = benutzerService.holeBenutzerMitId(benutzerid);
        if(!opt.isPresent()){
            logger.warn("Benutzer nicht gefunden");
            return "";
        }

        String wert = "";

        if(feldname.equals("email")){
            wert = opt.get().getEmail();
        }else if(feldname.equals("name")){
            wert = opt.get().getName();
        }else{
            logger.info("Kein Feld " + feldname + " gefunden");
            return "";
        }


        m.addAttribute("benutzerid", benutzerid);
        m.addAttribute("wert", wert);
        m.addAttribute("feldname", feldname);
        return String.format("%s-zeile :: feldbearbeiten",benutzerlistehtml);
    }

    //Put Anfrage auf /benutzer/n/hx/feld/feldname
    @PutMapping("/{benutzerid}/hx/feld/{feldname}")
    public String schnipselPut(Locale locale, Model m, @PathVariable("benutzerid") Long benutzerid, @PathVariable("feldname") String feldname, @RequestParam("wert") String wert) {
        Benutzer benutzer;
        String info;

        //Überprüfung, ob Benutzer in der Datenbank ist
        try {
            benutzer = benutzerService.aktualisiereBenutzerAttribut(benutzerid, feldname, wert);
            info = "";
        } catch (Exception e) {
            //Fehler beim Speichern (Feld falsch ausgefüllt)
            logger.error(e.getMessage(), e);
            info = e.getMessage();
            
            Optional<Benutzer> opt = benutzerService.holeBenutzerMitId(benutzerid);
            if(!opt.isPresent()){
                logger.warn("Großer Fehler beim Benutzer PUT");
                m.addAttribute("info", "Benutzer nicht gefunden");
                return "";
            }

            benutzer = opt.get();

            //Passendes Feld wieder mit alten Werten füllen
            if(feldname.equals("email")){
                wert = benutzer.getEmail();
            }else if(feldname.equals("name")){
                wert = benutzer.getName();
            }else{
                logger.info("Kein Feld " + feldname + " gefunden");
                return "";
            }


            m.addAttribute("info", info);
            m.addAttribute("benutzerid", benutzerid);
            m.addAttribute("wert", wert);
            m.addAttribute("feldname", feldname);
            return String.format("%s-zeile :: feldbearbeiten",benutzerlistehtml);
        }        

        //Werte erfolgreich gespeichert
        logger.info("erfolgreich getauscht : " + wert);
        m.addAttribute("info", info);
        m.addAttribute("benutzerid", benutzerid);
        m.addAttribute("wert", wert);
        m.addAttribute("feldname", feldname);
        return String.format("%s-zeile :: feldausgeben",benutzerlistehtml);
    }
}
