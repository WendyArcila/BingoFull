/**
 * Ruta para guardar los datos y crear un nuevo juego(game),
 * así mismo, crear los jugadores(gamers) que van a participar del juego.
 * @version 1.0.000 2023-01-10
 * @author Wendy Arcila
 */

const express = require('express');
const router = express.Router();
const User = require('../models/user');
const bcryptjs = require('bcryptjs');
const mongodb = require('../database/db')

/**
 * Maneja la solicitud de inicio de sesión y auténtica al usuario.
 * @param {Object} req - Objeto de solicitud HTTP
 * @param {Object} res - Objeto de respuesta HTTP
 * @param {Function} next - Función de siguiente middleware en la cadena
 */
router.post('/', async function  (req, res, next) {
  const {user, password} = req.body;
  if (user && password){
    const foundUser = await User.findOne({ user });
    if (foundUser) {
      const isPasswordMatch = await bcryptjs.compare(password, foundUser.password);
      if (isPasswordMatch) {
        req.session.name = foundUser.name;
        req.session.id = foundUser._id;
        req.session.user = foundUser.user;
        req.session.loggedIn = true;
        renderLogin(res, "Conexión Exitosa", "Ingreso correcto", 'success', '') 
      } else {
        req.session.loggedIn = false;
        renderLogin(res, "Error", "Contraseña incorrecta", 'error', 'login')
      }
    } else {
      req.session.loggedIn = false;
      renderLogin(res, "Error", "Usuario incorrecto", 'error', 'login')
    }
  } else {
    req.session.loggedIn = false;
    renderLogin(res, "Error", "Faltan datos de autenticación", 'error', 'login')
  }
  });

/**
 * Renderiza la página de inicio de sesión con una alerta personalizada
 * @param {object} res - Objeto respuesta que se envía al navegador
 * @param {string} title - Título de la alerta
 * @param {string} message - Mensaje de la alerta
 * @param {string} icon - Icono de la alerta
 * @param {string} ruta - Ruta a la que se redirige el usuario al cerrar la alerta
 */
function renderLogin(res, title, message, icon, ruta) {
    res.render('login', {
      alert: true,
      alertTitle: title,
      alertMessage: message, 
      alertIcon: icon,
      showConfirmButton: true, 
      allowOutsideClick: false,
      timer: false,
      ruta: ruta
    });
}


/**
 * Se exporta el contenido del módulo
 * @type {Router}
 */

module.exports = router;