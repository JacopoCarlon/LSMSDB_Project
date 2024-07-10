// used for signupPage.html
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/signup") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $('#signup_btn').click(function (e) {
        e.preventDefault();

        const name              = $('#name_input').val();
        const surname           = $('#surname_input').val();
        const username          = $('#username_input').val();
        const password          = $('#password_input').val();
        const repeatPassword    = $('#repeat_password_input').val();
        const birthday          = $('#birthday_input').val();
        const email             = $('#email_input').val();
        const sex               = $('#sex_input').val();

        if (!name || !surname || !username || !password ||
            !repeatPassword || !birthday || !sex || !email) {
            alert("Please fill in all fields.");
            return;
        }

        if (password !== repeatPassword) {
            alert("Passwords do not match. Please re-enter them.");
            return;
        }

        if (!isValidEmail(email)) {
            alert("Please enter a valid email address.");
            return;
        }

        if (!isValidBirthday(birthday)){
            alert("Please enter your real birthdate.");
            return;
        }

        const formData = {
            name        : name,
            surname     : surname,
            username    : username,
            password    : password,
            birthday    : birthday,
            sex         : sex,
            email       : email
        };

        $.ajax({
            url: '/api/signup',
            data: formData,
            dataType: 'json',
            method: 'POST',

            success: function(response) {
                switch(response.outcome_code) {
                    case 0:
                        alert("Registration successful!\nYou can now proceed with the login.");
                        window.location.href = "./loginPage.html";
                        break;
                    case 1:
                        alert("Username already in use. Please choose another username.");
                        break;
                    case 2:
                        alert("Email already in use. Please use a different email.");
                        break;
                    case 3:
                        alert("Error in MongoDB connection.");
                        break;
                    case 4:
                        alert("Other error in MongoDB.");
                        break;
                    case 5:
                        alert("Error during user registration in MongoDB.");
                        break;
                    case 6:
                        alert("Error during user registration in Neo4j.");
                        break;
                    case 7:
                        alert("Error in Neo4j connection.");
                        break;
                    default:
                        alert("Unknown error.");
                }
            },
            error: function (xhr, status, error) {
                alert("ERROR: " + error);
            }
        });
    });
});

function isValidEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
}

//  new Date(year, month, day, hours, minutes, seconds, milliseconds);
//  birthday = "yyyy-mm-dd"
function isValidBirthday(birthday){
    var today_date = new Date();
    var birthdate = birthday.split("-");
    var born = new Date(year=birthdate[0], monthIndex=birthdate[1]-1, date=birthdate[2]);
    var birthday = new Date(year=today_date.getFullYear(), monthIndex=born.getMonth(), date=born.getDate());

    var cur_age =  today_date.getFullYear() - born.getFullYear();

    if (today_date <= birthday) {
        cur_age =  cur_age - 1;
    }

    if (cur_age < 14 || cur_age > 150){
        return false;
    }
}

