package de.hsrm.mi.web.projekt.services.ort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.entities.ort.OrtRepository;
import de.hsrm.mi.web.projekt.services.geo.GeoService;
import de.hsrm.mi.web.projekt.services.geo.GeoService.GeoAdresse;

public class OrtServiceImpl implements OrtService{
    
    private Logger logger = LoggerFactory.getLogger(OrtServiceImpl.class);
    private final OrtRepository ortRepo;
    @Autowired
    private GeoService geoService;

    @Autowired 
    public OrtServiceImpl(OrtRepository ortRepo){
        this.ortRepo = ortRepo;
    }

    @Override @Transactional(readOnly=true)
    public List<Ort> holeAlleOrte() {
        logger.info("Alle Orte sortiert zurückgegeben.");
        return ortRepo.findAll(Sort.by("name")); 
    }

    @Override @Transactional
    public Optional<Ort> holeOrtMitId(long id) {
        Optional<Ort> opt = ortRepo.findById(id);

        if(opt.isPresent()) {
            logger.info("Ort mit id " + id + " gefunden");
        }else {
            logger.info("Ort mit id " + id + " nicht gefunden");
        }

        return opt;
    }

    @Override @Transactional
    public Ort speichereOrt(Ort o) {
        o = ortRepo.save(o);
        logger.info("Ort gespeichert " + o);
        return o;
    }

    @Override @Transactional
    public void loescheOrtMitId(long id) {
        Optional<Ort> o = holeOrtMitId(id);

        if(o.isPresent()){
            ortRepo.delete(o.get());
            logger.info("erfolgreich gelöscht " + o);
        }else{
            logger.warn("Ort mit id " + id + " nicht gefunden.");
        }
    }

    @Override
    public List<Ort> findeOrtsvorschlaegeFuerAdresse(String ort) {
        List<GeoAdresse> adressen = geoService.findeAdressen(ort);
        List<Ort> orte = new ArrayList<>();

        for (GeoAdresse adresse : adressen) {
            orte.add(new Ort(adresse.name(),adresse.lat(),adresse.lon()));
        }

        return orte;
    }
}
