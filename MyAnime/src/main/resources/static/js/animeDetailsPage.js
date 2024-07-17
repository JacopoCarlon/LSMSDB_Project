
//  TODO : fix this buttons please, need to interact proprly con DB for details review and animeID
//  TODO : do API corresponding
$(document).ready(function () {

    // TODO : it would be much better to get id or title from here instead of from url ...
    //  if done, please also change the href... in this page

    //  //  TODO : like any one of these would be saner, but none work, they just return "... : [object Object]"


    //  TODO : this is not exactly the most safe way to do this, but it works enough
    let forceAnimeTitle = new URLSearchParams(window.location.search).get("title");
    //  alert("found forceAnimeTitle : " + forceAnimeTitle);
    if(forceAnimeTitle == null ){
        alert("no anime id nor title");
        return;
    }

    //  alert("doing viewAnimeReviews_btn")
    let viewAllReviewBtn = $("#viewAnimeReviews_btn");
    if (viewAllReviewBtn != null) {
        //  alert("done viewAnimeReviews_btn")
        $(viewAllReviewBtn).click(function (){
            window.location.href = '/animeReviewsPage?animeTitle=' + forceAnimeTitle;
        });
    } else{
        alert("viewAnimeReviews_btn not found");
    }





    //  alert("doing writeReview_btn")
    let writeReviewBtn = $("#writeReview_btn");

    if (writeReviewBtn !== null) {
        //  alert("done writeReview_btn")
        $(writeReviewBtn).click(function (){
            window.location.href = '/writeReviewPage?animeTitle=' + forceAnimeTitle;
        });
    } else{
        alert("writeReviewBtn not found");
    }




    //  alert("magic-logged ? : " + $('#magic-logged').text());
    //  alert("magic-is_admin? : " + $('#magic-is_admin').text());

    $("#setAnimeWatchedEp_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();

        const episodes_val = parseInt( $("#userNumWatchedEpisodes_input").val() ) ;

        const maxEp_valtext = parseInt( $("#numEpMaxThisAnime").text() ) ;
        //  const maxEp_valval = $("#numEpMaxThisAnime").val();

        //  alert("declared number : " + episodes_val);
        //  alert("declared number type: " + typeof(episodes_val) );
        //  alert("max00 number : " + maxEp_valtext);
        //  alert("max00 number type:" + typeof(maxEp_valtext) );

        if(episodes_val > maxEp_valtext){
            alert("you cannot possibly have watched these many episodes of this anime.")
            return false;
        }


        $.ajax({
            url: '/api/setUserAnimeWatchedEpisodes',
            dataType: 'json',
            method: 'POST',

            data:   {
                targetList :    episodes_val,
                animeToList :   forceAnimeTitle
            },

            success: function(response) {
                switch(response.outcome_code){
                    case 0:
                        alert("episode count updated to : " + trg_arr);
                        break;
                    default:
                        alert("Unknown (magic) error.");
                }
            },
            error: function (xhr, status, error) {
                alert("ERROR in ajax: " + error);
            }
        });

        return false;
    })








    $("#doSetAnimeList_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
        setAnimeToList();
        return false;
    })

    function setAnimeToList(){
        //  alert("enter in function")
        let trg_name = "setAnimeLstn_radio"
        //  alert("looking for name : " + trg_name)

        let ele = document.getElementsByName(trg_name);
        //  alert("found elements : " + ele)
        //  alert("number of found elements : " + ele.length)

        let trg_arr = [];
        for (i = 0; i < ele.length; i++) {
            if (ele[i].checked){
                trg_arr.push(ele[i].id.toString());
            }
        }
        //  alert("found ; " + trg_arr);

        $.ajax({
            url: '/api/setAnimeToList',
            dataType: 'json',
            method: 'POST',

            data:   {
                targetList :    trg_arr,
                animeToList :   forceAnimeTitle
            },

            success: function(response) {
                switch(response.outcome_code){
                    case 0:
                        alert("anime added to the list : " + trg_arr);
                        break;
                    case 1:
                        alert("anime was already in the list : " + trg_arr);
                        break;
                    default:
                        alert("Unknown (magic) error.");
                }
            },
            error: function (xhr, status, error) {
                alert("ERROR in ajax: " + error);
            }
        });
    }
});