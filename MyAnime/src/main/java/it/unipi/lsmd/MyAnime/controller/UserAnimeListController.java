package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;
import java.util.List;

import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserAnimeListController {

    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @Autowired
    UserRepoNeo4j userRepoNeo4j;

    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;

    @GetMapping(value={"userAnimeList","userAnimeListPage","userAnimeList.html","userAnimeListPage.html"})
    public String userFollow(Model model,
                             HttpSession session,
                             @RequestParam("type") String type,
                             @RequestParam("username") String username) {

        System.out.println("type : " + type);
        System.out.println("username : " + username);

        //  0 : curWatch_btn
        //  1 : complete_btn
        //  2 : on_hold_btn
        //  3 : dropped_btn
        //  4 : planWtc_btn

        if (type != "0" && type != "1" && type != "2" && type != "3" && type != "4" ) {
            return "error/userNotFound";
        }

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }
        boolean userFound = userRepoMongoDB.existsByUsername(username);
        System.out.println("userFound : " + userFound + " username: " + username);
        if(userFound) {
            //  List<Anime>aniList = animeRepoMongoDB.getAnimeByUsernameType(username, type);
            //  boolean aniListFound = (aniList != null && !aniList.isEmpty());
            //  if(aniListFound) {
            //      model.addAttribute("aniList", aniList);
            //      model.addAttribute("username", username);
            //      model.addAttribute("type", type);
            //  }
        }
        else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        return "userAnimeListPage";
    }



}
