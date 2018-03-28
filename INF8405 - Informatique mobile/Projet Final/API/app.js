const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const Location = require('./models/locations');
const User = require('./models/users');

var busboy = require('connect-busboy'); //middleware for form/file upload
var path = require('path');     //used for file path
var fs = require('fs-extra');       //File System - for file manipulation

const app = express();
app.use(busboy());
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.set('port', (process.env.PORT || 3000));
// Connect to Mongoose
var options = {
    server: { socketOptions: { keepAlive: 300000, connectTimeoutMS: 30000 } },
    replset: { socketOptions: { keepAlive: 300000, connectTimeoutMS: 30000 } }
};    
var mongodbUri = 'mongodb://SuperPandaFunk:zaq1xsw2@ds125469.mlab.com:25469/tourista';
mongoose.connect(mongodbUri, options);
const db = mongoose.connection;



app.get('/', (req,res) =>{
    res.json("success")
});

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

app.post('/api/locations/comment/:_locationId', (req, res) => {
    var loc = req.params._locationId;
    var message = req.body
    Location.addComment(loc,message,function (err, locations) {
        if (err) {
            throw err;
        }
        res.json(locations);
    });
});

app.post('/api/locations/image/:_locationId', (req, res, next) => {
    var fstream;
    
    req.pipe(req.busboy);
    req.busboy.on('file', function (fieldname, file, filename) {
        console.log("Uploading: " + filename);

        //Path where image will be uploaded
        fstream = fs.createWriteStream(__dirname + '/img/'+ req.params._locationId + filename);
        file.pipe(fstream);
        fstream.on('close', function () {
            console.log("Upload Finished of " + req.params._locationId + filename);
        });
        var pathToimg = __dirname + '/img/' + req.params._locationId + filename;
        var loc = req.params._locationId;
        fstream.on('close', function () {
            var a = {
                img:{
                data: Buffer,
                contentType: String
            }}

            a.img.data = fs.readFileSync(pathToimg);
            a.img.contentType = 'image/png';
            
            
            Location.findByIdAndUpdate({_id:loc},{$push:{Images:a}},function(err,response){ 
                res.send(response);
            })
        });
        
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

app.listen(app.get('port'));
console.log(`Running on port ${app.get('port')}`);