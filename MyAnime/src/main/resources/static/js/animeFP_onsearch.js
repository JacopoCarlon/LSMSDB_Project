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

        const selected_genre = $(dropdownMenuButton_genre)

        //  const category = $("#category_input").val();

        $.ajax({
            url: '/api/search',
            data: { term: searchTerm, category: category },
            dataType : 'json',
            method: 'GET',

            success: function (arrayResults) {
                //  window.location.href = "../templates/animeFilterPage.html";
                if(arrayResults==null || arrayResults.length == 0) {
                    const container = $(".modal-body");
                    container.empty();
                    container.append($("<p>No results found</p>"));
                }
                else {
                    displayAnime(arrayResults);
                }
                
            },
            error: function (xhr, status, error) {
                console.error("Error: " + error);
            }
        });
    }
});

function displayAlbums(anime_lst) {
    const container = $(".modal-body");
    container.empty();

    anime_lst.forEach(function(anime) {
        let animeDiv = $("<div id=\"anime_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        animeDiv.append($("<img id=\"anime_cover\" class=\"anime-cover-sm mt-1 mb-1\">").attr("src", anime.coverURL));
        let animeInf = $("<div id=\"anime_details\" class=\"anime-details-sm d-flex flex-column mt-1\"></div>");
        animeDiv.append(animeInf);
        animeInf.append($("<h4 id=\"anime_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(anime.title));
        let div1 = $("<div class=\"d-flex m-0\"></div>");
        animeInf.append(div1);
        div1.append($("<p style=\"font-size: medium;\" id=\"anime_artists\"></p>").text(anime.artists.join(", ")));

        animeDiv.click(function() {
            window.location.href = '/animeDetails?animeId=' + anime.id;
        });

        container.append(animeDiv);
    });
}
