package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WriteReviewPageController {

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;

    @GetMapping("/writeReview")
    public String writeReview(HttpSession session,
                              Model model,
                              @RequestParam("animeID") String animeID) {

        if(!Utility.isLogged(session))
            return "error/mustBeLogged";
        if(Utility.isAdmin(session))
            return "error/accessDenied";

        boolean animeFound = animeRepoMongoDB.existsById(animeID);
        if(animeFound)
            model.addAttribute("animeId", animeID);
        else
            return "error/animeNotFound";

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("isAdmin", Utility.isAdmin(session));

        if(Utility.isLogged(session))
            return "writeReview";
        else
            return "error/mustBeLogged";
    }
}
