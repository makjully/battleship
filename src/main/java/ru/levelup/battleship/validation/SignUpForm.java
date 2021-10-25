package ru.levelup.battleship.validation;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@PasswordsMatch
public class SignUpForm {

    @Pattern(regexp = "[a-zA-Z0-9]{4,10}", message = "Login must contain 4-10 characters: letters and/or numbers")
    private String login;

    @NotBlank(message = "Password must not be empty!")
    private String password;

    private String repeatPassword;
}