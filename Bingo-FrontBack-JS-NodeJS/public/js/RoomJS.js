import socket  from './socket-custom.js'

/**
 * Crea las funciones necesarias para conexión con el aplicativo en localhost:8080 (la creación, actualización de dominios en Java)
 * Establece las conexiones y comunicaciones entre sockets
 * Y dinamiza el front.
 * @version 1.0.000 2023-04-08
 * @author Wendy Arcila
 */


let gameUser = [];
let myBoard = [];
let allNumbers = [];
let gamerId;
let winnerFound = false;

/**
 * Establece una conexión con el servidor a través del socket.
 * Una vez conectado, ejecuta una serie de funciones:
 * Llama a la función main.
 * Llama a la función socket.ready.
 * Llama a la función socket.gamers.
 * Llama a la función socket.timerOff, que desactiva el temporizador para iniciar ronda de juego.
 * Una vez apagado el temporizador, llama a la función socket.ballot para comenzar el proceso que muestra la balota.
 * Finalmente, llama a la función winner para determinar el ganador de la ronda de juego.
 * @throws {Error} Si hay un error al conectarse al servidor o al ejecutar cualquiera de las funciones.
 */
socket.connect()
    .then(()=>{
        main();
        socket.ready();
        socket.gamers();
        socket.timerOff()
            .then(()=>{
                socket.ballot();
                winner();
            }).catch(err => console.log(err));
    }).catch(err => console.log(err));


/**
 * Inicia el juego.
 * Obtiene el nombre de usuario del elemento con ID "info" y llama a la función game del objeto socket.
 * A continuación, llama a la función initGame para inicializar el juego.
 */
function main () {
    let user = $('#info').val();
    socket.game()
    initGame(user)
}

/**
 * Inicializa el juego.
 * Obtiene el ID del jugador correspondiente al nombre de usuario recibido como parámetro.
 * Si el jugador ya existe en SQL, actualiza su estado a 7. Si no existe, crea un nuevo registro para el jugador.
 * @param {string} user - Nombre de usuario del jugador.
 */
function initGame(user) {
    getGamerByUser(user)
        .then(id => {
            if (id) {
                console.log("El usuario ya existe en SQL, se actualiza el estado");
                updateGamerByUser(id, 7);
                gamerId = id;
            } else {
                console.log("Se crea un nuevo Gamer en SQl");
                createGamer(user);
            }
        })
        .catch(err => console.log(err));

}

/**
 * Muestra un nuevo tablero.
 * Obtiene el ID del jugador correspondiente al nombre de usuario recibido y llama a la función
 * createBoard para crear un nuevo tablero para el jugador.
 */
function showNewBoard(){
    let user = $('#info').val();
    getGamerByUser(user)
        .then(id => {
            createBoard(id);
        })
        .catch(err => console.log(err));
}

/**
 * Esta función se ejecuta al cargar la página y agrega un controlador de eventos al botón "Obtener".
 * Oculta el contenido de la tabla y el botón "Obtener" al cargar la página.
 * Cuando se hace clic en el botón "Obtener", muestra el contenido de la tabla y llama a la función "showNewBoard"
 * para generar una nueva tabla de juego. También llama a la función "updateGamerInGame" para actualizar el estado del jugador en el juego.
 */
$(document).ready(function() {
    // Ocultar el contenido de la tabla cuando se carga la página
    $(".table").hide();
    $(".game").hide();
    fillBingoBoard()
    // Agregar un controlador de eventos para el botón "Obtener"
    $("#newBoardBtn").click(function() {
        // Mostrar el contenido de la tabla y ocultar el botón
        $(".table").show();
        $(this).hide();

        // Llamar a la función "showNewBoard" para generar una nueva tabla de juego
        showNewBoard();
        let idGame= $("#game").text();
        console.log("dentro de board idGame: "+ idGame+ ", idGamer: "+ gamerId)
        updateGamerInGame(gamerId,idGame)
    });
});

