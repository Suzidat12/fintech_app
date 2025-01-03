package com.fintech.validation;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private final PhoneNumberUtil phoneNumberUtil;
    public PhoneNumberValidator(PhoneNumberUtil phoneNumberUtil) {
        this.phoneNumberUtil = phoneNumberUtil;
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext cvc) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        try {
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(value, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name()));
        } catch (NumberParseException e) {
            return false;
        }
    }
}