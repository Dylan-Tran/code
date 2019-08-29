const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');


//Bring in model
let ClientInformation = require('../models/ClientInformation');
let appointment = require('../models/appointment');
let admin = require('../models/admin');
let requestAppointment = require('../models/request');

router.get('/', (req, res) => {
    res.render('index');
});


//Formating the input time  1HHMM
function inputFormatTime(hour, min, select) {
    var builder = 10000;
    var toggle = 0;
    if (select == "AM") {
        toggle = 0;
    } else {
        toggle = 12;
    }

    builder += (parseInt(hour) + toggle) * 100;
    builder += parseInt(min);
    return builder;
}

router.post('/CreateAccount', (req, res) => {
    //check if the input is empty 
    req.checkBody('FirstName', 'First name is required').notEmpty();
    req.checkBody('LastName', 'Last name is required').notEmpty();
    req.checkBody('Email', 'Email is required').notEmpty().isEmail();
    req.checkBody('DOB', 'Birthday is required').notEmpty();
    req.checkBody('Number1', 'Phone Number 1 is required').notEmpty();
    req.checkBody('Password', 'Password is required').notEmpty();

    var errors = req.validationErrors();

    if (errors) {
        res.render('createAccount', {
            errors: errors
        });
    } else {
        //create the user object
        user = {
            password: req.body.Password,
            email: req.body.Email,
            firstName: req.body.FirstName,
            lastName: req.body.LastName,
            phoneNumber: req.body.Number1,
            phoneNumber2: req.body.Number2,
            birthday: req.body.DOB
        };

        //create the object with the schema so it can be saved to the database
        let clientInformation = new ClientInformation(user);

        //save to the database
        clientInformation.save(function (err) {
            if (err) {
                console.log("error");
            } else {
                res.render('Login', {
                    message: "Your account was created! Try logging in"
                });
            }
        });
    }
});


router.post('/login', (req, res) => {
    var username = req.body.Username;
    var password = req.body.Password;

    //Check the basic 
    req.checkBody('Username', 'Username is Required').notEmpty();
    req.checkBody('Password', 'Password is Required').notEmpty();

    var errors = req.validationErrors();

    //Check for error if there are error return them. If not search the database for the proper information
    if (errors) {
        res.render('login', {
            errors: errors,
            message: ""
        });
    } else {

        let query = { username: username };

        //see if the user is using an admin login. If not the search will return null
        admin.findOne(query, function (err, adminObject) {
            if (err) {
                console.log("Error with database");
            };
            //If the Database returns null then it a client. If the database returns on object
            //then the username for an admin is right still have to check password
            if (adminObject == null) {
                //This process is for a client login

                //Check to see if the username is in the database
                ClientInformation.findOne(query, function (err, user) {
                    if (err) {
                        console.log("Error");
                    };
                    //If the username is not in the database return to the page with a message
                    if (user === null) {
                        res.render('login', {
                            message: 'No user found'
                        });
                    } else {
                        //If username exist in database check to see if the password matches with the hashed password
                        bcrypt.compare(password, user.password, function (err, isMatch) {
                            if (err) {
                                console.log("Error");
                            };
                            if (isMatch) {
                                /*
                                If the password match, the server will send the appointments/requested appointments associated 
                                with the username. The information will be used in the display. Also the appointments/request 
                                are sorted
                                */
                                appointment.find(query, (err, events) => {
                                    if (err) {
                                        console.log("Error");
                                    };
                                    requestAppointment.find(query, (err, request)=>{
                                        if(err){
                                            console.log("Error");
                                        };
                                        console.log(request);
                                        res.render('account', {
                                            user,
                                            events,
                                            request
                                        });
                                    }).sort({id: 1});
                                }).sort({ id: 1 });
                            } else {
                                /*
                                If password is wrong, User will be returned to the login page
                                with a message
                                */
                                res.render('login', {
                                    message: "Wrong password"
                                });
                            }
                        });
                    }

                })
            } else {
                //Admin login 

                //Only need to check password because we already check username
                bcrypt.compare(password, adminObject.password, function (err, isMatch) {
                    if (err) throw err;

                    if (isMatch) {
                        /*
                        If it match, the server will get ALL the appointments because the user 
                        is admin. I will need it for the display on the next page also
                        the appointments will be sorted
                        */
                        appointment.find(function (err, events) {
                            res.render('admin', {
                                events,
                                message: "No message"
                            })
                        }).sort({ id: 1 });
                    } else {
                        /*
                        If password is wrong, User will be returned to the login page
                        with a message
                        */
                        res.render('login', {
                            message: "Wrong password"
                        });
                    }

                });

            }
        })
    }
});

