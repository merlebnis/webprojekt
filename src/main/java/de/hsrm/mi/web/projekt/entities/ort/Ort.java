package de.hsrm.mi.web.projekt.entities.ort;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ort implements Serializable {
    @Id @GeneratedValue
    private long id;
    @Version
    private long version;
    @NotNull @NotBlank
    private String name;
    private double geobreite;
    private double geolaenge;

    public Ort(){}

    public Ort(long id){
        this();
        this.id = 0;
        this.name = "---"; 
    }

    public Ort(String name, double geobreite, double geolaenge) {
        this.name = name;
        this.geobreite = geobreite;
        this.geolaenge = geolaenge;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

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

    @Override
    public String toString(){
        return id + " name: "+ name + " breitengrad: " + geobreite + " laengengrad: " + geolaenge;
    }
}
