const mongoose = require('mongoose');

const locationSchema = mongoose.Schema({
        lat: 
        {
            type:Number, 
            require: true      
        },
        lon:
        {
            type: Number,
            require: true
        },
        adress:{
            type: String,
            require: true
        },
        description:{
            type: String
        }

        // add the user who create 
        //add array of coments
});

const Location = module.exports = mongoose.model('Location',locationSchema);

// Get locations

module.exports.getLocations = (callback, limit) =>
{
    Location.find(callback).limit(limit);
}


// Add locations

module.exports.addLocations = (loc, callback) => {
    
    Location.create(loc, callback);
}