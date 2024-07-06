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

function displayAlbums(albums) {
    const container = $(".modal-body");
    container.empty();

    albums.forEach(function(album) {
        let albumDiv = $("<div id=\"album_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        albumDiv.append($("<img id=\"album_cover\" class=\"album-cover-sm mt-1 mb-1\">").attr("src", album.coverURL));
        let albumInf = $("<div id=\"album_details\" class=\"album-details-sm d-flex flex-column mt-1\"></div>");
        albumDiv.append(albumInf);
        albumInf.append($("<h4 id=\"album_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(album.title));
        let div1 = $("<div class=\"d-flex m-0\"></div>");
        albumInf.append(div1);
        div1.append($("<p style=\"font-size: medium;\" id=\"album_artists\"></p>").text(album.artists.join(", ")));

        albumDiv.click(function() {
            window.location.href = '/albumDetails?albumId=' + album.id;
        });

        container.append(albumDiv);
    });
}
