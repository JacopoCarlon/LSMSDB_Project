package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserReviewsPageController {

    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    ReviewRepoMongoDB reviewRepoMongoDB;

    @GetMapping(value={"/userReviews.html","/userReviewsPage.html","/userReviews","/userReviewsPage"})
    public String animeReviews(HttpSession session,
                               Model model,
                               @RequestParam("username") String username) {

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }
        boolean userFound = userRepoMongoDB.existsByUsername(username);
        if(userFound){
            List<Review> reviewList = reviewRepoMongoDB.getReviewsByUsername(username);
            LinkedList<Review> reviews = new LinkedList<>(reviewList);

            boolean reviewsFound = (reviews != null && !reviews.isEmpty());
            if (reviewsFound) {
                for (Review review : reviews) {
                    review.setPrintableDate();
                }
                model.addAttribute("reviews", reviews);
                model.addAttribute("username", username);
            }
        }
        else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        return "userReviewsPage";

    }
}