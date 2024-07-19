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
        //  alert("trg_arr for name " + trg_name + "is : " + trg_arr);
        return trg_arr;
    }


    function adminUploadAnime(){
        //  //  const isAdmin = /*[[${is_admin}]]*/ false;
        //  //  const isLogged = /*[[${logged}]]*/ false;
        //  //  alert("isAdmin ? : " + isAdmin);
        //  //  alert("isLogged? : " + isLogged);

        //  alert("magic-logged ? : " + $('#magic-logged').text());
        //  alert("magic-is_admin? : " + $('#magic-is_admin').text());

        const title_val = $("#a_title_input").val();
        const titleJapanese_val = $("#a_titleJapanese_input").val();
        const source_val = $("#a_source_input").val();
        const episodes_val = $("#a_episodes_input").val();
        //  const airing_val = $("#a_airing_input").val();      // true/false
        const slider_airing_val = $('#a_airing_input').is(":checked")
        //  const aired_val = $("#a_aired_input").val();
        const aired_input_from_val = $("#a_aired_input_from").val();
        const aired_input_to_val = $("#a_aired_input_to").val();
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
        //  alert("slider_airing_val : " + slider_airing_val);

        //  alert(aired_input_from_val)
        //  alert(aired_input_to_val)
        //  alert(!aired_input_from_val)
        //  alert(!aired_input_to_val)
        // if(!aired_input_from_val !== !aired_input_to_val){
        //    alert("if you put one date, you must put both !!!!!!!!!!!!!!!")
        //    return;
        //}
        if(aired_input_from_val > aired_input_to_val){
            alert("anime cannot time travel, check order of airing(from-to) dates")
            return;
        }

        //  alert(episodes_val)
        if(episodes_val <0){
            // or 1 minimum (maybe future anime has zero idk)
            alert("anime shall have at the least one episode ..?")
            return;
        }

        if(!title_val || !source_val || !imgURL_val ){
            alert("title, source and imgURL are obligatory fields !!!");
            return;
        }

        let trgFilterNames = [ "type_cbox", "rating_cbox", "genre_cbox" ]
        let filters = [];
        for (i_name in trgFilterNames) {
            filters.push(getChosenByName(trgFilterNames[i_name]) );
        }

        //  let lis = document.getElementById("addedRelationsList").getElementsByTagName("li")
        // alert("begin extracting relations")
        const addRelList = Array.from(document.querySelectorAll('ul#addedRelationsList li div'));
        let parity = 0;
        let relations = [];
        for (const this_li of addRelList){
            //  alert("thisli : " + this_li);
            //  alert(this_li.textContent)
            relations.push( this_li.textContent );
        }
        if (filters[2].length <1){
            alert("you shall not push an anime with no genre !!!");
            return;
        }

        const formData = {
            title_val               : title_val,
            titleJapanese_val       : titleJapanese_val,
            source_val              : source_val,
            episodes_val            : episodes_val,
            slider_airing_val       : slider_airing_val,
            aired_input_from_val    : aired_input_from_val,
            aired_input_to_val      : aired_input_to_val,
            background_val          : background_val,
            broadcast_val           : broadcast_val,
            producer_val            : producer_val,
            licensor_val            : licensor_val,
            studio_val              : studio_val,
            EpisodeDuration_val     : EpisodeDuration_val,
            imgURL_val              : imgURL_val,
            type_list               : filters[0],
            rating_list             : filters[1],
            genre_list              : filters[2],
            relations_list          : relations
        };

        // alert("about to ajax request to upload anime")
        $.ajax({
            url: '/api/adminAnimeUpload',
            data: formData,
            dataType: 'json',
            method: 'POST',

            success: function(response) {
                switch(response.outcome_code) {
                    case 0:
                        alert("Anime successfully uploaded !");
                        break;
                    case 1:
                        alert("Missing information");
                        break;
                    case 2:
                        alert("Not logged as Admin");
                        break;
                    case 3:
                        alert("Image URL not valid");
                        break;
                    case 4:
                        alert("Related Anime does not exists");
                        break;
                    case 5:
                        alert("MongoDB failure");
                        break;
                    case 6:
                        alert("Neo4j Failure");
                        break;
                    case 7:
                        alert("Error connecting to database");
                        break;
                    default:
                        alert("Unexpected error");
                }
            },
            error: function (xhr, status, error) {
                alert("ERROR in ajax: " + error);
            }
        });

        return;
    }
});




