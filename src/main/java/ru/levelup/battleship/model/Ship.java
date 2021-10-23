package ru.levelup.battleship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ships")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ship {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User player;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.PERSIST)
    private List<Cell> location;

    public Ship (User player, List<Cell> location) {
        this.player = player;
        this.location = location;
        location.forEach(cell -> cell.setShip(this));
    }
}