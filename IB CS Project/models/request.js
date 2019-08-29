var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var requestSchema = new Schema({
    id: {
        type: Number,
        index: true
    },
    username: {
        type: String
    },
    firstName: {
        type: String
    },
    lastName: {
        type: String
    },
    description: {
        type: String
    },
    month: {
        type: Number
    },
    day: {
        type: Number
    },
    startTime: {
        type: Number
    }
});

requestSchema.pre('save', function(next){
    var appointment = this;
    
    //There will follow the username system to allow clients to look at their appointments
    appointment.username = this.firstName + "_" + this.lastName.substring(0,5);

    //Format for ID so it can be sorted
    appointment.id = 100000000 + this.month*1000000 + this.day*10000 + (this.startTime - 10000); 

    next();
});

module.exports = mongoose.model('request', requestSchema);