/**
 * Actualiza el estado de un gamer en la base de datos por su id
 * @param {number} id - El ID del gamer a actualizar
 * @param {number} status - El nuevo estado del gamer
 */
function updateGamerByUser(id, status){
    let url = 'http://localhost:8080/gamer/update/status/' + id;
    console.log(url);
    fetch(url, {
        method:'PATCH',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            statusGamer: {idStatus: status}
        })
    })
        .then(data => data.json())
        .catch(err => {
            console.log(err)
        });

}

/**
 * Actualiza la tabla de juego de un gamer en la base de datos por su id
 * @param {number} id - El ID del gamer a actualizar
 * @param {number} board - La nueva tabla de juego del gamer
 */
function updateGamerBoard(id, board){
    let url = 'http://localhost:8080/gamer/update/board/' + id;
    console.log(url);
    fetch(url, {
        method:'PATCH',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            board: {idBoard: board}
        })
    })
        .then(data => data.json())
        .catch(err => {
            console.log(err)
        });

}

/**
 * Crea un objeto Gamer en el servidor y asigna un ID de gamer a la variable global gamerId.
 * @param {string} user - El nombre de usuario del Gamer.
 */
function createGamer (user){
    fetch('http://localhost:8080/gamer/save', {
        method: 'POST',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            user: user
        })
    })
        .then(response => response.json())
        .then(json => {
            gamerId = json.idGamer;
        })
        .catch(error => console.error(error))
}

/**
 * Crea un objeto Board en el servidor y actualiza el objeto Gamer relacionado con el ID de Gamer especificado.
 * @param {number} ID - El ID de Gamer al que se le asignará la nueva tabla.
 */
function createBoard(ID){
        fetch('http://localhost:8080/board/save', {
            method:'POST',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                gamerId:ID
            })
        })
            .then(response => {
                return response.json()
            })
            .then(json => {
                updateGamerBoard(ID, json.idBoard);
                getBoard(json.idBoard);
                console.log("Se crea la tabla, se relaciona con gamer y se muestra");
                console.log(json);
            })
            .catch(err => console.log(err));
}

/**
 * Obtiene los datos de una tabla de juegos desde el servidor y la muestra en la interfaz gráfica.
 * @param {number} id - El ID de la tabla de juegos a obtener.
 * @returns {void}
 */
