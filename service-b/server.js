'use strict';

const express = require('express');
const app = express()

// Constants
const PORT = process.env.PORT || 80
console.log(`Configured port ${PORT}`)

// App
app.get('/message', (req, res) => {
    console.log(`GET message received`)
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify({
        title: 'service b message',
        message: 'hello world'
    }));
});


app.use(express.json())
app.post('/message', (req, res) => {
    console.log(`POST message received`)
    res.json(req.body)
});

app.listen(PORT, () => {
    console.log(`Server listening on port ${PORT}`)
})