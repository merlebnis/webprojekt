package de.hsrm.mi.web.projekt.services.geo;

import java.util.List;

public interface GeoService {
    public record GeoAdresse(String name, String addresstype, String display_name, double lat, double lon) {};
    
    List<GeoAdresse> findeAdressen(String ort);
}
