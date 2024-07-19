package it.unipi.lsmd.MyAnime.controller;

import java.util.List;

import it.unipi.lsmd.MyAnime.model.query.AnimeWatched;
import it.unipi.lsmd.MyAnime.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserAnimeListController {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    UserRepoNeo4j userRepoNeo4j;

    @GetMapping(value={"userAnimeList","userAnimeListPage","userAnimeList.html","userAnimeListPage.html"})
    public String userAnimeList(Model model,
                             HttpSession session,
                             @RequestParam("status") String status,
                             @RequestParam("username") String username) {

        System.out.println("type : " + status);
        System.out.println("username : " + username);

        //  1 : curWatch_btn
        //  2 : complete_btn
        //  3 : on_hold_btn
        //  4 : dropped_btn
        //  6 : planWtc_btn

        if (!status.equals("1") && !status.equals("2") && !status.equals("3") && !status.equals("4") && !status.equals("6")) {
            return "error/genericError";
        }

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }
        boolean userFound = userRepoMongoDB.existsByUsername(username);
        System.out.println("userFound : " + userFound + " username: " + username);
        if(userFound) {
            List<AnimeWatched>aniList = userRepoNeo4j.findWatchedAnime(username, Integer.parseInt(status));
            boolean aniListFound = (aniList != null && !aniList.isEmpty());
            if(aniListFound) {
                model.addAttribute("aniList", aniList);
                model.addAttribute("username", username);
            }
        }
        else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));
        model.addAttribute("status", Utility.statusFromInt(Integer.parseInt(status)));

        return "userAnimeListPage";
    }
}
