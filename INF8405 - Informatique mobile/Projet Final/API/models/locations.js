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
        address:{
            type: String,
            require: true
        },
        description:{
            type: String
        },
        postedBy:{
            type: mongoose.Schema.Types.ObjectId,
            ref: 'User'
        },
        Comments:
        [{
            text: String,
            postedBy:{
                type: mongoose.Schema.Types.ObjectId,
                ref: 'User'
            }
        }],
        Images:[{
            img:{
                data: Buffer,
                contentType: String
            },
            postedBy:{
                type: mongoose.Schema.Types.ObjectId,
                ref: 'User'
            }
        }]
});

const Location = module.exports = mongoose.model('Location',locationSchema);

// Get locations

module.exports.getLocations = (callback, limit) =>
{
    Location.find(callback).populate('postedBy').populate('Comments.postedBy').limit(limit);
}


// Add locations

module.exports.addLocations = (loc, callback) => {   
    Location.create(loc, callback);
}