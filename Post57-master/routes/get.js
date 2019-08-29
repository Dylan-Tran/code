var express = require('express');


var routes = express.Router();

//handles get request

routes.get('/', (req,res)=>{
    console.log("hit");
    res.render('index');
})



//pictures

routes.get('/homePic', (req,res)=>{
    res.render('homePhto.jpg');
})

module.exports = routes;