package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnimeDetailsPageController {

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;

    @GetMapping("/animeDetails")
    public String animeDetailsPage(HttpSession session,
                               Model model,
                               @RequestParam(required = false) String animeID,
                               @RequestParam(required = false) String title) {

        Anime anime;

        if (animeID!= null) {
            anime = animeRepoMongoDB.getAnimeById(animeID);
        } else if (title!=null) {
            anime = animeRepoMongoDB.getAnimeByTitle(title);
        } else {
            return "error/animeNotFound";
        }

        if (anime == null) {
            return "error/animeNotFound";
        }

        model.addAttribute("anime", anime);
        model.addAttribute("is_logged", Utility.isLogged(session));

        return "animeDetailsPage";
    }
}