function getBoard(id){
    const url = 'http://localhost:8080/board/get/'+id
    fetch(url, {
        method:'GET',
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(response => response.json())
        .then(array => {
            myBoard = array;
            paintBoard(array);
            paintActiveBoard(array);
        })
        .catch(err => console.log(err));

}

/**
 * Función que renderiza un tablero de bingo en la página HTML
 * @param items  Un objeto que contiene los números del tablero de bingo
 */
function paintBoard(items){

    const tableBody = document.querySelector("#contentTable");
        //Primera fila
        const row1 = document.createElement("tr");

        let columnB = document.createElement("td");
        columnB.textContent = items.numberB1;
        row1.appendChild(columnB);

        let columnI = document.createElement("td");
        columnI.textContent = items.numberI1;
        row1.appendChild(columnI);

        let columnN = document.createElement("td");
        columnN.textContent = items.numberN1;
        row1.appendChild(columnN);

        let columnG = document.createElement("td");
        columnG.textContent = items.numberG1;
        row1.appendChild(columnG);

        let columnO = document.createElement("td");
        columnO.textContent = items.numberO1;
        row1.appendChild(columnO);

        tableBody.appendChild(row1);

        //Segunda fila
        const row2 = document.createElement("tr");

        let columnB2 = document.createElement("td");
        columnB2.textContent = items.numberB2;
        row2.appendChild(columnB2);

        let columnI2 = document.createElement("td");
        columnI2.textContent = items.numberI2;
        row2.appendChild(columnI2);

        let columnN2 = document.createElement("td");
        columnN2.textContent = items.numberN2;
        row2.appendChild(columnN2);

        let columnG2 = document.createElement("td");
        columnG2.textContent = items.numberG2;
        row2.appendChild(columnG2);

        let columnO2 = document.createElement("td");
        columnO2.textContent = items.numberO2;
        row2.appendChild(columnO2);

        tableBody.appendChild(row2);

        //Tercera fila
        const row3 = document.createElement("tr");

        let columnB3 = document.createElement("td");
        columnB3.textContent = items.numberB3;
        row3.appendChild(columnB3);

        let columnI3 = document.createElement("td");
        columnI3.textContent = items.numberI3;
        row3.appendChild(columnI3);

        let columnN3 = document.createElement("td");
        columnN3.textContent = items.numberN3;
        row3.appendChild(columnN3);

        let columnG3 = document.createElement("td");
        columnG3.textContent = items.numberG3;
        row3.appendChild(columnG3);

        let columnO3 = document.createElement("td");
        columnO3.textContent = items.numberO3;
        row3.appendChild(columnO3);

        tableBody.appendChild(row3);

        //Cuarta fila
        const row4 = document.createElement("tr");

        let columnB4 = document.createElement("td");
        columnB4.textContent = items.numberB4;
        row4.appendChild(columnB4);

        let columnI4 = document.createElement("td");
        columnI4.textContent = items.numberI4;
        row4.appendChild(columnI4);

        let columnN4 = document.createElement("td");
        columnN4.textContent = items.numberN4;
        row4.appendChild(columnN4);

        let columnG4 = document.createElement("td");
        columnG4.textContent = items.numberG4;
        row4.appendChild(columnG4);

        let columnO4 = document.createElement("td");
        columnO4.textContent = items.numberO4;
        row4.appendChild(columnO4);

        tableBody.appendChild(row4);

        //Quinta fila

        const row5 = document.createElement("tr");

        let columnB5 = document.createElement("td");
        columnB5.textContent = items.numberB5;
        row5.appendChild(columnB5);

        let columnI5 = document.createElement("td");
        columnI5.textContent = items.numberI5;
        row5.appendChild(columnI5);

        let columnN5 = document.createElement("td");
        columnN5.textContent = items.numberN5;
        row5.appendChild(columnN5);

        let columnG5 = document.createElement("td");
        columnG5.textContent = items.numberG5;
        row5.appendChild(columnG5);

        let columnO5 = document.createElement("td");
        columnO5.textContent = items.numberO5;
        row5.appendChild(columnO5);

        tableBody.appendChild(row5);

}

/**
 * Recupera del servidor el ID de jugador asociado al usuario dado.
 * @param {string} user - El nombre de usuario a buscar.
 * @returns {Promise<number|null>} Una promesa que devuelve el ID de jugador asociado con el usuario dado, o null si el usuario no existe.
 */
function getGamerByUser(user){
    let url= 'http://localhost:8080/gamer/get/user/'+ user
    return fetch(url, {
        method: 'GET',
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(response => response.json())
        .then(json => {
            console.log("este es el id del user: " + json.idGamer)
            if (json) {
                return json.idGamer;
            } else {
                return null;
            }
        })
        .catch(error => console.error(error))
}

/**
 * Actualiza el jugador con el ID dado para asociarlo con el juego especificado en el servidor.
 * @param {number} idGamer - El ID del jugador a actualizar.
 * @param {number} gameId - El ID del juego a asociar con el jugador.
 */
function updateGamerInGame(idGamer, gameId) {
    let url = 'http://localhost:8080/gamer/update/game/' + idGamer;
    fetch(url, {
        method: 'PATCH',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            game: {idGame: gameId}
        })
    })
        .then(data => data.json())
        .then(json => {
            console.log("Se relaciona el jugador con el juego")
            console.log(json)
        })
        .catch(err => {
            console.log(err)
        });
}

/**
 * Renderiza el tablero de bingo con números continuos en su respectiva columna
 */
function fillBingoBoard(){

    const tableBody = $("#contentBingo");
    //fila B
    const rowB = $("#B");

    for (let i = 1; i < 16; i++) {
        let column= $("<td></td>");
        column.text(i);
        column.addClass("num " + i);
        rowB.append(column);
    }
    tableBody.append(rowB);

    //fila I
    const rowI = $("#I");

    for (let i = 1; i < 16; i++) {
        let column= $("<td></td>");
        let l= i + 15;
        column.text(l);
        column.addClass("num "+ l);
        rowI.append(column);
    }
    tableBody.append(rowI);

    //fila N
    const rowN = $("#N");

    for (let i = 1; i < 16; i++) {
        let column= $("<td></td>");
        let l= i + 30;
        column.text(l);
        column.addClass("num "+ l);
        rowN.append(column);
    }
    tableBody.append(rowN);

    //fila G
    const rowG = $("#G");

    for (let i = 1; i < 16; i++) {
        let column= $("<td></td>");
        let l= i + 45;
        column.text(l);
        column.addClass("num "+ l);
        rowG.append(column);
    }
    tableBody.append(rowG);

    //fila O
    const rowO = $("#O");

    for (let i = 1; i < 16; i++) {
        let column= $("<td></td>");
        let l= i + 60;
        column.text(l);
        column.addClass("num "+ l);
        rowO.append(column);
    }
    tableBody.append(rowO);
}

/**
 * Función que renderiza un tablero de bingo en la página HTML, y agrega un evento de escucha click
 * que al ser seleccionado cambiará la clase
 * @param items  Un objeto que contiene los números del tablero de bingo
 */
function paintActiveBoard(items){
    const tableBody = $("#contentTable2");

    //Primera fila
    const row1 = $("<tr></tr>");

    let columnB = $("<td></td>");
    columnB.text(items.numberB1);
    columnB.click(()=>{
        columnB.addClass("select");
        gameUser.push(items.numberB1);
    });
    row1.append(columnB);

    let columnI = $("<td></td>");
    columnI.text(items.numberI1);
    columnI.click(()=>{
        columnI.addClass("select");
        gameUser.push(items.numberI1);
    });
    row1.append(columnI);

    let columnN = $("<td></td>");
    columnN.text(items.numberN1);
    columnN.click(()=>{
        columnN.addClass("select");
        gameUser.push(items.numberN1);
    });
    row1.append(columnN);

    let columnG = $("<td></td>");
    columnG.text(items.numberG1);
    columnG.click(()=>{
        columnG.addClass("select");
        gameUser.push(items.numberG1);
    });
    row1.append(columnG);

    let columnO = $("<td></td>");
    columnO.text(items.numberO1);
    columnO.click(()=>{
        columnO.addClass("select");
        gameUser.push(items.numberO1);
    });
    row1.append(columnO);

    tableBody.append(row1);

    //Segunda fila
    const row2 = $("<tr></tr>");

    let columnB2 = $("<td></td>");
    columnB2.text(items.numberB2);
    columnB2.click(()=>{
        columnB2.addClass("select");
        gameUser.push(items.numberB2);
    });
    row2.append(columnB2);

    let columnI2 = $("<td></td>");
    columnI2.text(items.numberI2);
    columnI2.click(()=>{
        columnI2.addClass("select");
        gameUser.push(items.numberI2);
    });
    row2.append(columnI2);

    let columnN2 = $("<td></td>");
    columnN2.text(items.numberN2);
    columnN2.click(()=>{
        columnN2.addClass("select");
        gameUser.push(items.numberN2);
    });
    row2.append(columnN2);

    let columnG2 = $("<td></td>");
    columnG2.text(items.numberG2);
    columnG2.click(()=>{
        columnG2.addClass("select");
        gameUser.push(items.numberG2);
    });
    row2.append(columnG2);

    let columnO2 = $("<td></td>");
    columnO2.text(items.numberO2);
    columnO2.click(()=>{
        columnO2.addClass("select");
        gameUser.push(items.numberO2);
    });
    row2.append(columnO2);

    tableBody.append(row2);

    //Tercera fila
    const row3 = $("<tr></tr>");

    let columnB3 = $("<td></td>");
    columnB3.text(items.numberB3);
    columnB3.click(()=>{
        columnB3.addClass("select");
        gameUser.push(items.numberB3);
    });
    row3.append(columnB3);

    let columnI3 = $("<td></td>");
    columnI3.text(items.numberI3);
    columnI3.click(()=>{
        columnI3.addClass("select");
        gameUser.push(items.numberI3);
    });
    row3.append(columnI3);

    let columnN3 = $("<td></td>");
    columnN3.text(items.numberN3);
    columnN3.click(()=>{
        columnN3.addClass("select");
        gameUser.push(items.numberN3);
    });
    row3.append(columnN3);

    let columnG3 = $("<td></td>");
    columnG3.text(items.numberG3);
    columnG3.click(()=>{
        columnG3.addClass("select");
        gameUser.push(items.numberG3);
    });
    row3.append(columnG3);

    let columnO3 = $("<td></td>");
    columnO3.text(items.numberO3);
    columnO3.click(()=>{
        columnO3.addClass("select");
        gameUser.push(items.numberO3);
    });
    row3.append(columnO3);

    tableBody.append(row3);

    //Cuarta fila
    const row4 = $("<tr></tr>");

    let columnB4 = $("<td></td>");
    columnB4.text(items.numberB4);
    columnB4.click(()=>{
        columnB4.addClass("select");
        gameUser.push(items.numberB4);
    });
    row4.append(columnB4);

    let columnI4 = $("<td></td>");
    columnI4.text(items.numberI4);
    columnI4.click(()=>{
        columnI4.addClass("select");
        gameUser.push(items.numberI4);
    });
    row4.append(columnI4);

    let columnN4 = $("<td></td>");
    columnN4.text(items.numberN4);
    columnN4.click(()=>{
        columnN4.addClass("select");
        gameUser.push(items.numberN4);
    });
    row4.append(columnN4);

    let columnG4 = $("<td></td>");
    columnG4.text(items.numberG4);
    columnG4.click(()=>{
        columnG4.addClass("select");
        gameUser.push(items.numberG4);
    });
    row4.append(columnG4);

    let columnO4 = $("<td></td>");
    columnO4.text(items.numberO4);
    columnO4.click(()=>{
        columnO4.addClass("select");
        gameUser.push(items.numberO4);
    });
    row4.append(columnO4);

    tableBody.append(row4);

    //Quinta fila
    const row5 = $("<tr></tr>");

    let columnB5 = $("<td></td>");
    columnB5.text(items.numberB5);
    columnB5.click(()=>{
        columnB5.addClass("select");
        gameUser.push(items.numberB5);
    });
    row5.append(columnB5);

    let columnI5 = $("<td></td>");
    columnI5.text(items.numberI5);
    columnI5.click(()=>{
        columnI5.addClass("select");
        gameUser.push(items.numberI5);
    });
    row5.append(columnI5);

    let columnN5 = $("<td></td>");
    columnN5.text(items.numberN5);
    columnN5.click(()=>{
        columnN5.addClass("select");
        gameUser.push(items.numberN5);
    });
    row5.append(columnN5);

    let columnG5 = $("<td></td>");
    columnG5.text(items.numberG5);
    columnG5.click(()=>{
        columnG5.addClass("select");
        gameUser.push(items.numberG5);
    });
    row5.append(columnG5);

    let columnO5 = $("<td></td>");
    columnO5.text(items.numberO5);
    columnO5.click(()=>{
        columnO5.addClass("select")
        gameUser.push(items.numberO5)
    });
    row5.append(columnO5);

    tableBody.append(row5);

}

/**
 * Maneja la lógica para cuando se pulsa el botón ¡Gané!
 * Evalúa que lista de números ganadores coincide con los números del usuario,
 * cuando encuentra una coincidencia la envía a la función whereIsFirst, que a su vez,
 * toma como parámetro el retorno de la función whereIs, que devuelve la ubicación de la
 * casilla donde se encuentra la primera ficha ganadora.
 * Cuando este se completa se evalúa si ya hay una fila, columna o diagonal ganadora, para romper el ciclo.
 * Se llama a la función iAmTheWinner
 * Sí se encuentra un ganador, se imprime en pantalla hay ganador y
 * se llama a la función gameOver.
 */
function winner(){
    $("#iWin").click(function() {
        socket.flagBallot(false);
        allNumbers = socket.numbersWinner();
        for (let i = 0; i < gameUser.length; i++) {
            if(allNumbers.includes(gameUser[i])){
                if(whereIsTheFirst(whereIs(gameUser[i]))){
                    if (winnerFound) {
                        break;
                    }
                }
            }else {
                console.log("Error "+ gameUser[i] + "NO está")
            }
        }
        socket.iAmTheWinner();
        if (winnerFound) {
            console.log("hay ganador");
            socket.gameOver();
        }
    })
}

/**
 * Determina en qué fila está un número dado en el tablero de bingo del usuario.
 * Si no está en una de las filas, llama a la función isOnColumn1, qué evalúa si está en una columna
 * @param {number} move - el número a comprobar
 * @returns {string} - la fila donde se encuentra el número (ej. "B1", "B2", "B3", etc.)
 */
function whereIs(move){
    if(move === myBoard.numberB1){
        console.log("es igual 1")
        return "B1";
    }else if (move === myBoard.numberB2){
        console.log("es igual 2 ")
        return "B2";
    }else if (move === myBoard.numberB3){
        console.log("es igual 3")
        return "B3";
    }else if (move === myBoard.numberB4){
        console.log("es igual 4")
        return "B4";
    }else if (move === myBoard.numberB5){
        console.log("es igual 5")
        return "B5";
    }else {
      return isOnColumn1(move);
    }
}

/**
 * Determina en qué columna está un número dado en el tablero de bingo del usuario.
 * @param {number} move - el número a comprobar
 * @returns {string} - la columna donde se encuentra el número (ej. "I1", "N1", "O1", etc.)
 */
function isOnColumn1(move){
    if(move === myBoard.numberI1){
        console.log("es igual I1")
        return "I1";
    }else if (move === myBoard.numberN1){
        console.log("es igual N1 ")
        return "N1";
    }else if (move === myBoard.numberG1){
        console.log("es igual G1")
        return "G1";
    }else if (move === myBoard.numberO1){
        console.log("es igual O1")
        return "O1";
    }else {
        console.log(move + "no es inicial")
        return "no";
    }
}

/**
 * Comprueba si el jugador ha ganado al rellenar la fila B1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, llama a la función diagonalB1().
 */
function allRowB1(){
    for (let i = 0; i < gameUser.length ; i++) {
        if(gameUser[i] === myBoard.numberI1) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN1) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG1) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO1) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON FILA B1")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con la fila B1");
    diagonalB1();
}

