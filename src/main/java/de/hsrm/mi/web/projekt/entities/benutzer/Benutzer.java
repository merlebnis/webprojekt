package de.hsrm.mi.web.projekt.entities.benutzer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import de.hsrm.mi.web.projekt.entities.tour.Tour;
import de.hsrm.mi.web.projekt.validators.GutesPasswort;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
public class Benutzer {
    @Id @GeneratedValue
    private long id;
    @Version
    private long version;
    @Size(min = 3, max = 80) @NotBlank
    private String name;
    @Email @NotNull
    private String email;
    @GutesPasswort(message = "{gutespasswort.fehler}") @NotNull
    private String passwort;
    @NotNull @Past
    private LocalDate geburtstag;
    @NotNull @ElementCollection
    private Set<String> mag;
    @NotNull @ElementCollection
    private Set<String> magnicht;
    @NotNull @OneToMany(cascade = CascadeType.PERSIST ,orphanRemoval = true) @ElementCollection
    private Set<Tour> angeboteneTouren;
    @NotNull @ManyToMany
    private Set<Tour> gebuchteTouren;


    public Benutzer() {
        angeboteneTouren = new HashSet<>();
        mag = new HashSet<>();
        magnicht = new HashSet<>();
        gebuchteTouren = new HashSet<>();
    }

    public Benutzer(long id){
        this();
        this.id = 0;
        this.name = "---";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswort(String pwort) {
        if(pwort == null || pwort == ""){
            return;
        }
        this.passwort = pwort;
    }

    public void setGeburtstag(LocalDate gtag) {
        this.geburtstag = gtag;
    }

    public void setMag(Set<String> mag) {
        this.mag = mag;
    }

    public void setMagnicht(Set<String> magnicht) {
        this.magnicht = magnicht;
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


    public String getEmail() {
        return email;
    }


    public String getPasswort() {
        return passwort;
    }


    public LocalDate getGeburtstag() {
        return geburtstag;
    }

    public Set<String> getMag() {
        return mag;
    }


    public Set<String> getMagnicht() {
        return magnicht;
    }

    public Set<Tour> getAngeboteneTouren() {
        return angeboteneTouren;
    }

    public void setAngeboteneTouren(Set<Tour> angeboteneTouren) {
        this.angeboteneTouren = angeboteneTouren;
    }


    @Override
    public String toString(){
        String magstring = setToString(mag);
        String magnichtString = setToString(magnicht);
        String gtagString = (geburtstag == null) ? "" : geburtstag.toString();

        return String.format("name:%s email:%s pwort:%s gtag:%s mag:%smag nicht:%s", name, email, passwort, gtagString, magstring, magnichtString);
    }

    private String setToString(Set<String> set){
        String ergebnis = "";
        for (String string : set) {
            ergebnis += string + ", ";
        }

        return ergebnis;
    }


}
