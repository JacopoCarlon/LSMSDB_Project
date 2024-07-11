package it.unipi.lsmd.MyAnime.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public class AdminPageController {
    @RequestMapping("/adminPage")
    public String adminPage(HttpSession session,
                            Model model){
        if (!Utility.isLogged(session))
            return "error/youMustBeLogged";
        else if(!Utility.isAdmin(session))
            return "error/accessDenied";

        try {
            model.addAttribute("logged", Utility.isLogged(session));
            //  TODO : 
            //   need access to admin data, like :
            //  AdminData adminData = Utility.readAdminData();
            //  if(adminData != null){
            //      model.addAttribute("adminDataFound", true);
            //      model.addAttribute("dailyReviews", adminData.getDailyReviews());
            //  }
            //  else
            //      model.addAttribute("adminDataFound", false);

            return "adminPage";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/genericError";
        }
    }
}
