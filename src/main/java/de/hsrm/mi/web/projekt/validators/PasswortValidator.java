package de.hsrm.mi.web.projekt.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswortValidator implements ConstraintValidator<GutesPasswort,String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value == ""){
            //Passwort darf leer sein
            return true;
        } else if(value.contains("17") || value.toUpperCase().contains("SIEBZEHN")){
            //Passwort muss 17 oder Siebzehn (Case egal) enthalten
            return true;
        }
        return false;
    }
    

}
