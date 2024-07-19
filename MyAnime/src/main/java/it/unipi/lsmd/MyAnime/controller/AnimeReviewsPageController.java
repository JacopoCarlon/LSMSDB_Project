package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;

import it.unipi.lsmd.MyAnime.model.Anime;
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
                               @RequestParam(required = false) ObjectId animeId,
                               @RequestParam(required = false) String animeTitle) {



        System.out.println("entered in animeRevPage with animeID : " + animeId + " or animeTitle : " + animeTitle);

         if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }


        Anime anime;

        if (animeId != null && animeRepoMongoDB.existsById(animeId)) {
            anime = animeRepoMongoDB.getAnimeById(animeId);
        }
        else if (animeTitle != null && animeRepoMongoDB.existsByTitle(animeTitle)) {
            anime = animeRepoMongoDB.getAnimeByTitle(animeTitle);
        }
        else {
            // anime not found
            //  return "error/genericError";
            return "error/animeNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("animeDetails", anime);

        System.out.println(anime.getId());
        LinkedList<Review> reviews = new LinkedList<>(reviewRepoMongoDB.getReviewsByAnimeID(anime.getId()));
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

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        return "animeReviewsPage";
    }

}
