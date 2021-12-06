const socket = new SockJS("/ws");
const stompClient = Stomp.over(socket);

function connect() {
    stompClient.connect({room: ROOM}, onConnected, onError);
}

function onConnected() {
    console.log("connected");

    stompClient.subscribe(
        "/room/" + ROOM,
        onMessageReceived
    );
}

function sendHit(msg) {
    if (msg) {
        const message = {
            login: LOGIN,
            target: msg,
        };

        stompClient.send("/app/hit/" + ROOM, {}, JSON.stringify(message));
    }
}

function sendReady(isReady) {
    const message = {
        login: LOGIN,
        ready: isReady,
    };

    stompClient.send("/app/ready/" + ROOM, {}, JSON.stringify(message));
}

function sendStartGame() {
    stompClient.send("/app/start/" + ROOM, {});
}

function disconnect() {
    stompClient.disconnect();
}

function handleMove(response) {
    EMOJI.classList.remove();
    const field = response.target.username === LOGIN ? MY_FIELD : OPPONENT_FIELD;
    const cell = field.querySelector(`td[data-x="${response.target.x}"][data-y="${response.target.y}"]`);

    if (response.result !== "Miss") {
        if (field === MY_FIELD)
            cell.classList.add("hit_me");
        else cell.classList.add("hit");
    } else {
        cell.classList.add("miss");
        MESSAGE.innerText = "Miss.. Now is your turn, " + response.userToMove;
        EMOJI.className = "expressionless";
    }

    if (response.result === "Hit") {
        MESSAGE.innerText = "Hit! " + response.userToMove + ", continue to move";
        EMOJI.className = "laughing";
    }

    if (response.result === "Sink") {
        MESSAGE.innerText = "Sink a ship! " + response.userToMove + ", continue to move";
        EMOJI.className = "dizzy";
    }

    if (response.result === "Sink" || response.result === "Win") {
        if (isMyTurn)
            opponentShipsCount += 1;
        else myShipsCount += 1;

        showShipsCount();
    }

    if (response.result === "Win") {
        isMyTurn = false;
        OPPONENT_FIELD.classList.add("blocked");
        MESSAGE.innerText = response.userToMove + ", congrats! You won! Got 7.25 points";
        EMOJI.className = "sunglasses";
    } else {
        isMyTurn = response.userToMove === LOGIN;
        highlightUserToMove();
        blockOpponentField();
    }
}

function onMessageReceived(msg) {
    const response = JSON.parse(msg.body);

    // when move
    if (response.hasOwnProperty("target") && response.target !== null) {
        handleMove(response);
    }

    // when ready
    if (response.hasOwnProperty("ready")) {
        const caption = response.login === LOGIN ? MY_USERNAME : OPPONENT_USERNAME;
        caption.classList.add("ready");
        if (LOGIN === response.login)
            READY_BUTTON.disabled = true;

        if (MY_USERNAME.classList.contains("ready") && OPPONENT_USERNAME.classList.contains("ready") && START_BUTTON) {
            START_BUTTON.disabled = false;
        }
    }

    // when join
    if (response.hasOwnProperty("joined")) {
        if (response.login !== LOGIN) {
            OPPONENT_INFO.innerText = response.login;
            OPPONENT_USERNAME.innerText = response.login;
        }
    }

    // when game started
    if (response.hasOwnProperty("userToMove") && response.target === null) {
        initField();

        isMyTurn = response.userToMove === LOGIN;

        MY_USERNAME.classList.remove("ready");
        OPPONENT_USERNAME.classList.remove("ready");

        MESSAGE.innerText = response.userToMove + ", you start the game!";

        MY_SHIPS.classList.add("ships_count");
        OPPONENT_SHIPS.classList.add("ships_count");

        LINE.classList.remove("d-none");

        blockOpponentField();
        highlightUserToMove();
        hideButtons();
        showShipsCount();
    }

    // when opponent left
    if (response.hasOwnProperty("disconnected")) {
        ALERT.innerText = response.login + " has left the battle. Please press 'Exit game' and join another one " +
            "or start your own";
        ALERT.classList.remove("d-none");

        OPPONENT_FIELD.classList.add("blocked");

        hideButtons();

        MESSAGE.innerText = "It's too sad but you can't continue the game...";
        EMOJI.className = "frown";

        if (isMyTurn)
            MY_USERNAME.classList.remove("highlight");
        else
            OPPONENT_USERNAME.classList.remove("highlight");
    }
}

function onError(error) {
    console.log(error);
}