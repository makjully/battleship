"use strict";
let isMyTurn = false;
let myShipsCount = 0;
let opponentShipsCount = 0;
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
const EMOJI = document.querySelector("#emoji");
const MY_SHIPS = document.querySelector("#my_ships");
const OPPONENT_SHIPS = document.querySelector("#opponent_ships");
const LINE = document.querySelector(".style5");

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
        .forEach(cell => {
            cell.classList.add("aim");
            cell.addEventListener("click", onCellClick)
        });
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
        event.target.classList.remove("aim");
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

function blockOpponentField() {
    if (!isMyTurn)
        OPPONENT_FIELD.classList.add("blocked");
    else
        OPPONENT_FIELD.classList.remove("blocked");
}

function hideButtons() {
    if (ARRANGE_BUTTON)
        ARRANGE_BUTTON.style.visibility = "hidden";

    if (READY_BUTTON)
        READY_BUTTON.style.visibility = "hidden";

    if (START_BUTTON) {
        START_BUTTON.style.visibility = "hidden";
    }
}

function showShipsCount() {
    MY_SHIPS.innerHTML = 'ships sunk : <span style="font-weight: 600">' + myShipsCount + '</span>';
    OPPONENT_SHIPS.innerHTML = 'ships sunk : <span style="font-weight: 600">' + opponentShipsCount + '</span>';
}

window.addEventListener("load", function () {
    loadBoard();
    connect();
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