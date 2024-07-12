package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.utilities.Utility;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
//  import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.model.User;

@Controller
public class UserPersonalPageController {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @RequestMapping(value={"/userPage.html","/userPersonalPage.html","/userPage","/userPersonalPage","/user","/userPersonal"})
    public String discoverPage(HttpSession session,
                               Model model,
                               @RequestParam("username") String username){
        User user;

        if(username != null){
            user = userRepoMongoDB.getUserByUsername(username);
            if(user == null)
                return "error/userNotFound";
            else{
                if(Utility.isLogged(session)){
                    model.addAttribute("logged", Utility.isLogged(session) );
                    model.addAttribute("userDetails", user);
                    model.addAttribute("admin", Utility.isAdmin(session) );

                    return "user";
                }else
                    return "error/mustBeLogged";
            }
        }else{
            return "error/userNotFound";
        }
    }

}