/**
 * Comprueba si el jugador ha ganado al rellenar la fila B2 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allRowB2() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberI2) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN2) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG2) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO2) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON FILA B2")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con la fila B2");
}

/**
 * Comprueba si el jugador ha ganado al rellenar la fila B3 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allRowB3() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberI3) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG3) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO3) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON FILA B3")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con la fila B3");
}

/**
 * Comprueba si el jugador ha ganado al rellenar la fila B4 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allRowB4() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberI4) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN4) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO4) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON FILA B4")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con la fila B4")
}

/**
 * Comprueba si el jugador ha ganado al rellenar la fila B1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, llama a la función diagonalB5().
 */
function allRowB5() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberI5) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN5) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG5) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON FILA B5")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con la fila B5");
    diagonalB5()
}

/**
 * Comprueba si el jugador ha ganado al rellenar la diagonal B1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function diagonalB1 (){
    for (let i = 0; i < gameUser.length ; i++) {
        if(gameUser[i] === myBoard.numberI2){
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON DIAGONAL B1")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con diagonal B1");
    allColumnB1();
}

/**
 * Comprueba si el jugador ha ganado al rellenar la diagonal B5 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function diagonalB5(){
    for (let i = 0; i < gameUser.length ; i++) {
        if(gameUser[i] === myBoard.numberI4){
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG2) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO1) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON DIAGONAL B5")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con diagonal B5")
}

/**
 * Comprueba si el jugador ha ganado al rellenar la columna B1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allColumnB1() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberB2) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberB3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberB4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberB5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON COLUMNA B1")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con Columna B1")
}

/**
 * Comprueba si el jugador ha ganado al rellenar la columna I1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allColumnI1() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberI2) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberI3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberI4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberI5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON COLUMNA I1")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con columna I1");
}

/**
 * Comprueba si el jugador ha ganado al rellenar la columna N1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allColumnN1() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberN2) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberN3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberN4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberN5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON COLUMNA N1")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con columna N1");
}

/**
 * Comprueba si el jugador ha ganado al rellenar la columna G1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allColumnG1() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberG2) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberG3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberG4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberG5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON COLUMNA G1")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con columna G1");
}

/**
 * Comprueba si el jugador ha ganado al rellenar la columna O1 de su tablero de bingo.
 * Si hay una combinación ganadora, establece la variable winnerFound a true y registra un mensaje en la consola.
 * Si no hay combinación ganadora, imprime en consola.
 */
