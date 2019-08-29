var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//creating a simple table to hold admin information
var AdminSchema = new Schema({
    username: {
        type:String,
        index:true
    },
    password: {
        type: String
    }
});

//before the information is saved I need to format or assign predetermine values
AdminSchema.pre('save', function(next){
    var user = this;
   //For safely reason the password will be hashed
   bcrypt.hash(user.Password, 10, function (err, hash){
    if (err) {
      return next(err);
    }
    user.Password = hash;
    next();
  });
});

module.exports = mongoose.model('Admin', AdminSchema);
