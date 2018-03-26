const mongoose = require('mongoose');

const userSchema = mongoose.Schema({
        idFacebook: 
        {
            type:String, 
            require: true
        },
        LastName:
        {
            type: String,
            require: true
        },
        FirstName:{
            type: String,
            require: true
        }
        // photo
        
});

const User = module.exports = mongoose.model('User', userSchema);

// Get user

module.exports.getUser = (callback, limit) =>
{
    User.find(callback).limit(limit);
}

module.exports.getUserById = (id,callback) => {
    User.findById(id,callback);
}

module.exports.getUserByFBId = (FBid, callback) => {
    User.findOne({idFacebook: `${FBid}`},callback);
}
// Add user
module.exports.addUser = (toAdd, callback) => {
    User.create(toAdd, callback);
}
// Update user
module.exports.updateUser = (id, user, options, callback) => {
    var query = { idFacebook: id};
    var update = {
        LastName: user.LastName,
        FirstName: user.FirstName
    }
    User.findOneAndUpdate(query, update, options, callback);
}
