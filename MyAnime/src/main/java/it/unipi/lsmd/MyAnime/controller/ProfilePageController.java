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

    @RequestMapping("/profilePage")
    public String profilePage(HttpSession session,
                              Model model){

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(!Utility.isLogged(session))
            return "redirect:/login";
        else if(Utility.isAdmin(session))
            return "redirect:/adminPage";
        else{
            User optionalUser = userRepoMongoDB.getUserByUsername(Utility.getUsername(session));
            if(optionalUser==null)
                return "error/genericError";
            model.addAttribute("userDetails", optionalUser);
            return "profilePage";
        }
    }
}
