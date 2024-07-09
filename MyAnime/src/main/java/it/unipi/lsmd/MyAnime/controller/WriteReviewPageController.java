package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
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
                              @RequestParam("animeId") String animeID) {

    return "";
    }
}
