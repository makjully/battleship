const socket = new SockJS("http://localhost:8080/ws");
const stompClient = Stomp.over(socket);

function connect() {
    stompClient.connect({}, onConnected, onError);
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
            content: msg,
        };

        stompClient.send("/app/hit/" + ROOM, {}, JSON.stringify(message));
    }
}

function sendReady(isReady) {
    const message = {
        login: LOGIN,
        isReady
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
    const field = response.target.username === LOGIN ? MY_FIELD : OPPONENT_FIELD;
    const cell = field.querySelector(`td[data-x="${response.target.x}"][data-y="${response.target.y}"]`);

    if (response.result === "Hit" || response.result === "Win") {
        cell.classList.add("hit");
    }

    if (response.result === "Miss") {
        cell.classList.add("miss");
    }

    if (response.result === "Win") {
        isMyTurn = false;
        MESSAGE.innerText = response.userToMove + " won!";
        disconnect();
    } else {
        isMyTurn = response.userToMove === LOGIN;
    }
}

function onMessageReceived(msg) {
    const response = JSON.parse(msg.body);

    if (response.hasOwnProperty("target") && response.target !== null) {
        handleMove(response);
    }
    if (response.hasOwnProperty("isReady")) {
        const caption = response.login === LOGIN ? MY_USERNAME : OPPONENT_USERNAME;
        caption.classList.add("ready");

        if (MY_USERNAME.classList.contains("ready") && OPPONENT_USERNAME.classList.contains("ready") && START_BUTTON) {
            START_BUTTON.disabled = false;
        }
    }

    if (response.hasOwnProperty("userToMove") && response.target === null) {
        isMyTurn = response.userToMove === LOGIN;

        ARRANGE_BUTTON.style.visibility = "hidden";
        READY_BUTTON.style.visibility = "hidden";
        if (START_BUTTON) {
            START_BUTTON.style.visibility = "hidden";
        }
    }
}

function onError(error) {
    console.log(error);
}