package ru.levelup.battleship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooms_id_seq")
    @SequenceGenerator(name = "rooms_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    private User inviter;

    @ManyToOne
    private User accepting;

    @OneToOne
    private Game game;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Room(User inviter) {
        this.inviter = inviter;
        this.timestamp = LocalDateTime.now();
    }
}