//same thing as the post('/login') just routes to something else
router.post('/loginToCreate', (req, res) => {
    var username = req.body.Username;
    var password = req.body.Password;

    //Check the basic 
    req.checkBody('Username', 'Username is Required').notEmpty();
    req.checkBody('Password', 'Password is Required').notEmpty();

    var errors = req.validationErrors();

    //Check for error if there are error return them. If not search the database for the proper information
    if (errors) {
        res.render('loginToCreate', {
            errors: errors,
            message: ""
        });
    } else {

        let query = { username: username };

        //Check to see if the username is in the database
        ClientInformation.findOne(query, function (err, user) {
            if (err) {
                console.log("Error");
            };
            //If the username is not in the database return to the page with a message
            if (user === null) {
                res.render('loginToCreate', {
                    message: 'No user found'
                });
            } else {
                //If username exist in database check to see if the password matches with the hashed password
                bcrypt.compare(password, user.password, function (err, isMatch) {
                    if (err) {
                        console.log("Error");
                    };
                    if (isMatch) {
                        /*
                        If the password match, move on with the next page while storing the login information
                        */
                        
                        req.session.creating = {
                            username: user.username, 
                            firstName: user.firstName,
                            lastName: user.lastName
                        };

                        res.render('pickDateAppointment', {
                            user
                        })
                        
                    } else {
                        /*
                        If password is wrong, User will be returned to the login page
                        with a message
                        */
                        res.render('loginToCreate', {
                            message: "Wrong password"
                        });
                    }
                });
            }

        })
    }
});


router.post('/forgotPassword', (req, res) => {
    //set up the search
    query = {
        username: req.body.Username,
        birthday: req.body.DOB
    }

    //Search the database for the query
    ClientInformation.findOne(query, (err, user) => {
        if(err){
            console.log("Error");
        }
        //If the search comes up empty then that means that the username or DOB was wrong
        if (user == null) {
            res.render('forgotpassword', {
                message: "Wrong username or DOB"
            })
        } else {
            //If the information was correct. We have to update the database

            //I need to hash the password they entered
            bcrypt.hash(req.body.Password, 10, function (err, hash) {
                if (err) {
                    console.log("Error");
                }
                //The client information will be updated with the new hash password
                ClientInformation.updateOne(query, { password: hash }, (err) => {
                    if (err) {
                        console.log("Error");
                    }
                    res.render('login', {
                        message: "Password changed"
                    })
                });
            });
        }
    })
})

router.post('/delete', (req, res) => {
    query = { id: req.body.Delete };

    //Have to check if the query is empty 
    if (query.id.length > 0) {
        /*
            I need an object to collect all the appointments for a count later to see if anything was deleted. before will hold all the appointment.
        */
        appointment.find((err, before) => {
            if (err) {
                console.log("error");
            }

            //Delete the appointment
            appointment.deleteMany(query, (err) => {
                if (err) {
                    console.log("error");
                }

                //Grab a new object (events) with all the appointments and then compare it with "before". After the comparison the object will be use for the display
                appointment.find(function (err, events) {
                    if (err) {
                        console.log("error");
                    }

                    //comparison and messages
                    if (before.length == events.length) {
                        res.render('admin', {
                            events,
                            message: "Wrong appointment ID"
                        })
                    } else {
                        res.render('admin', {
                            events,
                            message: "Appointment was deleted"
                        })
                    }
                }).sort({ id: 1 });
            })
        })
    } else {
        appointment.find(function (err, events) {
            res.render('admin', {
                events,
                message: "Nothing was entered"
            })
        }).sort({ id: 1 });
    }
})


