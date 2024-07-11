// used for loginPage.html
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni login_code : 0
$(document).ready(function () {
    $("#login_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
		login();
	});

    $("#login_admin_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
		login(as_admin);
	});

    $("#password_input").on("keydown", function (e) {
        // Check if the pressed key is the "Enter" key (code 13)
        //  if (e.keyCode === 13) {
        //      e.preventDefault();
        //      login();
        //  }
        if (e.key === "Enter") {
            e.preventDefault();
            login();
        }
    });

	function login(option_admin) {
        const tmp_usrn = $("#username_input").val();
        const tmp_pwd = $("#password_input").val();

        // go to login_restctrl.java
		$.ajax({
			url: '/api/login',
			data: {
                username: tmp_usrn,
                password: tmp_pwd,
                option : option_admin
            },
            dataType : 'json',
            method: 'POST',
			success: function (outcome) {
                if (outcome["login_code"] == 0) {
                    window.location.href = "../templates/mostPopularPage.html";
                } else if (outcome["login_code"] == 1) {
                    alert("Username not found in the database. Please make sure you have typed the username correctly.");
                    $("#username_input").val("");
                    $("#password_input").val("");
                } else if (outcome["login_code"] == 2) {
                    alert("Incorrect password");
                    $("#password_input").val("");
                }
                else if (outcome["login_code"] == 3) {
                    alert("Unable to connect to the database :-(\nPlease try again later.");
                }
                else {
                    alert("Error: unknown login_code");
                }
            },
            error: function (xhr, status, error) {
                console.log("Error: " + error);
            }
        });
	}
});
