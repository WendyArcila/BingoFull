//Se invoca express 
const express = require('express');
const app = express();
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const mongodb = require('./database/db')

/**
 * Inicializa el servidor de Express y Socket.io en el puerto 3000.
 * También establece la configuración para el manejo de formularios,
 * el directorio público, el motor de plantillas y las rutas de la aplicación.
 * @version 1.0.000 2023-02-28
 * @author Wendy Arcila
 */

app.set('loggedIn', false);


//se invoca sockets
const socket = require('socket.io');
const http = require('http');
const server = http.createServer(app);
module.exports.io = socket(server);
require('./sockets/socket')

// Manejar la conexión de un cliente
server.listen(3000, () => {
    console.log('Servidor de WebSockets escuchando en el puerto 3000');
});

/**
 * Maneja la conexión de un cliente a través de Socket.io
 */
function socketConnect(){
io.on('connection', (socket) => {
    console.log('Nuevo cliente conectado');
  
    // Manejar la desconexión de un cliente
    socket.on('disconnect', () => {
      console.log('Cliente desconectado');
    });
  });
}

//Se setea urlencoded para capturar los datos del formulario
app.use(logger('dev'));
app.use(express.urlencoded({extended: false}));
app.use(express.json());
app.use(cookieParser());

//Se setea el directorio public
app.use('/public', express.static('public'));
app.use(express.static(path.resolve(__dirname, '../public')));

//Se establece el moto de plantillas
app.set('view engine', 'ejs');

//Se invoca a bcryptjs
const bcryptjs = require('bcryptjs');

//Se crea la var de sesión
const session = require('express-session');

app.use(session({
    secret:'123456',
    resave: true,
    saveUninitialized:true
}))

//Se establecen las rutas

/**
 * Maneja la petición GET a /login
 */
app.get('/login',  (req,res)=>{
    res.render('login');
});

/**
 * Maneja la petición GET a /register
 */
app.get('/register',  (req,res)=>{
    res.render('register');
});

/**
 * Maneja la petición GET a /
 */
app.get('/', (req,res)=>{
    console.log("Valor de loggedIn: " + req.session.loggedIn)
    if(req.session.loggedIn){
        res.render('index',{
            login:true,
            name:req.session.name
        })
    }else{
        res.render('index', {
            login: false,
            name:'Debe iniciar sesión'
        })
    }
});

/**
 * Maneja la petición GET a /logout
 */
app.get('/logout', (req,res)=>{
    req.session.destroy(()=>{
        res.redirect('/')
    });
});

/**
 * Maneja la petición GET a /room
 */
app.get('/room',  (req,res)=>{
    if(req.session.loggedIn){
        res.render('room',{
            login:true,
            name:req.session.name,
            user:req.session.user
        })
    }else{
        res.render('index', {
            login: false,
            name:'Debe iniciar sesión'
        })
    }
});

/**
 * Habilita el middleware de CORS para permitir solicitudes entre orígenes (cross-origin)
 * @param {Object} options - Las opciones para el middleware CORS
 * @param {string} options.origin - El dominio de origen que se permite para las solicitudes CORS
 * @returns {function} - Middleware CORS
 */
const cors = require('cors');

// Se usa el middleware CORS en Express app
app.use(cors({
    origin: 'http://localhost:8080'
}));

app.use('/register', require('./routes/create_user'));
app.use('/auth', require('./routes/authentication'));


/**
 * Se exporta el contenido del módulo
 * @type {app}
 */
module.exports = app;
