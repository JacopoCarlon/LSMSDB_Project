
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
    } else
        alert("writeReviewBtn not found");

});