// used for loginPage
$(document).ready(function () {
    $("#filterdo_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
        search();
        return false;
    });

    $("#close_btn").click(function (e) {
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


    function getChosenByName(trg_name){
        let ele = document.getElementsByName(trg_name);
        let trg_arr = [];
        for (i = 0; i < ele.length; i++) {
            if (ele[i].checked){
                //  alert("element value : " + ele[i].value);
                //  alert("element id : " + ele[i].id);
                //  alert("element id : " + ele[i].id.toString());
                trg_arr.push(ele[i].id.toString());
            }
        }
        //  alert("trg_arr for name " + trg_name + "is : " + trg_arr);
        return trg_arr;
    }




    function search() {
        let trgFilterNames = ["genre_cbox", "year_cbox", "type_cbox", "status_cbox", "rating_cbox", "sort_cbox"]
        let filters = [];
        for (i_name in trgFilterNames) {
            filters.push(getChosenByName(trgFilterNames[i_name]) );
        }
        //  const searchTerm = $("#search_input").val();
        const keyword = $("#keyword").val();

        const category = "anime";

        //  const category = $("#category_input").val();

        $.ajax({
            url: '/api/search',
            data: {
                term: keyword,
                category: category,
                genreList : filters[0],
                yearList : filters[1],
                typeList : filters[2],
                statusList : filters[3],
                ratingList : filters[4],
                sortList : filters[5]
            },
            dataType: 'json',
            method: 'GET',

            success: function (arrayResults) {
                //  //  window.location.href = "/animeFilterPage";
                if (arrayResults == null || arrayResults.length == 0) {
                    alert("no results found")
                } else {
                    displayAnime(arrayResults);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error: " + error);
            }
        });
    }
});




function displayAnime(arrayResults){

    //  alert("begin displayAnime")

    //  let magicdiv = document.getElementById("magicalDiv");
    //  let content = document.createElement("p");
    //  content.innerHTML = "contenutissimoooooooo";
    //  magicdiv.appendChild(content);

    //  alert("before emptying container")
    let trg_container = document.getElementById("afpResults_container");
    trg_container.innerHTML = '';
    trg_container.classList.add("d-flex" , "flex-column" , "p-1" , "gap-1" , "w-100" , "overflow-y-auto");
    //  alert("after emptying container")

    let num_res = arrayResults.length;
    //  alert("num_res : " + num_res);

    for (let i = 0; i < num_res ; i++) {

        let number_i = parseInt(i);
        //  alert("got the result : " + JSON.stringify(arrayResults[number_i])  )

        let myMap = new Map(Object.entries(JSON.parse(JSON.stringify(arrayResults[number_i]))));

        let this_imgURL = myMap.get('imgURL') ;
        let this_animeTitle =  myMap.get('title') ;
        let this_averageScore =  myMap.get('averageScore') ;

        //  alert(this_imgURL) ;
        //  alert(this_animeTitle) ;
        //  alert(this_averageScore) ;

        let trg_aniDiv = document.createElement("div");
        trg_aniDiv.classList.add("d-flex" , "top-container" , "align-items-center");

        //  alert("doing img")
        //  let trg_aniIMG = document.createElement("img");
        //  trg_aniIMG.attr("src", this_imgURL);
        //  trg_aniDiv.appendChild(trg_aniIMG);
        //  <img class="img_resize" style="margin: 8px; padding: 4px" th:src="${anime_r.imgURL}" alt="anime_image">

        let this_trg_IMG = new Image();
        this_trg_IMG.src = this_imgURL;
        this_trg_IMG.style = "margin: 8px; padding: 4px";
        this_trg_IMG.className = "img_resize";
        trg_aniDiv.appendChild(this_trg_IMG);


        let trg_aniInfo = document.createElement("div");
        trg_aniDiv.appendChild(trg_aniInfo);

        //  alert("doing title")

        let trg_atitle = document.createElement("h3");
        trg_atitle.innerText = this_animeTitle;
        trg_atitle.style= "font-weight: bold; margin-bottom: 0";
        trg_aniInfo.appendChild(trg_atitle);

        //  alert("doing score")

        let trg_score = document.createElement("h5");
        trg_score.innerText = "average score : " + this_averageScore;
        trg_score.style= "font-weight: bold; margin-bottom: 0";
        trg_aniInfo.appendChild(trg_score);

        //  alert("making url link ")

        trg_aniDiv.onclick = function () {
            window.location.href = '/animeDetailsPage?title=' + this_animeTitle;
        }

        trg_container.appendChild(trg_aniDiv);
    }
}



//  function displayAnime(anime_lst) {
//      const container = $(".afpResults_container");
//      container.empty();
//
//      anime_lst.forEach(function (anime) {
//
//          let animeDiv = $("<div id=\"anime_info\" class=\"d-flex flex-column top-container gap-1\"></div>");
//          //  animeDiv.append(  $("<div class=\"d-flex top-container align-items-center\"></div>") );
//          animeDiv.append(  $("<img id=\"anime_cover\" class=\"top-cover p-1\"/>").attr("src", anime.imgURL)  );
//          let animeInf = $("<div id=\"anime_details\" class=\"anime-details-sm d-flex flex-column mt-1\"></div>");
//          animeDiv.append(animeInf);
//          animeInf.append($("<h4 id=\"anime_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(anime.title));
//          let div1 = $("<div class=\"d-flex m-0\"></div>");
//          animeInf.append(div1);
//          div1.append($("<p style=\"font-size: medium;\" id=\"anime_score\"></p>").text(anime.averageScore ));
//          animeDiv.click(function () {
//              window.location.href = '/animeDetailsPage?title=' + anime.title;
//          });
//          container.append(animeDiv);
//
//      });
//  }


//  //  //  OLD SOLUTION :
//  //  let AanimeDiv = $("<div id=\"anime_info\" class=\"d-flex mb-1 align-items-center\"></div>");
//  //  AanimeDiv.append($("<img id=\"anime_cover\" class=\"anime-cover-sm mt-1 mb-1\">").attr("src", anime.coverURL));
//  //  let AanimeInf = $("<div id=\"anime_details\" class=\"anime-details-sm d-flex flex-column mt-1\"></div>");
//  //  AanimeDiv.append(animeInf);
//  //  AanimeInf.append($("<h4 id=\"anime_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(anime.title));
//  //  let Adiv1 = $("<div class=\"d-flex m-0\"></div>");
//  //  AanimeInf.append(div1);
//  //  Adiv1.append($("<p style=\"font-size: medium;\" id=\"anime_score\"></p>").text(anime.averageScore));
//  //  AanimeDiv.click(function () {
//  //      window.location.href = '/animeDetails?animeID=' + anime.id;
//  //  });
//  //  container.append(AanimeDiv);


//  //  //  <a href="'/animeDetails?title=' + ${anime_r.title}" className="d-flex flex-column top-container gap-1">
//  //  //      <div className="d-flex top-container align-items-center">
//  //  //          <img className="top-cover p-1" th:src="${anime_r.imgURL}" alt="anime_image"/>
//  //  //          <div className="d-flex flex-column">
//  //  //              <h5 className="mb-0" th:text="${anime_r.title}"></h5>
//  //  //              <p className="mt-0 mb-0" style="color: white;"><i className="fa fa-star me-1"></i><span>${anime_r.averageScore}</span></p>
//  //  //          </div>
//  //  //      </div>
//  //  //  </a>

//  //  //  <a th:each="anime_r : ${rankingAnimeByRating_AllTime}" th:href="'/animeDetails?title=' + ${anime_r.title}" class="d-flex flex-column top-container gap-1">
//  //  //      <div class="d-flex top-container align-items-center">
//  //  //          <img class="top-cover p-1" th:src="${anime_r.imgURL}" alt="anime_image">
//  //  //              <div class="d-flex flex-column">
//  //  //                  <h5 class="mb-0" th:text="${anime_r.title}"></h5>
//  //  //                  <p class="mt-0 mb-0" style="color: white;"><i class="fa fa-star me-1"></i><span th:text="${anime_r.averageScore}"></span></p>
//  //  //              </div>
//  //  //      </div>
//  //  //  </a>