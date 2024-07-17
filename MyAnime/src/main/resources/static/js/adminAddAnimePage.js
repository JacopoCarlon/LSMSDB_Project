// used for loginPage
//  TODO : unrelativize paths
//  needs : controllers/api/...rest.java che abbia @PostMapping("/api/login") e mi ritorni outcome_code : 0
$(document).ready(function () {
    $("#anime_upload_btn").click(function (e) {
        e.preventDefault();
        adminUploadAnime();
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
        alert("trg_arr for name " + trg_name + "is : " + trg_arr);
        return trg_arr;
    }


    function adminUploadAnime(){
        //  //  const isAdmin = /*[[${is_admin}]]*/ false;
        //  //  const isLogged = /*[[${logged}]]*/ false;
        //  //  alert("isAdmin ? : " + isAdmin);
        //  //  alert("isLogged? : " + isLogged);

        alert("magic-logged ? : " + $('#magic-logged').text());
        alert("magic-is_admin? : " + $('#magic-is_admin').text());

        const title_val = $("#a_title_input").val();
        const titleJapanese_val = $("#a_titleJapanese_input").val();
        const source_val = $("#a_source_input").val();
        const episodes_val = $("#a_episodes_input").val();
        //  const airing_val = $("#a_airing_input").val();      // true/false
        const slider_airing_val = $('#a_airing_input').is(":checked")
        const aired_val = $("#a_aired_input").val();
        //  //  const averageScore_val = $("#a_averageScore_input").val();
        //  //  const scoredBy_val = $("#a_scoredBy_input").val();
        //  //  const watchers_val = $("#a_watchers_input").val();
        const background_val = $("#a_background_input").val();
        const broadcast_val = $("#a_broadcast_input").val();
        const producer_val = $("#a_producer_input").val();
        const licensor_val = $("#a_licensor_input").val();
        const studio_val = $("#a_studio_input").val();
        const EpisodeDuration_val = $("#a_EpisodeDuration_input").val();
        const imgURL_val = $("#a_imgURL_input").val();

        //  alert("airing_val : " + airing_val);
        alert("slider_airing_val : " + slider_airing_val);

        if(!title_val || !source_val  || !imgURL_val ){
            alert("title, source, airing and imgurl are obbligatory fields !!!");
            return;
        }








        let trgFilterNames = [ "type_cbox", "rating_cbox", "genre_cbox" ]
        let filters = [];
        for (i_name in trgFilterNames) {
            filters.push(getChosenByName(trgFilterNames[i_name]) );
        }

        return;
    }





});

