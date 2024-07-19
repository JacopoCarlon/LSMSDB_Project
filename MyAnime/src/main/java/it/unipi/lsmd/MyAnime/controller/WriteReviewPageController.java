package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import jdk.jshell.execution.Util;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WriteReviewPageController {

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;

    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @GetMapping(value={"/writeReview.html","/writeReviewPage.html","/writeReview","/writeReviewPage"})
    public String writeReview(HttpSession session,
                              Model model,
                              @RequestParam(required = false) ObjectId animeId,
                              @RequestParam(required = false) String animeTitle) {

        System.out.println("entered in animeRevPage with animeID : " + animeId + " or animeTitle : " + animeTitle);

        String username = Utility.getUsername(session);

        if(!Utility.isLogged(session)){
            return "error/mustBeLogged";
        }
        if(Utility.isAdmin(session)){
            return "error/accessDenied";
        }

        Anime animeDetails;
        if (animeId != null && animeRepoMongoDB.existsById(animeId)) {
            animeDetails = animeRepoMongoDB.getAnimeById(animeId);
        }
        else if (animeTitle != null && animeRepoMongoDB.existsByTitle(animeTitle)) {
            animeDetails = animeRepoMongoDB.getAnimeByTitle(animeTitle);
        }
        else {
            // anime not found
            return "error/animeNotFound";
        }

        // anime found
        //  User user = userRepoMongoDB.getUserByUsername(username);
        //  model.addAttribute("userDetails", user);
        model.addAttribute("username", Utility.getUsername(session));
        model.addAttribute("animeDetails", animeDetails);
        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        System.out.println("username: " + username);

        if(Utility.isLogged(session))
            return "writeReviewPage";
        else
            return "error/mustBeLogged";
    }
}
