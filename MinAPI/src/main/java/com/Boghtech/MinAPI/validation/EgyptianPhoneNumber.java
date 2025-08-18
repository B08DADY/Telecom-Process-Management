package com.Boghtech.MinAPI.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * A custom validation annotation to check if a string is a valid
 * Egyptian mobile phone number.
 * A valid number must be 11 digits long and start with 010, 011, 012, or 015.
 */
@Documented
@Constraint(validatedBy = EgyptianPhoneNumberValidator.class) // Link to the logic class
@Target({ElementType.METHOD, ElementType.FIELD}) // This annotation can be used on fields
@Retention(RetentionPolicy.RUNTIME) // The validation will happen at runtime
public @interface EgyptianPhoneNumber {

    // Default error message if validation fails
    String message() default "Invalid phone number. Must be 11 digits and start with 010, 011, 012, or 015.";

    // Standard boilerplate for validation annotations
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}