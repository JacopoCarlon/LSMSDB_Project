package it.unipi.lsmd.MyAnime.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unipi.lsmd.MyAnime.utilities.Utility;

@Controller
public class LoginPageController {

    @RequestMapping(value={"/login.html","/loginPage.html","/login","/loginPage"})
    public String loginPage(HttpSession session, Model model) {

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        if(!Utility.isLogged(session)){
            return "loginPage";
        }
        else{
            return "error/alreadyLogged";
        }
    }
}
