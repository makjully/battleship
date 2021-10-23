package ru.levelup.battleship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    @Column(unique = true, nullable = false, length = 10)
    @Pattern(regexp = "[a-zA-Z0-9]{4,10}", message = "Login must contain 4-10 characters: letters and/or numbers")
    @EqualsAndHashCode.Include
    private String login;

    @Column(nullable = false, length = 20)
    @Size(min = 5, max = 20)
    @NotBlank(message = "Password can not be empty")
    private String password;

    @Column(nullable = false)
    private double rating;

    @Column
    private boolean playerFieldArranged;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        rating = 0.0;
    }
}