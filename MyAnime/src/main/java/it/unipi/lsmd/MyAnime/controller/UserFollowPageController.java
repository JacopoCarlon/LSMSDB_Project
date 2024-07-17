package it.unipi.lsmd.MyAnime.controller;

import java.util.LinkedList;
import java.util.List;

import it.unipi.lsmd.MyAnime.model.UserNode;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
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

    @Autowired
    UserRepoNeo4j userRepoNeo4j;

    @GetMapping(value={"userFollow","userFollowPage","userFollow.html","userFollowPage.html"})
    public String userFollow(Model model,
                             HttpSession session,
                             @RequestParam("type") String type,
                             @RequestParam("username") String username) {

        System.out.println("type : " + type);
        System.out.println("username : " + username);

        if (!type.equals("following") && !type.equals("follower")) {
            return "error/userNotFound";
        }

        if (!Utility.isLogged(session)) {
            return "error/mustBeLogged";
        }
        boolean userFound = userRepoMongoDB.existsByUsername(username);
        System.out.println("userFound : " + userFound + " username: " + username);
        if(userFound) {
            List<UserNode>followList;
            if (type.equals("following"))
                followList = userRepoNeo4j.findFollowedByUsername(username);
            else
                followList = userRepoNeo4j.findFollowersOfUsername(username);

            if(followList != null && !followList.isEmpty()) {
                model.addAttribute("followus", followList);
                model.addAttribute("username", username);
                model.addAttribute("type", type);
            }
        }
        else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("is_admin", Utility.isAdmin(session));

        return "userFollowPage";
    }

}
