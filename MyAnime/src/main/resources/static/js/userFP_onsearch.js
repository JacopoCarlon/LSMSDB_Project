// used for loginPage
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $("#filterdo_btn").click(function (e) {
        e.preventDefault();
        search();
    });
    $("#close_btn").click(function (e){
        e.preventDefault();
    })

    $("#search_input").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
        }
    });

    $("#keyword").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
        }
    });

    function search() {
        const searchTerm = $("#search_input").val();
        const keyword = $("#keyword").val();
        const category = "user";

        //  const category = $("#category_input").val();

        $.ajax({
            url: '/api/search',
            data: { term: keyword, category: category },
            dataType : 'json',
            method: 'GET',

            success: function (arrayResults) {
                //  window.location.href = "/userFilterPage";
                if(arrayResults==null || arrayResults.length == 0) {
                    const container = $(".modal-body");
                    container.empty();
                    container.append($("<p>No results found</p>"));
                }
                else {
                    displayUser(arrayResults);
                }
                
            },
            error: function (xhr, status, error) {
                console.error("Error: " + error);
            }
        });
    }
});



function displayUser(user_lst){
    const container = $(".ufpResults_container");
    container.empty();

    user_lst.forEach(function (tsuser) {

        let userDiv = $("<div id=\"user_info\" class=\"d-flex flex-column top-container gap-1\"></div>");
        let userInf = $("<div id=\"user_details\" class=\"user-details-sm d-flex flex-column mt-1\"></div>");
        userDiv.append(userInf);
        userInf.append($("<h4 id=\"user_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(tsuser.username));
        let div1 = $("<div class=\"d-flex m-0\"></div>");
        userInf.append(div1);
        div1.append($("<p style=\"font-size: medium;\" id=\"user_stats\"></p>").text(tsuser.statsEpisodes ));
        let div2 = $("<div class=\"d-flex m-0\"></div>");
        userInf.append(div2);
        div2.append($("<p style=\"font-size: medium;\" id=\"user_joined\"></p>").text(tsuser.joinDate ));
        userDiv.click(function () {
            window.location.href = '/userDetailsPage?title=' + tsuser.username;
        });
        container.append(userDiv);
    });


    
    //  const container = $(".modal-body");
    //  container.empty();
    //  users.forEach(function (user){
    //      let usDiv = $("<div id=\"user_info\" class=\"d-flex mb-1 align-items-center\"></div>");
    //      usDiv.append($("<i class=\"fa fa-user-circle fa-3x me-3\"></i>"))
    //      let usInf = $("<div class=\"d-flex flex-column\"></div>");
    //      usDiv.append(usInf);
    //      usInf.append($("<h3 id=\"user_name\" style=\"font-weight: bold; margin-bottom: 0;\"></h3>").text(user.username));
    //      usInf.append($("<p id=\"user_full_name\"></p>").text(user.name + " " + user.surname));
    //      usDiv.click(function() {
    //          window.location.href = '/user?username=' + user.username;
    //      });
    //      container.append(usDiv);
    //  });
    
}
