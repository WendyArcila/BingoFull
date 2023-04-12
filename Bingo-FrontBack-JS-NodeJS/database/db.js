
/**
 * Clase que establece la conexión con la base de datos
 * @version 1.0.000 2023-01-10
 * @author Wendy Arcila
 */

const mongoose = require ('mongoose');

mongoose.set('strictQuery', true);

const mongodb = 'mongodb://127.0.0.1/login_bingo';

/**
 * Conexión a la base de datos mongodb
 */
mongoose.connect(mongodb, {useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => console.log('MongoDB connected'))
    .catch(err => console.log('MongoDB not connected',err));

module.exports = mongoose;