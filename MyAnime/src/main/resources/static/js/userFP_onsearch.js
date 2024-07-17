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

        //  alert("asking for username : " + keyword);
        //  alert("asking for category : " + category);

        $.ajax({
            url: '/api/search',
            data: { term: keyword, category: category },
            dataType : 'json',
            method: 'GET',

            success: function (arrayResults) {
                alert("we got something from the search : " + JSON.stringify(arrayResults[0]));
                let myMap = new Map(Object.entries(JSON.parse(JSON.stringify(arrayResults[0]))));
                alert("myMap size : " + myMap.size);
                for (const [key, value] of myMap) {
                    console.log(`The value for key ${key} is ${value}`);
                }
                console.log("CIAO: "+ myMap.get('username'));
                window.location.href = "/userFilterPage";
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
    alert("begin displaying user");
    alert("user list : " + user_lst);
    alert("typeof user list : " + typeof(user_lst));

    //  let parsedlst = JSON.parse(user_lst);
    //  alert("parsedlst list : " + parsedlst);
    //  alert("typeof parsedlst list : " + typeof(parsedlst));



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



    
}
