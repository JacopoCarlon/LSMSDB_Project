// used for loginPage.html
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $("#filterdo_btn").click(function (e) {
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

    $("#keyword").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
            $('#search_results').modal('show');
        }
    });

    function search() {
        const searchTerm = $("#search_input").val();
        const keyword = $("#keyword").val();
        const category = "user";

        const selected_genre = $(dropdownMenuButton_genre)

        //  const category = $("#category_input").val();

        $.ajax({
            url: '/api/search',
            data: { term: searchTerm, category: category },
            dataType : 'json',
            method: 'GET',

            success: function (arrayResults) {
                //  window.location.href = "../templates/userFilterPage.html";
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



function displayUser(users){
    const container = $(".modal-body");
    container.empty();

    users.forEach(function (user){
        let usDiv = $("<div id=\"user_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        usDiv.append($("<i class=\"fa fa-user-circle fa-3x me-3\"></i>"))
        let usInf = $("<div class=\"d-flex flex-column\"></div>");
        usDiv.append(usInf);
        usInf.append($("<h3 id=\"user_name\" style=\"font-weight: bold; margin-bottom: 0;\"></h3>").text(user.username));
        usInf.append($("<p id=\"user_full_name\"></p>").text(user.name + " " + user.surname));

        usDiv.click(function() {
            window.location.href = '/user?username=' + user.username;
        });

        container.append(usDiv);
    });

}
