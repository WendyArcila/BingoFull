/**
 * Establece las conexiones del socket con el servidor, tanto llamados como escuchas.
 * @version 1.0.000 2023-04-08
 * @author Wendy Arcila
 */

const socket = io();
let rotationCount = 1;
let allWinNumbers = [];
let gotWinner = false;
let finish = false;

/**
 * Se conecta al servidor usando la librería cliente socket.io.
 * @returns {Promise} Una promesa que resuelve cuando se establece la conexión y rechaza si hay un error al conectar con el servidor.
 */
const connect = () => {
    return new Promise((resolve, reject) => {
        socket.on('connect', () => {
            console.log('Conectado al servidor');
            resolve();
        });
        socket.on('connect_error', (error) => {
            console.log('Error al conectar al servidor:', error);
            reject(error);
        });
    });
}

/**
 * Función que escucha el evento 'timerOn' emitido por el socket.
 * Actualiza el contenido de texto de los elementos HTML con clase 'minutos' y 'segundos'
 * con los valores de data. Minutos y 'data.segundos' respectivamente.
 * @param {object} data - Objeto que contiene los valores de minutos y segundos para el temporizador.
 */

socket.on('timerOn', function (data) {
    $(".minutos").text(data.minutos);
    $(".segundos").text(data.segundos);
});

/**
 * Espera el evento 'timerOff' del socket, oculta .waiting de espera, y muestra .game del juego.
 * Si hay un error conectando al socket, rechaza con un error.
 * @returns {Promise<void>} Una Promise que resuelve cuando el temporizador se apaga.
 * @description
 */
const timerOff = () => {
    return new Promise((resolve, reject) => {
        socket.on('timerOff', () => {
            $(".waiting").hide();
            $(".game").show();
            resolve();
        });
        socket.on('connect_error', (error) => {
            console.log('Error con game:', error);
            reject(error);
        });
    })
}

/**
 * Función que emite el evento 'ready' hacia el servidor.
 */
const ready =  () => {
    socket.emit('ready')
}

/**
 * Función que escucha el evento 'newGame' emitido por el socket.
 * renderiza la información obtenida.
 */
const game = () => {
    socket.on('newGame', (data) => {
        $("#game").text(data.data);
        });
}

/**
 * Función que emite el evento 'gamer' hacia el servidor.
 * envía como data name que trae del HTML
 */
socket.emit('gamer',{name:$("#playerName").text()})

/**
 * Función que emite el evento 'flagBallot' hacia el servidor.
 * @param flagR un boolean que se recibe para ser enviado al servidor.
 */
const flagBallot = (flagR) => {
    socket.emit("flagBallot", {flag: flagR})
}

/**
 * Muestra una lista de jugadores en el HTML y la actualiza cuando se recibe información del servidor.
 */
const gamers = () =>{
    socket.on('gamers', (data)=>{
        let list = $("#playersList");
        cleanTable();
        data.forEach(gamer => {
            let li = $("<li></li>");
            li.text(gamer);
            list.append(li);
        })
        console.log("esta es la lista: "+list)
    })
}

/**
 * Limpia la tabla de jugadores
 */
function cleanTable(){
    const clean = $("#playersList");
    clean.empty();
}

/**
 * Escucha un evento 'ballot' del servidor y realiza una rotación basada en los datos recibidos
 */
const ballot = () => {
    socket.on('ballot', (data) =>{
        rotationCount = data.count;
        rotate(data.num, data.lett)
    })
}

/**
 * Rota la bola de bingo y actualiza el tablero de bingo con el nuevo número.
 * @param number  El nuevo número de bingo.
 * @param letter La letra que corresponde a la columna del nuevo número.
 */
function rotate (number, letter){
    let result;
    allWinNumbers.push(number);
    result = letter + number.toString();
    bingoChangeBoard(number)
    if (rotationCount % 2 === 0) {
        $(".ballot-all").css("transform", `rotateY(${180}deg)`);
        $(".Back").html(result);
    } else {
        $(".ballot-all").css("transform", `rotateY(${0}deg)`);
        $(".Front").html(result);
    }
}

/**
 * @returns {*[]} Todos los números ganadores que se encuentran en la variable allWinNumbers
 */
const numbersWinner = () =>{
    return allWinNumbers;
}

