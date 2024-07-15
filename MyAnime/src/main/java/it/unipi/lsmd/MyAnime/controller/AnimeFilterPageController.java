package it.unipi.lsmd.MyAnime.controller;


import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AnimeFilterPageController {

    @RequestMapping(value={"/animeFilter.html","/animeFilterPage.html","/animeFilter","/animeFilterPage"})
    public String animeFilterPage(HttpSession session,
                                  Model model) {
        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }
        else {
            return "animeFilterPage";
        }
    }
}





