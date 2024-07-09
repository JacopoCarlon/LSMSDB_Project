package it.unipi.lsmd.MyAnime.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignupPageController {
    @RequestMapping("/signup")
    public String signupPage(HttpSession session, Model model) {
        return "";
    }
}
