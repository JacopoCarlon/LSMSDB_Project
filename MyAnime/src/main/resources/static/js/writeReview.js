$(document).ready(function () {
    $('#sendReview_btn').click(function (e) {
        e.preventDefault();
        let animeTitle = new URLSearchParams(window.location.search).get("animeTitle");
        // Take the data from the form
        const score = $('#score_input').val();
        const text = $('#text_review_input').val();

        //  const username = /*[[${username}]]*/ null;  // Username of the logged user insert by HttpSession
        const isAdmin = /*[[${is_admin}]]*/ false;   // Value insert by HttpSession to understand if the logged user is admin

        //alert($('#this_username').text())

        let username = $('#this_username').text()
        let magictitle = $("#anime_title").text();
        //alert("magictitle : " + magictitle);

        // Check if all fields have been filled in
        if (!score || !text) {
            alert("Please fill in all fields.");
            return;
        }
        if(!username) {
            alert("You must be logged in to write a review.");
            return;
        }
        if(isAdmin) {
            alert("Admins cannot write reviews.");
            return;
        }
        if(!animeTitle) {
            alert("An error occurred while loading the page.");
            return;
        }

        // Prepare the data to be sent
        const formData = {
            score: score,
            text: text,
            animeTitle: animeTitle,
            username: username
        };

        //alert("before ajax in writerev")
        // Send the request to the server
        $.ajax({
            url: '/api/writeReview',
            data: {
                score: score,
                text: text,
                animeTitle: animeTitle,
                username: username
            },
            dataType: 'json',
            method: 'POST',

            success: function(response) {
                alert("success ajax writerev")
                handleOutcome(response.outcome_code, animeTitle);
            },
            error: function (xhr, status, error) {
                alert("ERROR: " + error);
            }
        });
    });
});

function handleOutcome(outcomeCode, animeTitle) {
    //alert("enter handleoutcome")
    switch(outcomeCode) {
        case 0:
            alert('Review written successfully');
            window.location.href = '/animeDetails?title=' + animeTitle;
            break;
        case 1:
            window.location.href = '/youMustBeLogged';
            break;
        case 2:
            alert("Usernames don't match.");
            break;
        case 3:
            window.location.href = '/animeNotFound';
            break;
        case 4:
            alert("Score out of range.");
            break;
        case 5:
            alert("User has already written a review for this anime.");
            break;
        case 6:
            alert("Error while writing the review into the collection reviews.");
            break;
        case 7:
            alert("Error while writing the review into the collection users.");
            break;
        case 8:
            window.location.href = '/animeNotFound';
            break;
        case 9:
            alert("Violation of uniqueness constraint.");
            break;
        case 10:
            alert("Violation of data integrity.");
            break;
        case 11:
            alert("Other exceptions related to data access.");
            break;
        case 12:
            alert("Error while connecting to the database.");
            break;
        default:
            alert("Unexpected outcome.");
            break;
    }
}