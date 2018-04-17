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
        Images:[
            {
            img: String
        }
    ]
});


const Location = module.exports = mongoose.model('Location',locationSchema);

// Get locations

module.exports.getLocations = (callback, limit) =>
{
    Location.find({},'-Images -Comments',callback).populate('postedBy').populate('Comments.postedBy').limit(limit);
}

module.exports.getLocationById = (loc,callback) => {
    Location.findById(loc, callback).populate('postedBy').populate('Comments.postedBy')
}



// Add locations

module.exports.addLocations = (loc, callback) => {   
    Location.create(loc, callback);
}

module.exports.addComment = (loc, message, callback) => {
    Location.findByIdAndUpdate(loc,{$push:{Comments:message}},callback);
}

module.exports.addImage = (loc, image, callback) => {
   

    //Location.findByIdAndUpdate(loc,{$push:{Images:a}},callback);
}