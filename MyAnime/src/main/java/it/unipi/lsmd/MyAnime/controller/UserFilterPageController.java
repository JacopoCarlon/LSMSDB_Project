package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserFilterPageController {

    @RequestMapping(value={"/userFilter.html","/userFilterPage.html","/userFilter","/userFilterPage"})
    public String mostPopularPage(HttpSession session,
                                  Model model) {

        if (!Utility.isLogged(session)) {
             return "error/mustBeLogged";
        }
        else {
            model.addAttribute("logged", Utility.isLogged(session));
            model.addAttribute("is_admin", Utility.isAdmin(session));
            return "userFilterPage";
        }
    }
}