/**
 * Añade nueva clase al elemento que viene por parámetro.
 * @param {number} number - El número a marcar como ganador.
 */
function bingoChangeBoard(number){
    let element = "." + number.toString()
    const search = $(element)
    search.addClass("winBallot");
}

/**
 * Escucha un evento 'isWinner' del servidor y llama a la función gameFinish
 */
socket.on('isWinner', ()=>{
    console.log("entra a evaluar un ganador");
    gameFinish();
})

/**
 * Cambia el valor de finish a true.
 */
const gameOver = () =>{
    finish = true;
}

/**
 * Marca el final de la partida y envía un mensaje al servidor indicando si hay ganador o no.
 */
function gameFinish () {
    if (finish) {
        socket.emit('gameOver');
        console.log("hay Ganador, got winner")
        gotWinner = true;
    } else {
        console.log("no hay Ganador, no winner")
        socket.emit('noWinner')
    }
}

/**
 * Función que emite el evento 'iAmTheWinner' hacia el servidor.
 */
const iAmTheWinner = () => {
    socket.emit('amITheWinner')
}

/**
 * Maneja el evento "gotWinner" emitido por el servidor y muestra una alerta
 * de felicitación en caso de que el jugador haya ganado o una alerta de
 * consolación en caso contrario.
 */
socket.on('gotWinner', ()=>{
    console.log("Se evalúa el ganador" +gotWinner)
    if (gotWinner) {
        Swal.fire({
            title: 'GANASTE',
            text: '¡Felicitaciones eres la persona GANADORA!',
            width: 600,
            padding: '3em',
            color: 'maroon',
            backdrop: `
                    rgba(255, 0, 0, 0.4)
                    url("https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExOWFmMTNmYmVhYzEzMDc0MmIxMjY4MzQ2Nzk3ZjMxOTI3ODMzZmE0ZSZjdD1z/QYSfKq0j6NJK9WVDjv/giphy.gif")
                    left center
                    no-repeat
                  `
        }).then(() => {
            window.location = '/'
        })
    }else{
        console.log("entra al else")
        $("#theWinner").text("Lo siento ")
        Swal.fire({
            title: 'NUEVO GANADOR',
            text: 'Lo siento, sigue intentando',
            width: 600,
            padding: '3em',
            color: 'maroon',
            confirmButtonColor: '#ff0000',
            backdrop: `
                    rgba(255, 0, 0, 0.4)
                    url("https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExOTAyZTU0YTdlZTFkMmU1MTk0NjY0YmRkYjUxMjczMGU4ZTk5MjNjMiZjdD1n/xUA7aO7FnhFicdRp3W/giphy.gif")
                    left center
                  `
        }).then(() => {
            window.location = '/'
        })
    }
})

/**
 * Callback que se ejecuta cuando el servidor emite el evento 'yourWinner'.
 * Actualiza el contenido de dos elementos HTML con los mensajes "¡GANADOR!", y "GANASTE".
 */
socket.on('yourWinner', () =>{
    console.log("entra a ganador");
    $("#ganaste").text("¡GANADOR!");
    $("#theWinner").text("GANASTE ")
})

/**
 * Callback que se ejecuta cuando el servidor emite el evento 'expelled'.
 * Muestra una alerta de error indicando que el usuario ha sido expulsado por presionar el botón de "GANÉ" sin haber ganado realmente.
 * Redirige al usuario a la página de inicio de sesión al hacer clic en el botón OK.
 */
socket.on('expelled', () =>{
    console.log("entra a expulsar");
    Swal.fire(
        'No eres un ganador, EXPULSADO',
        'Presionaste el botón GANÉ, sin haber ganado, serás expulsado',
        'error'
    ).then(()=>{
        window.location = 'login'
    })
})

/**
 * Callback que se ejecuta cuando el servidor emite el evento 'updateFlag'.
 * Emite un evento 'changeAllFlags' al servidor.
 */
socket.on('updateFlag', () => {
    socket.emit('changeAllFlags')
})

/**
 * Función que emite el evento 'disconnect' hacia el servidor, cuando un cliente se desconecta.
 */
socket.on('disconnect', function(){
    console.log('Servidor desconectado')
});

export default {gameOver, numbersWinner, iAmTheWinner,flagBallot, ballot, ready, game, connect, gamers, timerOff };

