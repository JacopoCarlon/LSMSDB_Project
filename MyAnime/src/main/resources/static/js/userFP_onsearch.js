// used for loginPage
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $("#filterdo_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
        search();
        return false;
    });

    $("#close_btn").click(function (e){
        e.preventDefault();
        e.stopPropagation();
        return false;
    })

    $("#search_input").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
            return false;
        }
    });

    $("#keyword").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
            return false;
        }
    });

    function search() {
        //  const searchTerm = $("#search_input").val();
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
                //  window.location.href = "/userFilterPage";
                if(arrayResults==null || arrayResults.length == 0) {
                    alert("no results found")
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



function displayUser(arrayResults){

    let trg_container = document.getElementById("ufpResults_section");
    trg_container.innerHTML = '';

    let num_res = arrayResults.length;
    //  alert("num_res : " + num_res);

    for (let i = 0; i < num_res; i++) {

        let number_i = parseInt(i);

        let myMap = new Map(Object.entries(JSON.parse(JSON.stringify(arrayResults[number_i]))));

        let this_username = myMap.get('username') ;
        //  alert("this_username : " + this_username);
        //  alert("this_username type: " + typeof(this_username) );
        let this_statsEpisodes =  myMap.get('statsEpisodes') ;
        //  alert("this_statsEpisodes : " + this_statsEpisodes);
        let this_joinDate =  myMap.get('joinDate') ;
        //  alert("this_joinDate : " + this_joinDate);


        let trg_usrDiv = document.createElement("div");
        let trg_usrInfo = document.createElement("div");
        trg_usrDiv.appendChild(trg_usrInfo);

        let trg_uname = document.createElement("h4");
        trg_uname.innerText = this_username;
        trg_uname.style= "font-weight: bold; margin-bottom: 0";
        trg_usrInfo.appendChild(trg_uname);

        let trg_stats = document.createElement("h4");
        trg_stats.innerText = this_statsEpisodes;
        trg_stats.style= "font-weight: bold; margin-bottom: 0";
        trg_usrInfo.appendChild(trg_stats);

        let trg_date = document.createElement("h4");
        trg_date.innerText = this_joinDate;
        trg_date.style= "font-weight: bold; margin-bottom: 0";
        trg_usrInfo.appendChild(trg_date);

        trg_usrDiv.onclick = function () {
            window.location.href = '/userPage?username=' + this_username;
        }

        trg_container.appendChild(trg_usrDiv);
    }
}



//  let userDiv = $("<div id=\"user_info\" class=\"d-flex flex-column top-container gap-1\"></div>");
//  let userInf = $("<div id=\"user_details\" class=\"user-details-sm d-flex flex-column mt-1\"></div>");
//  userDiv.append(userInf);
//  userInf.append($("<h4 id=\"user_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(this_username));
//  let div1 = $("<div class=\"d-flex m-0\"></div>");
//  userInf.append(div1);
//  div1.append($("<p style=\"font-size: medium;\" id=\"user_stats\"></p>").text(this_statsEpisodes));
//  let div2 = $("<div class=\"d-flex m-0\"></div>");
//  userInf.append(div2);
//  div2.append($("<p style=\"font-size: medium;\" id=\"user_joined\"></p>").text(joinDate));
//  userDiv.click(function () {
//      window.location.href = '/userDetailsPage?title=' + this_username;
//  });
//  container.append(userDiv);
















