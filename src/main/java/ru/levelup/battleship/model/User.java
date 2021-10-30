package ru.levelup.battleship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    @EqualsAndHashCode.Include
    private String login;

    @Column(nullable = false, length = 20)
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