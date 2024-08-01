package de.hsrm.mi.web.projekt.api.ort;

import de.hsrm.mi.web.projekt.entities.ort.Ort;

public record OrtDTO (long id, String name, double geoBreite, double geoLaenge) {

    public static OrtDTO fromOrt(Ort o){
        return new OrtDTO(o.getId(), o.getName(), o.getGeobreite(), o.getGeolaenge());
    }

    public static Ort toOrt(OrtDTO dto){
        Ort o = new Ort();
        o.setName(dto.name);
        o.setGeobreite(dto.geoBreite);
        o.setGeolaenge(dto.geoLaenge);
        return o;
    }
}