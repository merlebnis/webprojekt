package de.hsrm.mi.web.projekt.ui.ort;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.services.ort.OrtService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@SessionAttributes(names ={"ortformular", "ort"})
@RequestMapping("/admin/ort")
public class OrtController {
    private String ortehtml = "ort/ortliste";
    private String ortbearbeitenhtml = "ort/ortbearbeiten";
    private String ortString = "admin/ort";
    @Autowired
    private OrtService ortService;
    Logger logger = LoggerFactory.getLogger(OrtController.class);

    //Ortformular wird initialisiert
    @ModelAttribute("ortformular")
    public void initFormular(Model m){
        OrtFormular ortformular = new OrtFormular();
        logger.debug("Formular erstellt");
        m.addAttribute("ortformular", ortformular); 
    }

    //Ort wird initialisiert
    @ModelAttribute("ort")
    public void initOrt(Model m){
        Ort ort = new Ort();
        logger.debug("Ort erstellt");
        m.addAttribute("ort", ort);
    }

    //Get Anfrage auf /ort
    @GetMapping
    public String orteGet(Model m) {
        List<Ort> alleOrte = ortService.holeAlleOrte();

        m.addAttribute("alleOrte", alleOrte);
        return ortehtml;
    }

    //Get Anfrage auf /ort/n
    @GetMapping("/{ortid}")
    public String ortNGet(Locale locale, Model m, @ModelAttribute("ortformular") OrtFormular ortformular, @PathVariable("ortid") Long ortid){    
        if(ortid > 0){
            Optional<Ort> opt = ortService.holeOrtMitId(ortid);
            if(opt.isPresent()){
                Ort ort = opt.get();
                m.addAttribute("ort", ort);
                ortformular.fromOrt(ort);
                m.addAttribute("ortformular", ortformular);
                logger.info("Ortformular geladen : " + ortformular);
            }else{
                logger.warn("Ort zu id " + ortid + " nicht gefunden, redirekt zu /0");
                m.addAttribute("sprache", locale.getDisplayLanguage());
                return "forward:/" + ortString + "/0";
            }
        }else{
            initFormular(m);
            initOrt(m);
        }
        m.addAttribute("sprache", locale.getDisplayLanguage());
        m.addAttribute("ortid", ortid);
        return ortbearbeitenhtml;
    }

    //Get Anfrage auf /ort/n/del
    @GetMapping("/{ortid}/del")
    public String deleteGet(Locale locale, Model m, @PathVariable("ortid") Long ortid) {
        //lösche Ort
        ortService.loescheOrtMitId(ortid);
        logger.info("Ort gelöscht mit id "+ ortid);

        //Zeige übrige Orte
        List<Ort> alleOrte = ortService.holeAlleOrte();
        m.addAttribute("alleOrte", alleOrte);
        return "forward:/" + ortString;
    }

    // Post Mapping auf /ort/id
    @PostMapping("/{ortid}")
    public String benutzerPost( @PathVariable("ortid") int ortid, 
                                @Valid @ModelAttribute("ortformular") OrtFormular ortformular,
                                BindingResult result,
                                @ModelAttribute("ort") Ort ort,
                                Model m) {
        

        if(result.hasErrors()){
            logger.warn("Fehler im Formular : " + ortformular.toString());
            m.addAttribute("ortformular", ortformular);
            m.addAttribute("ortid", ortid);
            return ortbearbeitenhtml;
        }

        if(ortformular.getGeobreite() == 0.0d && ortformular.getGeolaenge() == 0.0d){
            List<Ort> vorschlaege = ortService.findeOrtsvorschlaegeFuerAdresse(ortformular.getName());
            if(vorschlaege == null || vorschlaege .size() == 0){
                m.addAttribute("info", "Kein Ort gefunden");
                m.addAttribute("ortid", ortid);
                return ortbearbeitenhtml;
            }
            ortformular.setName(vorschlaege.get(0).getName());
            ortformular.setGeobreite(vorschlaege.get(0).getGeobreite());
            ortformular.setGeolaenge(vorschlaege.get(0).getGeolaenge());

            m.addAttribute("ortformular", ortformular);
            m.addAttribute("info", "Vorschlag bitte bestätigen oder ändern");
            m.addAttribute("ortid", ortid);
            return ortbearbeitenhtml;
        }

        //Logging
        logger.info("Ortformular : " + ortformular.toString());

        //DB Aufrufe
        ortformular.toOrt(ort);
       
        String info;
        try {
            ort = ortService.speichereOrt(ort);
            info = "";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            info = e.getMessage();

            m.addAttribute("info", info);
            m.addAttribute("ortformular", ortformular);
            m.addAttribute("ortid", ortid);
            return ortbearbeitenhtml;
        }

        Long longid = ort.getId();

        //Model Attribute updaten
        m.addAttribute("info", info);
        m.addAttribute("benutzer", ort);
        m.addAttribute("ortformular", ortformular);
        m.addAttribute("ortid", longid);
        return "redirect:/" + ortString + "/" + longid;
    }
    
}
