package com.f139.shop.admin.common.validation;


import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

class MobileValidator implements ConstraintValidator<Mobile, String> {

    private static final Pattern PATTERN=Pattern.compile( "^1[345678]\\d{9}$");


    @Override
    public void initialize(Mobile constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasLength(value)) {
            return false;
        } else {
            return PATTERN.matcher(value).matches();
        }
    }

}
