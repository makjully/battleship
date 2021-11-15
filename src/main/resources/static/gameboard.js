"use strict";
let isMyTurn = false;
const MESSAGE = document.querySelector("#moveMessage");
const OPPONENT_FIELD = document.querySelector("#opponent_board");
const OPPONENT_USERNAME = OPPONENT_FIELD.querySelector("caption");
const MY_FIELD = document.querySelector("#my_board");
const MY_USERNAME = MY_FIELD.querySelector("caption");
const OPPONENT_INFO = document.querySelector("#opponent_info");
const READY_BUTTON = document.querySelector("#ready_button");
const ARRANGE_BUTTON = document.querySelector("#arrange_button");
const START_BUTTON = document.querySelector("#start_button");
const ALERT = document.querySelector(".alert");
const CLOSE_BUTTON = document.querySelector("#close_button");

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

    READY_BUTTON.disabled = false;
}

function loadBoard() {
    fetch("/api/loadBoard/" + LOGIN)
        .then(response => response.json())
        .then(entries => {
            entries.forEach(entry => {
                const cell = MY_FIELD.querySelector(
                    `td[data-x="${entry.coordinateX}"][data-y="${entry.coordinateY}"]`);
                cell.classList.add('ship')
            });
        }).catch(e => console.log(e));
}

function initField() {
    OPPONENT_FIELD.querySelectorAll("td")
        .forEach(cell => cell.addEventListener("click", onCellClick));
}

function onCellClick(event) {
    if (isMyTurn) {
        const target = {
            x: event.target.dataset.x,
            y: event.target.dataset.y,
            username: OPPONENT_USERNAME.innerText
        };
        console.log(target);
        sendHit(target);
        event.target.removeEventListener("click", onCellClick);
    }
}

function setReadyStatus() {
    sendReady(true);
    ARRANGE_BUTTON.disabled = true;
}

function highlightUserToMove() {
    MY_USERNAME.classList.remove("highlight");
    OPPONENT_USERNAME.classList.remove("highlight");
    const caption = isMyTurn ? MY_USERNAME : OPPONENT_USERNAME;
    caption.classList.add("highlight");
}

window.addEventListener("load", function () {
    loadBoard();
    initField();
    connect();
    ALERT.classList.add("d-none");
});

ARRANGE_BUTTON.addEventListener("click", function () {
    arrangeShips();
});

READY_BUTTON.addEventListener("click", function () {
    setReadyStatus();
});

if (START_BUTTON) {
    START_BUTTON.addEventListener("click", function () {
        sendStartGame();
    });
}

CLOSE_BUTTON.addEventListener("click", function () {
    disconnect();
})