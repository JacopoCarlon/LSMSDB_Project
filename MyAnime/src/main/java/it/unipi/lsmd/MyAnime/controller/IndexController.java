package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping(value={"/", "/index", "/index.html"})
    public String homePage(HttpSession session, Model model) {

        if(!Utility.isLogged(session)){
            //  System.out.println("entered mostPopularPage, value of isLogged is : " + Utility.isLogged(session) + ", redirecting to login");
            return "loginPage";
        }else{
            return "mostPupularPage";
        }

        //  return "loginPage";
    }
}
