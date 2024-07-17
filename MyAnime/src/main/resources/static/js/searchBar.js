$(document).ready(function(){
    //  alert("begin on ready serachbar")

    //  let value = $("#is_logged").val()
    //  if(value == 'true')
    //      categories = ['Anime', 'User']
    //  else
    //      categories = ['Anime', 'User']
    categories = ['Anime', 'User']
    //  alert("loading search bar")
    loadSearchBar()
    for(a in categories) {
        $("#category_input").append(
            new Option(categories[a], categories[a].toLowerCase())
        )
    }
    setActiveLink();
})
function loadSearchBar(){

    let value = $("#magic_is_logged").text();
    let admin = $("#magic_is_admin").text();
    //  alert("magicislogged : " + magicvalue)
    //  alert("magicisADMIN : " + magicadmin)
    //  let old_value = $("#is_logged").val();
    //  let old_admin = $("#is_admin").val();
    //  alert("value : " + old_value)
    //  alert("admin : " + old_admin)

    //  value = 'true'
    //  admin = 'false'
    $(".header").append(
        "<div class=\"container-fluid d-flex align-items-center justify-content-between\">" +
        "<h1 id=\"logo\" class=\"g-col-3\"><a href=\"/mostPopularPage\">MyAnimeLibrary</a></h1>" +
        "<section id=\"search-container\" class=\"w-50\">" +
        "<form class=\"d-flex\">" +
        "<input class=\"form-control\" type=\"text\" id=\"search_input\" placeholder=\"What are you looking for?\">" +
        "<select name=\"category\" id=\"category_input\"></select>" +
        "<button class=\"btn btn-success\" id=\"search_btn\"  data-bs-target=\"#search_results\" type=\"button\">Search</button>" +
        "</form>" +
        "</section>" +
        "<nav id=\"navbar\" class=\"navbar justify-content-center g-col-3\">" +
        "<ul class=\"nav nav-underline\">" +
        "<li id=\"home_controller\" class=\"nav-item\" role=\"presentation\">" +
        //  "<a class=\"nav-link active\" href=\"/homePage\">Home</a>" +
        "</li>" +
        "<li id=\"discover_controller\" class=\"nav-item\" role=\"presentation\">" +
        "</li>" +
        "<li id=\"user_controller\" class=\"nav-item\">" +
        "</li>" +
        "<li id=\"logout_btn\" class=\"nav-item\">" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</div>"
    )
    if(value == 'true'){
        //  alert("value : " + value)
        //  alert("admin : " + admin)
        //  alert("admin==true : " + (admin==true))
        //  alert("admin==str.true : " + (admin=='true'))
        //  js bad sadge

        if (admin == 'true'){
            //  alert("doing admin only part")
            $("#user_controller").empty()
            let container = $("#home_controller")
            container.empty()
            container.append($("<a class=\"nav-link active\" href=\"/adminPage\">Dashboard</a>"))
        }
        else{
            //  alert("doing user only part")
            $("#discover_controller").append(
                "<a class=\"nav-link scrollto\" href=\"/discoverPage\">Discover</a>"
            )
            $("#user_controller").append(
                "<a href=\"/profilePage\" class=\"btn btn-success\" style=\"border-radius: 10px 10px 10px 10px;\"><i class=\"fa fa-user me-1\"></i>Profile page</a>"
            )
            
        }

        $("#user_controller").append(
            "<button class=\"btn btn-danger\" id=\"logout_btn\" style=\"border-radius: 10px 10px 10px 10px;\" type=\"button\">Logout</button>"
        )
    }


}
function setActiveLink() {
    // Get the current URL
    var currentUrl = window.location.href;

    // Select all nav-link elements
    var navLinks = document.querySelectorAll('.nav-link');

    // Iterate over each nav-link
    navLinks.forEach(function(navLink) {
        // Remove the active class from all nav-links
        navLink.classList.remove('active');

        // Check if nav-link's href matches the current URL
        if (currentUrl.includes(navLink.getAttribute('href'))) {
            // Add the active class to the matching nav-link
            navLink.classList.add('active');
        }
    });
}