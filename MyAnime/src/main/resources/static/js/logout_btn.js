//  used in all headers for logout-button 
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/logout") e mi ritorni outcome_code : 0

$(document).ready(function () {
    $('#logout_btn').click(function (e) {
        e.preventDefault();

        $.ajax({
            url: '/api/logout',
            dataType : 'json',
            method: 'POST',
            success: function(outcome) {
                if(outcome["outcome_code"] == 0) {
                    window.location.href = "../templates/loginPage.html";
                }
                else {
                    alert("Si è verificato un problema durante il logout");
                }
            },
            error: function (xhr, status, error) {
                console.log("Error: " + error);
            }
        });
    });
});
