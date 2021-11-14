const socket = new SockJS("http://localhost:8080/ws");
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
    stompClient.disconnect({room: ROOM});
}

function handleMove(response) {
    const field = response.target.username === LOGIN ? MY_FIELD : OPPONENT_FIELD;
    const cell = field.querySelector(`td[data-x="${response.target.x}"][data-y="${response.target.y}"]`);

    if (response.result !== "Miss") {
        if (field === MY_FIELD)
            cell.classList.add("hit_me");
        else cell.classList.add("hit");
    } else {
        cell.classList.add("miss");
        MESSAGE.innerText = "Miss.. Now is your turn, " + response.userToMove;
    }

    if (response.result === "Hit") {
        MESSAGE.innerText = "Hit! " + response.userToMove + ", continue to move";
    }

    if (response.result === "Sink") {
        MESSAGE.innerText = "Sink a ship! " + response.userToMove + ", continue to move";
    }


    if (response.result === "Win") {
        isMyTurn = false;
        MESSAGE.innerText = response.userToMove + ", congrats! You won! Got 7.25 points";
        //disconnect();
    } else {
        isMyTurn = response.userToMove === LOGIN;
        highlightUserToMove();
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
        isMyTurn = response.userToMove === LOGIN;

        MY_USERNAME.classList.remove("ready");
        OPPONENT_USERNAME.classList.remove("ready");

        MESSAGE.innerText = response.userToMove + ", you start the game!";
        highlightUserToMove();

        ARRANGE_BUTTON.style.visibility = "hidden";
        READY_BUTTON.style.visibility = "hidden";
        if (START_BUTTON) {
            START_BUTTON.style.visibility = "hidden";
        }
    }

    // when opponent left
    if (response.hasOwnProperty("disconnected")) {
        EXIT_ALERT.innerText = response.login + " has left the battle. Please, press 'Exit game' and join another " +
            "or start your own";
        EXIT_ALERT.alert();
    }
}

function onError(error) {
    console.log(error);
}