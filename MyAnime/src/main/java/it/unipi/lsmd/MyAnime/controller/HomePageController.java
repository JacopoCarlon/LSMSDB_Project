package it.unipi.lsmd.MyAnime.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

    @RequestMapping(value={"/homePage","/home"})
    public String homePage(HttpSession session, Model model) {

        return "homePage";
    }
}
