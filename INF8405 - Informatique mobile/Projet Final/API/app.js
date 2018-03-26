const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const Location = require('./models/locations');
const User = require('./models/users');
const app = express();

app.use(bodyParser.json());
// Connect to Mongoose
mongoose.connect('mongodb://localhost/tourista');
const db = mongoose.connection;

app.get('/api/locations', (req,res) =>{
    Location.getLocations(function(err, locations){
        if(err){
            throw err;
        }
        res.json(locations);
    });
});

app.get('/api/locations/near/:_max/:_lat/:_lon', (req, res) => {
    Location.getLocations(function (err, locations) {
        if (err) {
            throw err;
        }
        var filterLocation = locations.filter(function (el){
            return getDistanceFromLatLonInKm(el.lat, el.lon, req.params._lat, req.params._lon, req.params._max)
        });
        res.json(filterLocation);
    });
});

app.post('/api/locations', (req, res) => {
    var loc = req.body;
    Location.addLocations(loc,function (err, loc) {
        if (err) {
            throw err;
        }
        res.json(loc);
    });
});



app.get('/api/users/fb/:_fbid', (req, res) => {
    User.getUserByFBId(req.params._fbid, function (err, user) {
        if (err) {
            throw err;
        }
        res.json(user);
    });
});

app.post('/api/users', (req, res) => {
    var toAdd = req.body;
    User.addUser(toAdd, function (err, toAdd) {
        if (err) {
            throw err;
        }
        res.json(toAdd);
    });
});

app.put('/api/users/:_fbid', (req, res) => {
    var fb = req.params._fbid;
    var user = req.body;

    User.updateUser(fb, user, {}, function (err, user) {
        if (err) {
            throw err;
        }
        res.json(user);
    });
});

function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2, maxD) {
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2 - lat1);  // deg2rad below
    var dLon = deg2rad(lon2 - lon1);
    var a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2)
        ;
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c; // Distance in km
    return d <= maxD;
}

function deg2rad(deg) {
    return deg * (Math.PI / 180)
}

app.listen(3000);
console.log('Running on port 3000');