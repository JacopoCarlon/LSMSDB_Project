package it.unipi.lsmd.MyAnime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class AdminPageController {

    @RequestMapping(value={"/admin.html","/adminPage.html","/admin","/adminPage"})
    public String adminPage(HttpSession session,
                            Model model){

        System.out.println("AdminPageController, is logged : " + Utility.isLogged(session));
        System.out.println("AdminPageController, is admin : " + Utility.isAdmin(session));

        if(!Utility.isLogged(session)){
            return "error/mustBeLogged";
        }
        else if(!Utility.isAdmin(session))
            return "error/accessDenied";

        try {
            model.addAttribute("logged", Utility.isLogged(session));
            model.addAttribute("is_admin", Utility.isAdmin(session));

            return "adminPage";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/genericError";
        }
    }
}
