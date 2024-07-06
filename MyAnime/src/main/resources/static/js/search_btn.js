// used for loginPage.html
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $("#search_btn").click(function (e) {
        e.preventDefault();
        search();
        $('#search_results').modal('show');
    });

    $("#close_btn").click(function (e){
        e.preventDefault();
        $(".modal-body").empty();
    })

    $("#search_input").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
            $('#search_results').modal('show');
        }
    });

    function search() {
        const searchTerm = $("#search_input").val();    //  field
        const category = $("#category_input").val();    //  anime / user

        if (category == "anime"){
            window.location.href = "../templates/filterPage.html?anime="+searchTerm;
        }
        else if (category == "user"){
            window.location.href = "../templates/filterPage.html?user="+searchTerm;
        }                          
    }
});