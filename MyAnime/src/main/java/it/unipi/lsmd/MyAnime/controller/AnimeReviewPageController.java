package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;
import java.util.List;

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
public class AnimeReviewPageController {
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    ReviewRepoMongoDB reviewRepoMongoDB;

    @GetMapping(value={"/animeReviews","/animeReviewsPage"})
    public String animeReviews(HttpSession session,
                                 Model model,
                                 @RequestParam("animeId") String animeId) {

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
            }
            else {
                model.addAttribute("animeId", animeId);
                return "error/noReviewsFound";
            }
        }
        else{
            return "error/animeNotFound";
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
        model.addAttribute("admin", (Utility.isAdmin(session)) ? true : false);

        return "animeReviews";
    }

}