router.post('/createAppointment', (req, res) => {
    //format to hold the data in the database
    var startTime = inputFormatTime(req.body.shour, req.body.smin, req.body.sam);
    var endTime = inputFormatTime(req.body.ehour, req.body.emin, req.body.eam);

    //create object to hold input information
    newAppointment = {
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        description: req.body.Description,
        month: req.body.month,
        day: req.body.day,
        startTime: startTime,
        endTime: endTime
    }

    //outputs a database object that has functions associated with database
    let appointmentObject = new appointment(newAppointment);

    //Save the object to the database and will perform .pre operation
    appointmentObject.save(function (err) {
        if (err) {
            console.log("error");
        } else {
            appointment.find(function (err, events) {
                res.render('admin', {
                    events,
                    message: "Appointment added"
                })
            }).sort({ id: 1 });
        }
    })
})

router.post('/search', (req, res) => {
    var query = {};
    
    /*
    If I want to return all client information the query needs to be {}. This is not the same as query.firstName = "";
    To prevent the latter from happening I check to see if there anything in the string before input. 
    This feature is also neeeded because if I want to search for last name the query can only contain {lastName: "something"}.
    This is not the same thing as {lastName: "Something", firstName: ""}.
    }
    */

    if (req.body.searchFirst.length != 0) {
        query.firstName = req.body.searchFirst;
    }

    if (req.body.searchLast.length != 0) {
        query.lastName = req.body.searchLast;
    }

    //Search the database for the query then send user to the page to display information
    ClientInformation.find(query, function (err, information) {
        if (err) {
            appointment.find(function (err, events) {
                res.render('admin', {
                    events,
                    message: "Error in the database"
                })
            }).sort({ id: 1 });
        } else {
            res.render('clientInfo', {
                information
            });
        }
    })

})

router.post('/searchDay', (req, res) => {
    //set up query
    let query = {
        month: req.body.month,
        day: req.body.day
    }

    //search and display
    appointment.find(query, (err, events) => {
        if (err) {
            console.log("err");
        }
        res.render('calendarView', {
            events,
            query
        });

    })
})


//step 1 of creating appointment
/*
same thing as post('/searchDay) expect a session token is being used.
The function of a session token is to save input as the client moves between pages.
the session token here is creating. Creating will point to an object which will hold information.
calling creating at the end of this call will return
{
    month: "A month",
    day: "a day"
}
}
*/
router.post('/createSearchDay', (req,res)=>{
    let query = {
        month: req.body.month,
        day: req.body.day
    }

    
    appointment.find(query, (err, events) => {
        if (err) {
            console.log("err");
        }

        req.session.creating.month = req.body.month;
        req.session.creating.day = req.body.day;

        res.render('requestAppointment', {
            events,
            query
        });

    })
})

//Step 2 of creating appointment
/*
Continue adding more information to the session token (creating)
I also made an object here requestAppointmentObject but it just for display on the next page.
*/
router.post('/requestAppointment', (req, res)=>{
    var startTime = inputFormatTime(req.body.shour, req.body.smin, req.body.sam);

    req.session.creating.startTime = startTime;
    req.session.creating.description = req.body.Description;
    
    
    requestAppointmentObject = {
        username: req.session.creating.username,
        firstName: req.session.creating.firstName,
        lastName: req.session.creating.lastName,
        month: req.session.creating.month,
        day: req.session.creating.day,
        startTime: req.session.creating.startTime,
        description: req.session.creating.description
    }
    
    res.render('confirm', {
        requestAppointmentObject
    });
})

//Step 3 of creating appointment
/*
If the client confirms their appointment then the appointment will be added to the request databased.
*/
router.post('/confirmRequest', (req,res)=>{
    //The session token is used to create an object (requestAppointmentObject)
    let requestAppointmentObject = new requestAppointment(req.session.creating);

    //Delete the session token
    req.session.destroy();

    //Save the object to the request databased
    requestAppointmentObject.save((err) => {
        if (err) {
            console.log("Error");
        } else {
            //This is for redirecting to the next page
            
            //set up query
            let query = { username: requestAppointmentObject.username };

            ClientInformation.findOne(query, function (err, user) {
                if (err) {
                    console.log("Error 1");
                } else {
                    appointment.find(query, (err, events) => {
                        if (err) {
                            console.log("Error 2");
                        };
                        requestAppointment.find(query, (err, request)=>{
                            if(err){
                                console.log("Error 3");
                            };                        
                            res.render('account', {
                                user,
                                events,
                                request
                            });
                        }).sort({id: 1});
                    }).sort({ id: 1 });
                }
            })
        }
    })
})

