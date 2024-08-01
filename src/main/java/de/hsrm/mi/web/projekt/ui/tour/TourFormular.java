package de.hsrm.mi.web.projekt.ui.tour;

import java.time.LocalDateTime;
import java.util.List;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.ort.Ort;
import de.hsrm.mi.web.projekt.entities.tour.Tour;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TourFormular {
    @Future @NotNull
    private LocalDateTime abfahrDateTime;
    @Min(0)
    private int preis;
    @Min(1)
    private int plaetze;
    @Size(min=0, max=400)
    private String info;
    @NotNull @ManyToOne
    private Benutzer anbieter;
    @NotNull 
    private Ort startOrt;
    @NotNull 
    private Ort zielOrt;
    private List<Benutzer> alleAnbieter;
    private List<Ort> alleOrte;


    public void fromTour(Tour tour){
        abfahrDateTime = tour.getAbfahrDateTime();
        preis = tour.getPreis();
        plaetze = tour.getPlaetze();
        info = tour.getInfo();
        anbieter = tour.getAnbieter();
        startOrt = tour.getStartOrt();
        zielOrt = tour.getZielOrt();
    }

    public void toTour(Tour tour){
        tour.setAbfahrDateTime(abfahrDateTime);
        tour.setPreis(preis);
        tour.setPlaetze(plaetze);
        tour.setInfo(info);
        tour.setStartOrt(startOrt);
        tour.setZielOrt(zielOrt);
    }


    public LocalDateTime getAbfahrDateTime() {
        return abfahrDateTime;
    }
    public void setAbfahrDateTime(LocalDateTime abfahrtDateTime) {
        this.abfahrDateTime = abfahrtDateTime;
    }
    public int getPreis() {
        return preis;
    }
    public void setPreis(int preis) {
        this.preis = preis;
    }
    public int getPlaetze() {
        return plaetze;
    }
    public void setPlaetze(int plaetze) {
        this.plaetze = plaetze;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public Benutzer getAnbieter() {
        return anbieter;
    }
    public void setAnbieter(Benutzer anbieter) {
        this.anbieter = anbieter;
    }
    public Ort getStartOrt() {
        return startOrt;
    }
    public void setStartOrt(Ort startOrt) {
        this.startOrt = startOrt;
    }
    public Ort getZielOrt() {
        return zielOrt;
    }
    public void setZielOrt(Ort zielOrt) {
        this.zielOrt = zielOrt;
    }

    public List<Benutzer> getAlleAnbieter() {
        return alleAnbieter;
    }

    public void setAlleAnbieter(List<Benutzer> alleAnbieter) {
        this.alleAnbieter = alleAnbieter;
    }

    public List<Ort> getAlleOrte() {
        return alleOrte;
    }

    public void setAlleOrte(List<Ort> alleOrte) {
        this.alleOrte = alleOrte;
    }

    @Override
    public String toString(){
        return String.format("abfahrt:%s anbieter:%s preis:%s plaetze:%s info:%s start:%s ziel%s", abfahrDateTime, anbieter, preis, plaetze, info, startOrt, zielOrt);
    }

    
}
