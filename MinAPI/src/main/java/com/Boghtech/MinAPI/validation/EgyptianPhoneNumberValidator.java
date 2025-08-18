package com.Boghtech.MinAPI.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EgyptianPhoneNumberValidator implements ConstraintValidator<EgyptianPhoneNumber, String> {


    private static final String PHONE_NUMBER_REGEX = "^(010|011|012|015)\\d{8}$";

    @Override
    public boolean isValid(String phoneValue, ConstraintValidatorContext context) {

        if (phoneValue == null) {
            return true;
        }

        return phoneValue.matches(PHONE_NUMBER_REGEX);
    }
}