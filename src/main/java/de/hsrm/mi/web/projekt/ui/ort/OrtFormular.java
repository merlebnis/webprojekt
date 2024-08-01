package de.hsrm.mi.web.projekt.ui.ort;

import de.hsrm.mi.web.projekt.entities.ort.Ort;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrtFormular {
    @NotNull @NotBlank
    private String name;
    private double geobreite;
    private double geolaenge;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGeobreite() {
        return geobreite;
    }

    public void setGeobreite(double geobreite) {
        this.geobreite = geobreite;
    }

    public double getGeolaenge() {
        return geolaenge;
    }

    public void setGeolaenge(double geolaenge) {
        this.geolaenge = geolaenge;
    }

    public void fromOrt(Ort ort) {
        this.name = ort.getName();
        this.geobreite = ort.getGeobreite();
        this.geolaenge = ort.getGeolaenge();
    }

    public void toOrt(Ort ort) {
        ort.setName(name);
        ort.setGeobreite(geobreite);
        ort.setGeolaenge(geolaenge);
    }

    @Override
    public String toString(){
        return name + " breite:" + geobreite + " laenge: " + geolaenge;
    }
}
