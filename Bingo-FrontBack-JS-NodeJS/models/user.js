/**
 * Modelo(datos) del Juego (Game)
 * @version 1.0.000 2023-01-10
 * @author Wendy Arcila
 */

const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/**
 * Esta función toma una dirección de correo electrónico como entrada y la válida utilizando una expresión regular.
 * @param {string} email - La dirección de email a validar
 * @return {boolean} - Verdadero si el email es válido, falso en caso contrario
 */
const validateEmail = (email) => {
    const regex = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
    return regex.test(email);
};

/**
 * Esta clase representa un esquema de usuario para una base de datos, definiendo los campos y restricciones para los datos de usuario.
 */
const userSchema = new Schema({
    name:{
        type: String,
        trim: true,
        required: true
    },
    lastName:{
        type: String,
        trim: true,
        required: true
    },
    age:{
        type: Number,
        min: [18, 'La edad mínima es de 18 años'],
        max: [90, 'La edad máxima es de 90 años']
    },
    email: {
        type: String,
        lowercase: true,
        trim: true, 
        required: [true, 'El correo es requerido'],
        unique: true, 
        validate:{
            validator: validateEmail,
            message: 'Por favor digite un correo válido'
        }
    },
    user: {
        type: String, 
        trim: true, 
        unique:  [true, 'El usuario del juego debe ser único.']
    },
    password: {
        type: String, 
        trim: true
    },
    idSQl:{
        type: Number,
        unique: true
    }
});

/**
 * Se exporta el contenido del schema del juego como Game.
 * @type exports
 */
module.exports = User = mongoose.model('User', userSchema);