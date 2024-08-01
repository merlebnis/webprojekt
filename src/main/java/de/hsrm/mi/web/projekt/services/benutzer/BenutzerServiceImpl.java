package de.hsrm.mi.web.projekt.services.benutzer;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;
@Service
public class BenutzerServiceImpl implements BenutzerService {
    private Logger logger = LoggerFactory.getLogger(BenutzerServiceImpl.class);
    private final BenutzerRepository benutzerRepo;

    @Autowired 
    public BenutzerServiceImpl(BenutzerRepository benutzerRepo){
        this.benutzerRepo = benutzerRepo;
    }

    @Override @Transactional(readOnly=true)
    public List<Benutzer> holeAlleBenutzer() {
        logger.info("Alle Benutzer sortiert zurückgegeben.");
        return benutzerRepo.findAll(Sort.by("name"));
        
    }

    @Override @Transactional
    public Optional<Benutzer> holeBenutzerMitId(long id) {
        Optional<Benutzer> opt = benutzerRepo.findById(id);

        if(opt.isPresent()) {
            logger.info(id + " gefunden");
        }else {
            logger.info(id + " nicht gefunden");
        }

        return opt;
    }

    @Override @Transactional
    public Benutzer speichereBenutzer(Benutzer b) {
        b = benutzerRepo.save(b);
        logger.info("Benutzer gespeichert " + b);
        return b;
    }

    @Override @Transactional
    public void loescheBenutzerMitId(long id) {
        Optional<Benutzer> b = holeBenutzerMitId(id);

        if(b.isPresent()){
            benutzerRepo.delete(b.get());
            logger.info("erfolgreich gelöscht " + b);
        }else{
            logger.warn("Nutzer mit id " + id + " nicht gefunden.");
        }
    }

    @Override @Transactional
    public Benutzer aktualisiereBenutzerAttribut(long id, String feldname, String wert) {
        Optional<Benutzer> opt = holeBenutzerMitId(id);

        if(!opt.isPresent()){
            logger.warn("Benutzer konnte nicht aktualisiert werden");
            return null;
        }

        Benutzer b = opt.get();
        
        if(feldname.equals("email")){
            b.setEmail(wert);
        }else if(feldname.equals("name")){
            b.setName(wert);
        }else{
            logger.info("Kein Feld zum Aktualisieren gefunden");
        }

        return speichereBenutzer(b);
    }

}
