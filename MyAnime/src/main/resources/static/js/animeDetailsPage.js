
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
                num_episodes    :   episodes_val,
                animeTitle      :   forceAnimeTitle
            },

            success: function(response) {
                switch(response.outcome_code){
                    case 0:
                        alert("Episode count updated to : " + episodes_val);
                        break;
                    case 1:
                        alert("You must be logged to do this!");
                        break;
                    case 2:
                        alert("Admins account cannot do this!");
                        break;
                    case 3:
                        alert("Invalid number of episodes");
                        break;
                    case 4:
                        alert("Selected anime does not exists");
                        break;
                    case 5:
                        alert("Anime must be added to a list first");
                        break;
                    case 6:
                    case 7:
                        alert("Error in database access");
                        break;
                    case 12:
                        alert("Error while connecting to database");
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
        let trg_name = "setAnimeLstn_radio"
        let ele = document.getElementsByName(trg_name);

        let trg_list = null;
        let list_name = null;
        for (i = 0; i < ele.length; i++) {
            if (ele[i].checked){
                trg_list = ele[i].id.toString()[4];
                list_name = ele[i].value.toString();
                break;
            }
        }

        if (trg_list == null) {
            alert("requested list not found")
            return;
        }


        $.ajax({
            url: '/api/setAnimeToList',
            dataType: 'json',
            method: 'POST',

            data:   {
                animeTitle :   forceAnimeTitle,
                targetList  :   trg_list
            },

            success: function(response) {
                switch(response.outcome_code){
                    case 0:
                        alert("Anime added to list \'" + list_name +"\'");
                        break;
                    case 1:
                        alert("You must be logged to do this!");
                        break;
                    case 2:
                        alert("Admins account cannot do this!");
                        break;
                    case 3:
                        alert("Selected list does not exists");
                        break;
                    case 4:
                        alert("Selected anime does not exists");
                        break;
                    case 5:
                        alert("Anime was already in list \'" + list_name + "\'");
                        break;
                    case 12:
                        alert("Error while connecting to database");
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