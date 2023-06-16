const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const morgan = require('morgan');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv/config');
const authJwt = require('./helpers/jwt');
const errorHandler = require('./helpers/error-handler')
const port = process.env.PORT || 8080;

app.use(cors());
app.options('*', cors())


//middleware
app.use(bodyParser.json());
app.use(morgan('tiny'));
app.use(authJwt());
app.use(errorHandler);

//Routes
const categoriesRoutes = require('./routes/categories');
const productsRoutes = require('./routes/products');
const usersRoutes = require('./routes/users');
const ordersRoutes = require('./routes/orders');

const api = process.env.API_URL;

app.use(`${api}/categories`, categoriesRoutes);
app.use(`${api}/products`, productsRoutes);
app.use(`${api}/users`, usersRoutes);
app.use(`${api}/orders`, ordersRoutes);

// conection to database
mongoose.connect(process.env.CONNECTION_STRING,{
    // userNewUrlParser: true,
    // userUnifiedTopology:true,
    // dbName: 'recomenu_db'
})
.then (() => {
    console.log('database connection is ready')
})
.catch((err) => {
    console.log(err)
});


app.listen(8080, () => {
    console.log(api);
    console.log('Server is Runing http://localhost:5000');
});