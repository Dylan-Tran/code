
//npm install nodemon -g
//npm install express -save
var express = require('express');
//npm install body-parser --save
var bodyParser = require('body-parser');
var path = require('path');
var expressValidator = require('express-validator');
var session = require('express-session');
var cookieParser = require('cookie-parser');

var config = require('./config/database');
var mongoose = require('mongoose');


mongoose.connect(config.database, { useNewUrlParser: true });
var db = mongoose.connection;

db.once('open', function () {
    console.log('Connected to mongoDB');
});

db.on('error', function (err) {
    console.log("Error connecting to database. Check connection string");
});

//npm install ejs --save

//npm install express-validator --save

var app = express();

//Global var --Error for express-validator
app.use(function (req, res, next) {
    res.locals.errors = null;
    next();
});


//Express Validator middleware
app.use(expressValidator());

//View Engine
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

//Body Parser Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

//Session Middleware
app.use(session({
    secret: 'lynnIsGay',
    resave: false,
    saveUninitialized: true,
}));

//CookieParser Middleware
app.use(cookieParser());

//Static folder
app.use(express.static(path.join(__dirname, '/public')));


//routes folder
const routes = require('./routes/routes');
app.use(routes);


app.listen(3000, function () {
    console.log('Server started on Port 3000');
})

