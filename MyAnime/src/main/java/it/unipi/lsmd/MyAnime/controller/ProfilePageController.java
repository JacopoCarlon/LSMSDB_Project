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

        if(!Utility.isLogged(session)){
            return "error/mustBeLogged";
        }
        else if(Utility.isAdmin(session)){
            return "adminPage";
        }
        else{
            String this_username = Utility.getUsername(session);
            System.out.println("entering private profilePage of user : " + this_username);
            User optionalUser = userRepoMongoDB.getUserByUsername(this_username);

            if(optionalUser==null){
                return "error/genericError";
            }
            model.addAttribute("logged", Utility.isLogged(session));
            model.addAttribute("userDetails", optionalUser);
            return "profilePage";
        }
    }
}
