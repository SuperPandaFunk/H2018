/////////////////////////////
// HOW TO USE TOURISTA API //
/////////////////////////////

////////////USERS////////////
idFacebook: String
LastName: String
FirstName: String



//////////LOCATIONS//////////
lat: Double
lon: Double
address: String
description: String
postedBy: mongoose.Schema.Types.ObjectId, ref: USERS
Comments: [Array]
    text: String
    postedBy: mongoose.Schema.Types.ObjectId, ref: USERS
Images:[Array]
    img: Buffer,
    postedBy: mongoose.Schema.Types.ObjectId, ref: USERS
