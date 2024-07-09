package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
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

    @GetMapping("/animeDetailsPage")
    public String animeDetailsPage(HttpSession session,
                               Model model,
                               @RequestParam(required = false) String animeID,
                               @RequestParam(required = false) String title) {

        Anime anime;
        return "";
    }
}
