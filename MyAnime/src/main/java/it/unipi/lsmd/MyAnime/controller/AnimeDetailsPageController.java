package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnimeDetailsPageController {

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    AnimeRepoNeo4j animeRepoNeo4j;

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
        System.out.println("Anime: " + anime);

        model.addAttribute("animeRelated", animeRepoNeo4j.findRelatedAnime(anime.getTitle()));
        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        return "animeDetailsPage";
    }
}
