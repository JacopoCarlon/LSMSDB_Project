$(document).ready(function() {
    let user = $("#username").text();

    // links to own anime lists

    $("#curWatch_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(1);
    });

    $("#complete_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(2);
    });

    $("#on_hold_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(3);
    });

    $("#dropped_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(4);
    });

    $("#planWtc_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(6);
    });

    function gotoUserAnimeListPage(status) {
        window.location.href = "/userAnimeList?status="+status + "&username=" + user;
    }



    // links to following and followed lists

    //  users followed by <user>
    $("#followingUsers_btn").click(function (e) {
        e.preventDefault();
        window.location.href = "/userFollow?type=following&username=" + user;
    });

    // users that follow <user>
    $("#followerUsers_btn").click(function (e) {
        e.preventDefault();
        window.location.href = "/userFollow?type=follower&username=" + user;
    });




    //  links to all user's reviews
    $("#reviews_btn").click(function(e){
        e.preventDefault();
        window.location.href = "/userReviews?username=" + user;
    });




});

/*

    // Button for view the complete list of reviews by the user
    $("#reviews_btn").click(function(){
        $.ajax({
            url: "/api/userReviews",
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_a) {
                if(result_a.length === 0){
                    const container = $("#reviews_container");
                    container.empty();
                    container.append("<p>No anime liked.</p>");
                }
                displayLikedAnimes(result_a);
            },
            error: function() {
                alert("Error on the AJAX request.");
            }
        });
    });


    // Button for view the complete list of follower user
    $("#followingUsers_btn").click(function() {
        $.ajax({
            url: "/api/userFollowingUsers",
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_f) {
                if(result_f.length === 0){
                    const container = $("#following_container");
                    container.empty();
                    container.append($("<p>No user following.</p>"));
                }else{
                    displayFollowing(result_f);
                }
            },
            error: function() {
                alert("Error on the AJAX request.");
            }
        });
    });

    // Button for view the complete list of follower user
    $("#followerUsers_btn").click(function() {
        $.ajax({
            url: "/api/userFollowerUsers",
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_f) {
                if(result_f.length === 0){
                    const container = $("#follower_container");
                    container.empty();
                    container.append($("<p>No user following.</p>"));
                }else{
                    displayFollower(result_f);
                }
            },
            error: function() {
                alert("Error on the AJAX request.");
            }
        });
    });

});


function displayFollowing(follower) {
    let increment = 0;
    const container = $("#follower_container");
    container.empty();

    // For each user find with the ajax request, create his container with all his information and append it in the main container
    // for the follower users
    follower.forEach(function (user_tmp){
        let userDiv = $("<div class=\"d-flex user_foll p-3 mb-1\"></div>");
        let userInf = $("<h5 class=\"mb-0\"></h5>");
        userDiv.append(userInf);
        userInf.append($("<i class=\"fa fa-user me-3\"></i>"));
        userInf.append($("<span></span>").text(user_tmp.username));

        // If we are in our profile page, for each user we follow we have the button to unfollow him
        if(window.location.href.includes("profilePage")){
            userDiv.append($("<button id=\"unfollow_btn_" + increment + "\" class=\"btn btn-danger ms-auto\">Unfollow</button>"))
        }
        $(userInf).click(function (){
            // Before go to the page with the details of the selected user, we have to encode the special characters
            let encoded_user = encodeURIComponent(user_tmp.username);
            window.location.href = "/user?username=" + encoded_user;
        })
        container.append(userDiv);
        let id="unfollow_btn_" + increment;
        $("#" + id).click(function () {
            // AJAX call to unfollow a user, for each user follower
            let username = $("#username").text();
            $.ajax({
                url: '/api/removeFollow',
                dataType: 'json',
                type: 'POST',
                data: {
                    user1: username,
                    user2: user_tmp.username
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code === 0){
                        alert("Follow removed");
                    }
                    else if(response.outcome_code === 1)
                        alert("Follow removal unsuccessful");
                    else
                        alert("Error occurred while removing a follow");
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        });
        increment++;
    })
}





function displayFollower(follower) {
    let increment = 0;
    const container = $("#follower_container");
    container.empty();

    // For each user find with the ajax request, create his container with all his information and append it in the main container
    // for the follower users
    follower.forEach(function (user_tmp){
        let userDiv = $("<div class=\"d-flex user_foll p-3 mb-1\"></div>");
        let userInf = $("<h5 class=\"mb-0\"></h5>");
        userDiv.append(userInf);
        userInf.append($("<i class=\"fa fa-user me-3\"></i>"));
        userInf.append($("<span></span>").text(user_tmp.username));

        // If we are in our profile page, for each user we follow we have the button to unfollow him
        if(window.location.href.includes("profilePage")){
            userDiv.append($("<button id=\"unfollow_btn_" + increment + "\" class=\"btn btn-danger ms-auto\">Unfollow</button>"))
        }
        $(userInf).click(function (){
            // Before go to the page with the details of the selected user, we have to encode the special characters
            let encoded_user = encodeURIComponent(user_tmp.username);
            window.location.href = "/user?username=" + encoded_user;
        })
        container.append(userDiv);
        let id="unfollow_btn_" + increment;
        $("#" + id).click(function () {
            // AJAX call to unfollow a user, for each user follower
            let username = $("#username").text();
            $.ajax({
                url: '/api/removeFollow',
                dataType: 'json',
                type: 'POST',
                data: {
                    user1: username,
                    user2: user_tmp.username
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code === 0){
                        alert("Follow removed");
                    }
                    else if(response.outcome_code === 1)
                        alert("Follow removal unsuccessful");
                    else
                        alert("Error occurred while removing a follow");
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        });
        increment++;
    })
}

*/

