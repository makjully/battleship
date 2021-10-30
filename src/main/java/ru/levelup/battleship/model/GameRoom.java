package ru.levelup.battleship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooms_id_seq")
    @SequenceGenerator(name = "rooms_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    private User inviter;

    @ManyToOne
    private User accepting;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public GameRoom(User inviter) {
        this.inviter = inviter;
        this.timestamp = LocalDateTime.now();
    }
}