package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MostPopularPageController {

    @RequestMapping("/mostPopularPage")
    public String mostPopularPage(HttpSession session,
                                  Model model) {

        return "";
    }
}
