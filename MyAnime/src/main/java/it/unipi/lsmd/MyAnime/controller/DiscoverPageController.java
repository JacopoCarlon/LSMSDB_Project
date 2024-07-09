package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.AnimeNode;
import it.unipi.lsmd.MyAnime.model.UserNode;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoNeo4j;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiscoverPageController {
    @Autowired
    UserRepoNeo4j userRepoNeo4j;
    @Autowired
    AnimeRepoNeo4j animeRepoNeo4j;

    @RequestMapping("/discoverPage")
    public String discoverPage(HttpSession session, Model model) {
        return "";
    }
}
