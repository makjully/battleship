package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.levelup.battleship.services.UserService;
import ru.levelup.battleship.validation.SignUpForm;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class SignUpController {

    private UserService service;
    private static final String SIGNUP = "signup";

    @GetMapping("/signup")
    public String signUp(@ModelAttribute("signupForm") SignUpForm signupForm) {
        return SIGNUP;
    }

    @PostMapping("/signup")
    public String doSignUp(@ModelAttribute("signupForm") @Valid SignUpForm signupForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return SIGNUP;

        try {
            service.createUser(signupForm.getLogin(), signupForm.getPassword());
        } catch (JpaSystemException e) {
            bindingResult.addError(new FieldError("signupForm", "login",
                    "User with this login already exists."));
            return SIGNUP;
        }

        return "redirect:/login";
    }
}