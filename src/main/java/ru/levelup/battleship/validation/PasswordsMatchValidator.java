package ru.levelup.battleship.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, SignUpForm> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(SignUpForm signupForm, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(signupForm.getPassword(), signupForm.getRepeatPassword());
    }
}