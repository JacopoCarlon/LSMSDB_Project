package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfilePageController {

    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @RequestMapping("/profilePage")
    public String profilePage(HttpSession session,
                              Model model,
                              @RequestParam("username") String username) {

        User user;

        return "";
    }
}
