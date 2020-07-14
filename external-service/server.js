'use strict';

const express = require('express');
var config = require('./config/config');
const app = express()

// Constants
const PORT = process.env.PORT || 80
console.log(`Configured ${config.serviceName} on port ${PORT}`)

// App
app.get('/message', (req, res) => {
    console.log(`GET /message -> received, replying after a delay of ${config.getOperationDelay}ms`)
    res.setHeader('Content-Type', 'application/json');
    setTimeout(() => {
        console.log(`GET /message -> replaying`)
        res.send(JSON.stringify({
            title: `"${config.serviceName}" message`,
            message: 'hello world'
        }));
    }, config.getOperationDelay);
});


app.use(express.json())
app.post('/message', (req, res) => {
    console.log(`POST /message -> received ${JSON.stringify(req.body)}, replying after a delay of ${config.getOperationDelay}ms`)
    setTimeout(() => {
        console.log(`POST /message -> replaying`)
        res.json(req.body)
    }, config.postOperationDelay);
});

app.listen(PORT, () => {
    console.log(`Server listening on port ${PORT}`)
})