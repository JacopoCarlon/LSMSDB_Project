package it.unipi.lsmd.MyAnime.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unipi.lsmd.MyAnime.utilities.Utility;

@Controller
public class LoginPageController {

    @RequestMapping("/login")
    public String loginPage(HttpSession session) {
        if(!Utility.isLogged(session)){
            return "login";
        }
        else{
            return "error/alreadyLogged";
        }
    }
}
