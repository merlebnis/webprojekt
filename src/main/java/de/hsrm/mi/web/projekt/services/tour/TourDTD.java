package de.hsrm.mi.web.projekt.services.tour;

import java.time.LocalDateTime;

import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.entities.tour.Tour;
import de.hsrm.mi.web.projekt.services.geo.GeoDistanz;

public record TourDTD(long id, 
                    LocalDateTime abfahrDateTime, 
                    int preis, 
                    int plaetze, 
                    int buchungen, 
                    String startOrtName, 
                    long startOrtId,
                    String zielOrtName, 
                    long zielOrtId, 
                    String anbieterName, 
                    long anbieterId, 
                    double distanz, 
                    String info) {

    public static TourDTD fromTour(Tour t){
        Ort startOrt = t.getStartOrt();
        Ort zielOrt = t.getZielOrt();
        double distanz = GeoDistanz.calculateDistance(startOrt.getGeobreite(), startOrt.getGeolaenge(), zielOrt.getGeobreite(), zielOrt.getGeolaenge());
        
        return new TourDTD(t.getId(),
            t.getAbfahrDateTime(), 
            t.getPreis(), 
            t.getPlaetze(), 
            t.getMitfahrgaeste().size(), 
            startOrt.getName(), 
            startOrt.getId(),
            zielOrt.getName(), 
            zielOrt.getId(), 
            t.getAnbieter().getName(), 
            t.getAnbieter().getId(), 
            distanz, 
            t.getInfo());
    }

}
