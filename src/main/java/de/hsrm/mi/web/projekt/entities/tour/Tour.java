package de.hsrm.mi.web.projekt.entities.tour;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.ort.Ort;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Tour {
    @Id @GeneratedValue
    private long id;
    @Version
    private long version;
    @Future @NotNull
    private LocalDateTime abfahrDateTime;
    @Min(0)
    private int preis;
    @Min(1)
    private int plaetze;
    @Size(min=0, max=400)
    private String info;
    @NotNull @ManyToOne(cascade = CascadeType.REMOVE)
    private Benutzer anbieter;
    @NotNull 
    private Ort startOrt;
    @NotNull 
    private Ort zielOrt;
    @ManyToMany
    private Set<Benutzer> mitfahrgaeste;

    public Tour(){
        mitfahrgaeste = new HashSet<>();
    }

    public Set<Benutzer> getMitfahrgaeste(){
        return mitfahrgaeste;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
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
    
}
