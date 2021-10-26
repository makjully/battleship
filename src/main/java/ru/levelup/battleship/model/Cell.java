package ru.levelup.battleship.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cells")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cell {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "coordinate_x")
    @EqualsAndHashCode.Include
    private int coordinateX;

    @Column(name = "coordinate_y")
    @EqualsAndHashCode.Include
    private int coordinateY;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Ship ship;

    public Cell(int x, int y) {
        this.coordinateX = x;
        this.coordinateY = y;
    }
}