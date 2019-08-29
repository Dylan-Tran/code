var mongoose = require('mongoose');
mongoose.set('useCreateIndex', true);
var bcrypt = require('bcryptjs');

//Table for client information
var ClientInformationSchema = new mongoose.Schema({
    username: {
        type:String,
        index:true
    },
    password: {
        type: String
    },
    email: {
        type: String
    },
    firstName: {
        type: String
    },
    lastName: {
        type: String
    },
    phoneNumber: {
        type: Number
    },
    phoneNumber2: {
        type: Number
    },
    birthday: {
        type: Date
    }  
});


//before inputs are save I need to ajust or assign predetermine values
ClientInformationSchema.pre('save', function (next) {
    var user = this;

    /*
    Username system will be their firstname + first 4 characters of their
    last name with an underscore inbetween. This is to prevent people from
    forgetthing their username.
    */
    user.Username = this.FirstName + "_" + this.LastName.substring(0,5);

    //For safely reason the password will be hashed
    bcrypt.hash(user.Password, 10, function (err, hash){
      if (err) {
        return next(err);
      }
      user.Password = hash;
      next();
    });
});

module.exports = mongoose.model('ClientInformation', ClientInformationSchema);