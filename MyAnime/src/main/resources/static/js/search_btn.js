// used for loginPage
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $("#search_btn").click(function (e) {
        e.preventDefault();
        search();
    });

    $("#close_btn").click(function (e){
        e.preventDefault();
        $(".modal-body").empty();
    })

    $("#search_input").on("keydown", function (e) {
        
        if (e.keyCode === 13 ) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
        }
    });

    function search() {
        const category = $("#category_input").val();    //  anime / user
        if(category=="anime" && window.location.href.indexOf("animeFilterPage") > -1 ){
            return;
        }
        if(category=="user" && window.location.href.indexOf("userFilterPage") > -1 ){
            return;
        }
        const searchTerm = $("#search_input").val();    //  field

        if (category == "anime"){
            window.location.href = "/animeFilterPage?keyword="+searchTerm;
        }
        else if (category == "user"){
            window.location.href = "/userFilterPage?keyword="+searchTerm;
        }                          
    }
});