function allColumnO1() {
    for (let i = 0; i < gameUser.length; i++) {
        if (gameUser[i] === myBoard.numberO2) {
            if (allNumbers.includes(gameUser[i])) {
                for (let j = 0; j < gameUser.length; j++) {
                    if (gameUser[j] === myBoard.numberO3) {
                        if (allNumbers.includes(gameUser[j])) {
                            for (let k = 0; k < gameUser.length; k++) {
                                if (gameUser[k] === myBoard.numberO4) {
                                    if (allNumbers.includes(gameUser[k])) {
                                        for (let l = 0; l < gameUser.length; l++) {
                                            if (gameUser[l] === myBoard.numberO5) {
                                                if (allNumbers.includes(gameUser[l])) {
                                                    winnerFound = true;
                                                    console.log("GANADOR CON COLUMNA 01")
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    console.log("No ganó con columna O1");
}

/**
 * Evalúa si la opción dada (una cadena que representa una celda en el tablero)
 * forma parte de una combinación ganadora y activa la comprobación correspondiente.
 * @param {string} option - Una cadena que representa una celda del tablero (por ejemplo, "B1").
 * @returns {boolean} - Verdadero si la opción forma parte de una combinación ganadora, falso en caso contrario.
 */
function whereIsTheFirst(option){
    switch (option) {
        case "B1":
            console.log("Evalúa B1");
            allRowB1();
            return true;
        case "B2":
            console.log("Evalúa B2");
            allRowB2();
            return true;
        case "B3":
            console.log("Evalúa B3");
            allRowB3();
            return true;
        case "B4":
            console.log("Evalúa B4");
            allRowB4();
            return true;
        case "B5":
            console.log("Evalúa B5");
            allRowB5();
            return true;
        case "I1":
            console.log("Evalúa I1");
            allColumnI1();
            return true;
        case "N1":
            console.log("Evalúa N1");
            allColumnN1();
            return true;
        case "G1":
            console.log("Evalúa G1");
            allColumnG1();
            return true;
        case "O1":
            console.log("Evalúa O1");
            allColumnO1();
            return true;
        default:
            console.log("No ganó")
            return false;
    }
}