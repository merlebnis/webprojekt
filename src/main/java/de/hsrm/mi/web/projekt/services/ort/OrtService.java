package de.hsrm.mi.web.projekt.services.ort;

import java.util.List;
import java.util.Optional;

import de.hsrm.mi.web.projekt.entities.ort.Ort;

public interface OrtService {
    List<Ort> holeAlleOrte();
    Optional<Ort> holeOrtMitId(long id);
    Ort speichereOrt(Ort b);
    void loescheOrtMitId(long id);
    List<Ort> findeOrtsvorschlaegeFuerAdresse(String ort);
}
