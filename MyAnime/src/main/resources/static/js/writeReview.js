$(document).ready(function () {
    $('#sendReview_btn').click(function (e) {
        e.preventDefault();

        // Take the data from the form
        const rating = $('#rating_input').val();
        const text = $('#text_review_input').val();
        const animeID = /*[[${animeId}]]*/ null;            // Id of the anime insert with Thymeleaf, taken from the controller
        const username = /*[[${session.username}]]*/ null;  // Username of the logged user insert by HttpSession
        const isAdmin = /*[[${session.isAdmin}]]*/ false;   // Value insert by HttpSession to understand if the logged user is admin

        // Check if all fields have been filled in
        if (!rating || !text) {
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
        if(!animeID) {
            alert("An error occurred while loading the page.");
            return;
        }

        // Prepare the data to be sent
        const formData = {
            rating: rating,
            text: text,
            animeID: animeID,
            username: username
        };

        // Send the request to the server
        $.ajax({
            url: '/api/writeReview',
            data: formData,
            dataType: 'json',
            method: 'POST',

            success: function(response) {
                handleOutcome(response.outcome_code);
            },
            error: function (xhr, status, error) {
                alert("ERROR: " + error);
            }
        });
    });
});

function handleOutcome(outcomeCode) {
    switch(outcomeCode) {
        case 0:
            alert('Review written successfully');
            window.location.href = '/animeDetails?animeId=' + /*[[${animeId}]]*/ null;
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
            alert("Rating out of range.");
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