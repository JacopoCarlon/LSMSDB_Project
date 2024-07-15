package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserFollowPageController {

    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @GetMapping(value={"userFollow","userFollowPage","userFollow.html","userFollowPage.html"})
    public String userFollow(Model model,
                             HttpSession session,
                             @RequestParam("type") String ftype,
                             @RequestParam("username") String username) {

        if (ftype != "following" && ftype != "follower") {
            return "error/userNotFound";
        }

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }
        boolean userFound = userRepoMongoDB.existsByUsername(username);
        if(userFound) {
            List<User>followList = userRepoMongoDB.getFollowByUsernameType(username, ftype);

            boolean followuFound = (followList != null && !followList.isEmpty());
            if(followuFound) {

                model.addAttribute("followus", followList);

                model.addAttribute("username", username);

                model.addAttribute("type", ftype);

            }
        }
        else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("admin", Utility.isAdmin(session));

        return "userFollow";
    }

}
