package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class AnimeReviewsPageController {

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    ReviewRepoMongoDB reviewRepoMongoDB;

    @GetMapping(value={"/animeReviews.html","/animeReviewsPage.html","/animeReviews","/animeReviewsPage"})
    public String animeReviews(HttpSession session,
                                 Model model,
                                 @RequestParam("animeId") ObjectId animeId) {

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }

        boolean animeFound = animeRepoMongoDB.existsById(animeId);
        if(animeFound){
            List<Review> reviewList = reviewRepoMongoDB.getReviewsByAnimeID(animeId);
            LinkedList<Review> reviews = new LinkedList<>(reviewList);
            boolean reviewsFound = (reviews != null && !reviews.isEmpty());
            if (reviewsFound){

                // if the user has already reviewed the anime, put his review as first in the list
                if(Utility.isLogged(session) && !Utility.isAdmin(session)){
                    String username = (String) session.getAttribute("username");
                    for (Review review : reviews) {
                        if(review.getUsername().equals(username)){
                            reviews.remove(review);
                            reviews.add(0, review);
                            break;
                        }
                    }
                }

                for (Review review : reviews) {
                    review.setPrintableDate();
                }
                model.addAttribute("reviews", reviews);

                // TODO : make sure this works
                model.addAttribute("animeDetails", animeRepoMongoDB.getAnimeById(animeId));
            }
            else {
                model.addAttribute("animeId", animeId);
                return "error/noReviewsFound";
            }
        }
        else{
            return "error/animeNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        return "animeReviews";
    }

}
