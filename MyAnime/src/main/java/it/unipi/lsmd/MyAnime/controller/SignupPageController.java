package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.utilities.Utility;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignupPageController {
    @RequestMapping("/signup")
    public String signupPage(HttpSession session, Model model) {
        boolean is_logged = (Utility.isLogged(session))? true : false ;
        model.addAttribute("logged", is_logged );
        
        if(is_logged){
            return "error/alreadyLogged";
        }
        else{
            return "signup";
        }
    }
}
