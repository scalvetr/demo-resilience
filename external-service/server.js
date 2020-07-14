'use strict';
//read config

var appConfig = require('./config/app.config');
var chaosMonkeyConfig = require('./config/chaos-monkey.config');
const express = require('express');
const ChaosMonkey = require('chaos-monkey');

const app = express()
ChaosMonkey.initialize(app)

// Constants
const PORT = process.env.PORT || 80
console.log(`Configured ${appConfig.serviceName} on port ${PORT}`)

// App
app.get('/message', (req, res) => {
    console.log(`GET /message -> received, replying after a delay of ${appConfig.getOperationDelay}ms`)
    res.setHeader('Content-Type', 'application/json');
    setTimeout(() => {
        console.log(`GET /message -> replaying`)
        res.send(JSON.stringify({
            title: `"${appConfig.serviceName}" message`,
            message: 'hello world'
        }));
    }, appConfig.getOperationDelay);
});


app.use(express.json())
app.post('/message', (req, res) => {
    console.log(`POST /message -> received ${JSON.stringify(req.body)}, replying after a delay of ${appConfig.getOperationDelay}ms`)
    setTimeout(() => {
        console.log(`POST /message -> replaying`)
        res.json(req.body)
    }, appConfig.postOperationDelay);
});

app.listen(PORT, () => {
    console.log(`Server listening on port ${PORT}`)
})