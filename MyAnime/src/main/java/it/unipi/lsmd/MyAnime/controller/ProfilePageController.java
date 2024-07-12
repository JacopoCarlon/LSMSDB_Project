package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.utilities.Utility;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
//  import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.model.User;

@Controller
public class ProfilePageController {

    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @RequestMapping(value={"/profile.html","/profilePage.html","/profile","/profilePage"})
    public String profilePage(HttpSession session,
                              Model model){

        model.addAttribute("is_logged", (Utility.isLogged(session)) ? true : false);

        if(!Utility.isLogged(session))
            return "redirect:/login";
        else if(Utility.isAdmin(session))
            return "redirect:/admin";
        else{
            User optionalUser = userRepoMongoDB.getUserByUsername(Utility.getUsername(session));
            if(optionalUser==null)
                return "error/genericError";
            model.addAttribute("userDetails", optionalUser);
            return "profilePage";
        }
    }
}
