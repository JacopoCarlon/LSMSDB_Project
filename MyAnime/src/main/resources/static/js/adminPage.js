$(document).ready(function (){
    /*
    $("#calculateStatsButton").click(function () {
        //  e.preventDefault();
        alert("linking to adminStats")
        window.location.href = '/adminStats';
    });
    */

    // update files for mostPopularPage
    $("#calculateRankingsButton").click(function () {
        alert("Start calculating rankings...");
        $.ajax({
            url: '/api/admin/calculateRankings',
            method: 'POST',
            dataType: 'json',
            success: function (data) {
                alert("we were given the outcome : " + data.outcome_code);
                switch (data.outcome_code) {
                    case 0:
                        alert('Ranking update successful!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        alert('No rankings data found.');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    case 11:
                        alert('Error while writing to file.');
                        break;
                    case 12:
                        alert('Error while clearing rankings directory.');
                        break;
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        alert("Other generic errors");
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            }
        });
    });



    $("#updateAvgScores").click(function () {
        alert("Start updating scores...");
        var popup = document.getElementById("myPopup");
        popup.classList.toggle("show");
        $.ajax({
            url: '/api/admin/updateWatchersAndScores',
            method: 'POST',
            dataType: 'json',
            success: function (data) {
                //  alert(data.outcome_code);
                popup.classList.toggle("show");

                switch (data.outcome_code) {
                    case 0:
                        alert('Update successful!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 2:
                        alert('Error while updating new scores (for animes).');
                        break;
                    case 3:
                        alert('Generic updating error).');
                        break;
                    case 4:
                        alert('Error while updating average score.');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            }
        });
    });




    $("#most_popular_animes").click(function (e){
        e.preventDefault();
        window.location.href = '/mostPopularPage?type=animes';
    });

    $("#addNewAnime_btn").click(function (e){
        e.preventDefault();
        window.location.href = '/adminAddAnimePage';
    });

})