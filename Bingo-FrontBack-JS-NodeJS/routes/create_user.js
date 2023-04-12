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

/**
 * Crea un nuevo usuario con los datos enviados en la solicitud y lo guarda en la base de datos.
 * @param {Object} req - La solicitud HTTP.
 * @param {Object} res - La respuesta HTTP que se enviará al cliente.
 * @param {Function} next - Función para pasar el control al siguiente middleware.
 */
router.post('/', async function  (req, res, next) {
  const user = new User ({
    name: req.body.name,
    lastName: req.body.lastName,
    age: req.body.age,
    email : req.body.email,
    user: req.body.user,
    password: await bcryptjs.hash(req.body.password, 8)
  });
  user
    .save()
    .then(newUser => req.session._id = user._id)
    .then(result => res.render('register', {
      alert: true,
      alertTitle:"Registro",
      alertMessage:"¡Registro Exitoso!",
      alertIcon:'success',
      showConfirmButton:false,
      timer:1500,
      ruta:''
    }))
    .catch(err => res.json(err));
});

/**
 * Se exporta el contenido del módulo
 * @type {Router}
 */

module.exports = router;