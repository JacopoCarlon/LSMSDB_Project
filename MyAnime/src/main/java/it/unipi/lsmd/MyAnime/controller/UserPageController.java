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
public class UserPageController {

    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @RequestMapping(value={"/user.html","/userPage.html","/userPage","/user"})
    public String discoverPage(HttpSession session,
                               Model model,
                               @RequestParam("username") String username){

        if(!Utility.isLogged(session)){
            return "error/mustBeLogged";
        }
        User user;

        if(username != null){
            user = userRepoMongoDB.getUserByUsername(username);
            if(user == null)
                return "error/userNotFound";
            else{
                if(Utility.isLogged(session)){
                    model.addAttribute("logged", Utility.isLogged(session) );
                    model.addAttribute("userDetails", user);
                    model.addAttribute("is_admin", Utility.isAdmin(session) );

                    return "userPage";
                }else
                    return "error/mustBeLogged";
            }
        }else{
            return "error/userNotFound";
        }
    }

}