//redirect
router.post('/redirectRequest', (req,res)=>{
    requestAppointment.find((err, request)=>{
        if(err){
            console.log("error 1");
        }
        res.render('requestView', {
            request,
            message: ""
        });
    })
})


router.post('/requestToAppointment', (req, res) => {
    let query = { id: req.body.requestID };

    //find the request Appointment
    requestAppointment.findOne(query, function (err, target) {
        if (err) {
            console.log("Error");
        } else {
            //Now that I found the request I can just copy the information into a new object (template)
            var startTime = inputFormatTime(req.body.shour, req.body.smin, req.body.sam);
            var endTime = inputFormatTime(req.body.ehour, req.body.emin, req.body.eam);

            var template = {
                username: target.username,
                firstName: target.firstName,
                lastName: target.lastName,
                description: req.body.Description,
                startTime: startTime,
                endTime: endTime,
                month: target.month,
                day: target.day
            };

            
            //Use the template to make the database object
            let appointmentObject = new appointment(template);

            
            //Save the object to the database
            appointmentObject.save( function (err) {
                if (err) {
                    console.log(err);
                };

                //delete the request appointment
                requestAppointment.deleteMany(query, (err)=>{
                    if(err){
                        console.log("error");
                    }
                    //Set up the next page
                    requestAppointment.find((err, request)=>{
                        if(err){
                            console.log("error");
                        }
                        res.render('requestView', {
                            request, 
                            message: "Appointment made"
                        });    
                    })
                })
            })
        }
    })
})

//Same thing for post('/delete') just different redirect
router.post('/deleteRequest', (req,res)=>{
    query = { id: req.body.Delete };

    //Have to check if the query is empty 
    if (query.id.length > 0) {
        /*
            I need an object to collect all the appointments for a count later to see if anything was deleted.
        */
        requestAppointment.find((err, before) => {
            if (err) {
                console.log("error");
            }

            //Delete the appointment
            requestAppointment.deleteMany(query, (err) => {
                if (err) {
                    console.log("error");
                }

                //Grab a new object with all the appointments and then compare it. After the comparison the object will be use for the display
                requestAppointment.find(function (err, request) {
                    if (err) {
                        console.log("error");
                    }

                    //comparison and messages
                    if (before.length == request.length) {
                        res.render('requestView', {
                            request,
                            message: "Wrong request ID"
                        })
                    } else {
                        res.render('requestView', {
                            request,
                            message: "Request appointment was deleted"
                        })
                    }
                }).sort({ id: 1 });
            })
        })
    } else {
        requestAppointment.find(function (err, request) {
            res.render('requestView', {
                request,
                message: "Nothing was entered"
            })
        }).sort({ id: 1 });
    }
})

//basic pathing

router.post('/redirectCreate', (req, res)=>{
    res.render('loginToCreate', {
        message: ""
    })
})

router.post('/admin', (req, res) => {
    appointment.find(function (err, events) {
        res.render('admin', {
            events,
            message: "No message"
        })
    }).sort({ id: 1 });
});

router.get('/Login', (req, res) => {
    res.render('login', {
        message: ""
    });
})

router.get('/appointment', (req, res) => {
    res.render('appointment')
})


router.get('/Account', (req, res) => {
    res.render('account')
})

router.post('/CreateAccount', (req, res) => {

    res.redirect('createAccount');
})

router.get('/CreateAccount', (req, res) => {
    res.render('createAccount');
})

router.get('/ForgotPassword', (req, res) => {
    res.render('forgotpassword', {
        message: ""
    });
})

router.get('/Index', (req, res) => {
    res.render('index');
})

router.get('/AboutUs', (req, res) => {
    res.render('aboutUs');
})

router.get('/Calendar', (req, res) => {
    res.render('calendar');
})

router.get('/LoginToCreate', (req, res) => {
    res.render('loginToCreate', {
        message: ""
    })
})

module.exports = router;
