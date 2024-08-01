package de.hsrm.mi.web.projekt.ui.benutzer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.validators.GutesPasswort;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class BenutzerFormular {
    private int max;
    @Size(min = 3, max = 80) @NotBlank
    private String name;
    @Email @NotBlank
    private String email;
    @GutesPasswort(message = "{gutespasswort.fehler}")
    private String pwort;
    @NotNull @Past
    private LocalDate gtag;
    private Set<String> mag;
    private Set<String> magnicht;

    public BenutzerFormular(){
        mag = new HashSet<>();
        magnicht = new HashSet<>();
    }

    public void toBenutzer(Benutzer b){
        b.setName(name);
        b.setEmail(email);

        b.setGeburtstag(gtag);
        b.setMag(mag);
        b.setMagnicht(magnicht);
    }

    public void fromBenutzer(Benutzer b){
        this.name = b.getName();
        this.email = b.getEmail();

        this.gtag = b.getGeburtstag();
        this.mag = b.getMag();
        this.magnicht = b.getMagnicht();
    }

    public void magAdd(String eintrag){
        if(addCheck(mag, eintrag)){
            mag.add(eintrag);
        }
    }

    public void magnichtAdd(String eintrag){
        if(addCheck(magnicht, eintrag)){
            magnicht.add(eintrag);
        }
    }

    private boolean addCheck(Set<String> set, String eintrag){
        if(set.size() < max && !eintrag.equals("")){
            return true;
        }
        return false;
    }

    

    public void setMag(String mag) {
        magAdd(mag);
    }

    public Set<String> getMagnicht() {
        return magnicht;
    }

    public void setMagnicht(String magnicht) {
        magnichtAdd(magnicht);
    }

    public void setMax(int max){
        this.max = max;
    }

    public Set<String> getMag(){
        return mag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwort(String pwort) {
        this.pwort = pwort;
    }

    public void setGtag(LocalDate gtag) {
        this.gtag = gtag;
    }

    public String getPwort() {
        return pwort;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getGtag() {
        return gtag;
    }

    @Override
    public String toString(){
        String magstring = setToString(mag);
        String magnichtString = setToString(magnicht);
        String gtagString = (gtag == null) ? "" : gtag.toString();

        return String.format("name:%s email:%s pwort:%s gtag:%s mag:%smag nicht:%s", name, email, pwort, gtagString, magstring, magnichtString);
    }

    private String setToString(Set<String> set){
        String ergebnis = "";
        for (String string : set) {
            ergebnis += string + ", ";
        }

        return ergebnis;
    }

}
