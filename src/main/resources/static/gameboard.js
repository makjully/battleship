"use strict";
const MESSAGE = document.querySelector("#message");
const OPPONENT_FIELD = document.querySelector("#opponent_board");
const MY_FIELD = document.querySelector("#my_board");

function arrangeShips() {
    fetch("/api/arrange/" + LOGIN)
        .then(response => response.json())
        .then(entries => {
            MY_FIELD.querySelectorAll(".ship")
                .forEach(cell => cell.classList.remove('ship'));
            entries.forEach(entry => {
                const cell = MY_FIELD.querySelector(
                    `td[data-x="${entry.coordinateX}"][data-y="${entry.coordinateY}"]`);
                cell.classList.add('ship')
            });
        }).catch(e => console.log(e));
}

function loadBoard() {
    fetch("/api/loadBoard/" + LOGIN)
        .then(response => response.json())
        .then(entries => {
            MY_FIELD.querySelectorAll(".ship")
                .forEach(cell => cell.classList.remove('ship'));
            entries.forEach(entry => {
                const cell = MY_FIELD.querySelector(
                    `td[data-x="${entry.coordinateX}"][data-y="${entry.coordinateY}"]`);
                cell.classList.add('ship')
            });
        }).catch(e => console.log(e));
}

function initField() {
    OPPONENT_FIELD.querySelectorAll("td")
        .forEach(cell => cell.addEventListener("click", event => {
                const target = {
                    x: event.target.dataset.x,
                    y: event.target.dataset.y
                };
                console.log(target);
                event.target.style.backgroundColor = "red";
            }
        ));
}