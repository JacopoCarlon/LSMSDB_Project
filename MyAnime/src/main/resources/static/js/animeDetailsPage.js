
//  TODO : fix this buttons please, need to interact proprly con DB for details review and animeID
//  TODO : do API corresponding
$(document).ready(function () {

    // TODO : it would be much better to get id or title from here instead of from url ...
    //  if done, please also change the href... in this page

    //  //  TODO : like any one of these would be saner, but none work, they just return "... : [object Object]"
    //  let animeId = $("#animeDetails.id");
    //  alert("found this animeID : " + animeId);
    //  let animeTitle = $("#animeDetails.title");
    //  alert("found this animeTitle : " + animeTitle);
    //  let js_animeId = $("#animeId");
    //  alert("found this js_animeId : " + js_animeId);
    //  let js_animeTitle = $("#animeTitle");
    //  alert("found this js_animeTitle : " + js_animeTitle);



    //  let animeId = /*[[${animeDetails.id}]]*/ null;
    //  let animeTitle = /*[[${animeDetails.title}]]*/ null;
    //  let logged = /*[[${logged}]]*/ true;
    //  let username = /*[[${session.username}]]*/ null;

    //  TODO : this is not exactly the most safe way to do this, but it works enough
    let forceAnimeTitle = new URLSearchParams(window.location.search).get("title");
    alert("found forceAnimeTitle : " + forceAnimeTitle);
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