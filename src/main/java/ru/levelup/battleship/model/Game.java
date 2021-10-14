package ru.levelup.battleship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "games")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(optional = false)
    private User player1;

    @ManyToOne(optional = false)
    private User player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_to_move_id")
    private User playerToMove;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.playerToMove = player1;
        this.isCompleted = false;
    }
}