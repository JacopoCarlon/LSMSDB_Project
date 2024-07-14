package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//  import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnimeDetailsPageController {

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;

    @RequestMapping(value={"/animeDetails.html","/animeDetailsPage.html","/animeDetails","/animeDetailsPage"})
    public String animeDetailsPage(HttpSession session,
                               Model model,
                               @RequestParam(required = false) ObjectId animeId,
                               @RequestParam(required = false) String title) {

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }

        Anime anime;

        if (animeId!= null) {
            anime = animeRepoMongoDB.getAnimeById(animeId);
        } else if (title!=null) {
            //  System.out.println("Anime: "+title);
            anime = animeRepoMongoDB.getAnimeByTitle(title);
        } else {
            return "error/animeNotFound";
        }

        if (anime == null) {
            return "error/animeNotFound";
        }

        model.addAttribute("animeDetails", anime);
        model.addAttribute("is_logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isLogged(session));

        return "animeDetailsPage";
    }
}
