$(document).ready(function() {
    let user = $("#username").text();

    // links to own anime lists

    $("#curWatch_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(0);
    });

    $("#complete_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(1);
    });

    $("#on_hold_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(2);
    });

    $("#dropped_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(3);
    });

    $("#planWtc_btn").click(function (e) {
        e.preventDefault();
        gotoUserAnimeListPage(4);
    });

    function gotoUserAnimeListPage(tipo) {
        window.location.href = "/userAnimeList?type="+tipo + "&username=" + user;
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


    $("#reviews_btn").click(function(e){
        e.preventDefault();
        window.location.href = "/userReviewsPage?username=" + user;
    });

});


/*
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




    // Button for view the complete list of animes liked
    $("#liked_animes_btn").click(function(){
        $.ajax({
            url: "/api/userLikedAnimes",
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_a) {
                if(result_a.length === 0){
                    const container = $("#liked_animes_container");
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




function displayLikedAnimes(liked){
    let increment = 0;
    const container = $("#liked_animes_container");
    container.empty();

    // For each anime find with the ajax request, create his container with all his information and append it in the main container
    // for the liked animes
    liked.forEach(function (anime_tmp){
        let animeDiv = $("<div class=\"d-flex anime_liked gap-1 p-1 align-items-center mb-1\"></div>");
        animeDiv.append($("<img class=\"anime-cover shadow\" style=\"max-width: 100px;\">").attr("src", anime_tmp.coverURL));
        let animeInf = $("<div class=\"d-flex flex-column anime_inf\"></div>");
        animeDiv.append(animeInf);
        animeInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(anime_tmp.animeName));
        animeInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(anime_tmp.artistName));

        // If we are in our profile page, for each anime we like we have the button to dislike it
        if(window.location.href.includes("profilePage")){
            animeDiv.append("<button id=\"dislike_A_btn_" + increment + "\" class=\"btn btn-danger ms-auto\">Dislike</button>");
        }
        $(animeInf).click(function (){
            // Before go to the page with the details of the selected anime, we have to encode the special characters
            let send_string_artist;
            if(anime_tmp.artistName.includes(",")){
                let artist_parts = anime_tmp.artistName.split(",");
                let substring = artist_parts[0];
                send_string_artist = encodeURIComponent(substring)
            } else {
                send_string_artist = encodeURIComponent(anime_tmp.artistName);
            }
            window.location.href = "/animeDetails?title=" + encodeURIComponent(anime_tmp.animeName) + "&artist=" + send_string_artist;
        })
        container.append(animeDiv);
        let id="dislike_A_btn_" + increment;
        $("#" + id).click(function () {
            // AJAX call to dislike an anime, for each anime liked
            let username = $("#username").text();
            let animeTitle = anime_tmp.animeName
            let artistsAsString = anime_tmp.artistName;

            $.ajax({
                url: '/api/animeDetails/removeLikesAnime',
                dataType: 'json',
                type: 'POST',
                data: {
                    username: username,
                    animeTitle: animeTitle,
                    artists: artistsAsString
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code === 0){
                        alert("Anime disliked");
                    }
                    else if(response.outcome_code === 1)
                        alert("Dislike addition unsuccessful");
                    else
                        alert("Error occurred while removing like to anime");
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
