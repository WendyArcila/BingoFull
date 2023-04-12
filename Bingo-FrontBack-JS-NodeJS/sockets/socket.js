
const { io } = require('../app');

/**
 * Establece las conexiones del servidor con los clientes.
 * @version 1.0.000 2023-04-08
 * @author Wendy Arcila
 */

let interval = 0;
let tiempoRestante = 10;
let gamers = [];
let user;
let gameId;
let rotationCount = 1;

/**
 * Maneja la conexión de un nuevo cliente en el servidor
 * @param {Socket} socket - El objeto Socket para el cliente conectado
 * @fires gamers - Emite un evento con la lista actualizada de usuarios
 * @listens disconnect - Maneja la desconexión de un cliente
 * @listens ready - Llama a la función timer y a la función game
 */
io.on('connection', (socket) => {
    console.log('Nuevo cliente conectado');

  gamer(socket)
      .then(data => {
          console.log("gamers name: "+ data.name)
          user = data.name;
          gamers.push(data.name)
          io.emit('gamers', gamers);
      });

    // Manejar la desconexión de un cliente
    socket.on('disconnect', () => {
      console.log('Cliente desconectado');

      gamers =  gamers.filter(gamer => gamer !== user);
      io.emit('gamers', gamers);

        // Si no hay más clientes conectados, detenemos el intervalo de tiempo
        if (io.engine.clientsCount === 0) {
            clearInterval(interval);
            interval = null;
            tiempoRestante = 10;
        }
    });

    // Llamamos a la función timer después de que el cliente se ha conectado
    socket.on('ready', () => {
        console.log("ingresa a la ready");
        timer(socket);
        game();
    });
});

/**
 * Escucha el evento 'gamer' en el socket y devuelve una promesa que se resuelve con los datos del jugador.
 * @param {Socket} socket - El socket a través del cual se comunican el cliente y el servidor.
 * @returns {Promise} Una promesa que se resuelve con los datos del jugador.
 * @throws {Error} Si ocurre un error al traer los datos del jugador.
 */
function gamer(socket) {
    return new Promise((resolve, reject) => {
        socket.on('gamer', (data) => {
            console.log("gamers: " + data.name);
            resolve(data);
        })
        socket.on('connect_error', (error) => {
            console.log('Error al traer gamers: ', error);
            reject(error);
        });
    });
}

/**
 * Comprueba si hay un solo cliente conectado. Si es así, crea un juego y envía
 * un mensaje a todos los clientes cada segundo con el identificador del juego.
 * Si ya hay un juego generado, muestra un mensaje en la consola.
 */
function game(){
    if(io.engine.clientsCount === 1) {
        createGame()
            .then(data => {
                gameId = data;
                setInterval(() =>{
                    if(tiempoRestante > 0){
                        io.emit('newGame', {data: data});
                    }
                }, 1000)
            })
            .catch(error => {
                console.error(error);
            });
    }else {
        console.log("El juego ya está creado")
    }

}

/**
 * Comprueba si hay un solo cliente conectado. Si es así, inicia un temporizador
 * descendente de 1 segundo y envía un mensaje a todos los clientes cada segundo
 * con el tiempo restante en formato de minutos y segundos. Cuando el temporizador
 * llega a cero, detiene el temporizador y llama a la función ballot().
 * @param {object} socket - El socket del cliente que ha comenzado el temporizador.
 */
function timer(socket){
    if(io.engine.clientsCount === 1) {
        console.log("ingresa a la función")
        interval = setInterval(() => {
            let minutos = Math.floor(tiempoRestante / 60);
            let segundos = tiempoRestante % 60;

            minutos = minutos < 10 ? "0" + minutos : minutos;
            segundos = segundos < 10 ? "0" + segundos : segundos;

            io.emit('timerOn', {minutos, segundos});

            tiempoRestante--;

            if (tiempoRestante < 0) {
                clearInterval(interval);
                io.emit('timerOff')
                ballot(socket);
            }
        }, 1000);
    }
}

/**
 * Crea un nuevo juego en el servidor y obtiene el ID del juego.
 * @returns {Promise<string>} El id del juego creado
 * @throws {Error} Si ocurre un error durante la solicitud HTTP
 */
function createGame (){

     return fetch('http://localhost:8080/game/save', {
        method: 'POST',
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(response => response.json())
        .then(json => {
            console.log("Juego creado: " + json.idGame);
            console.log(json);
            return json.idGame;
        })
        .catch(error => console.error(error));
}


/**
 * Crea un nuevo movimiento para un juego específico.
 * @param {string} idGame - El ID del juego
 * @returns {Promise<Object>} La información relacionada con el movimiento creado
 * @throws {Error} Si ocurre un error durante la solicitud HTTP
 */
function createMove(idGame){
    return fetch('http://localhost:8080/move/save', {
        method: 'POST',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            idGame: idGame
        })
    })
        .then(response => response.json())
        .then(json => {
            console.log(json)
            return json
        })
        .catch(error => console.error(error))
}

/**
 * Actualiza el ganador del juego con el usuario actual.
 * @throws {Error} Si ocurre un error durante la solicitud HTTP
 */
function updateWinnerInGame(){
    let url = 'http://localhost:8080/game/update/winner/' + gameId;
    fetch (url, {
        method: 'PATCH',
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            winner: user
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
 * Inicia el proceso generar las balotas del juego.
 * @param {Object} socket - Socket utilizado para la comunicación.
 */
function ballot(socket){
    let flag = true;
    let intervalBallot;
    let allNumbers = [];

    //Función que recibe el cambio de la bandera de votación.
    socket.on ('flagBallot', (data)=>{
        flag = data.flag;
        io.emit('updateFlag', {flag});
    });

    //Función que cambia la bandera de la balota para todos los sockets.
    socket.on('changeAllFlags', () =>{
        flag = false;
    })

    //Función que se ejecuta cuando no hay ganador.
    socket.on('noWinner', () =>{
        flag = true;
        startInterval();
    });

    //Función que se ejecuta cuando termina el juego.
    socket.on('gameOver', () =>{
        io.emit('gotWinner');
    })

    //Función que determina si un socket es el ganador.
    socket.on('amITheWinner', () =>{
       setTimeout(() => {
           if(flag){
               socket.emit('expelled');
           }else {
               socket.emit('yourWinner');
               updateWinnerInGame();
           }
       }, 5000);
    });

    //Función que crea un nuevo movimiento en el juego.
    const createNewMove = async () => {
        let {number, letter} = await createMove(gameId);
        if (!allNumbers.includes(number)) {
            allNumbers.push(number);
            io.emit('ballot', {
                num: number,
                lett: letter,
                count: rotationCount
            });
            rotationCount++;
            return true;
        } else {
            return false;
        }
    };

    //Función que inicia el intervalo de balotas.
    const startInterval = () => {
        intervalBallot = setInterval(async () => {
            if (flag) {
                const isNewMoveCreated = await createNewMove();
                if (!isNewMoveCreated) {
                    clearInterval(intervalBallot);
                    startInterval();
                }
            } else {
                clearInterval(intervalBallot);
                io.emit('isWinner');

            }
        }, 3000);
    };

    startInterval();
}


