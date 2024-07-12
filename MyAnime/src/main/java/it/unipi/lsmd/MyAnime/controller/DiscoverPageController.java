package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.AnimeNode;
//  import it.unipi.lsmd.MyAnime.model.UserNode;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoNeo4j;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiscoverPageController {
    @Autowired
    UserRepoNeo4j userRepoNeo4j;
    @Autowired
    AnimeRepoNeo4j animeRepoNeo4j;

    @RequestMapping(value={"/discover.html","/discoverPage.html","/discover","/discoverPage"})
    public String discoverPage(HttpSession session, Model model) {
        if(!Utility.isLogged(session))
            return "redirect:/login";
        if(Utility.isAdmin(session))
            return "error/accessDenied";

        String currentUsername = Utility.getUsername(session);

        ArrayList<AnimeNode> suggestedAnimes_ByTaste = animeRepoNeo4j.getSuggestedAnime_ByTaste(currentUsername);
        if(suggestedAnimes_ByTaste == null)
            return "error/genericError";
        else{
            if(suggestedAnimes_ByTaste.isEmpty())
                model.addAttribute("no_s_1", true);
            else
                model.addAttribute("suggestedAnimes_ByTaste", suggestedAnimes_ByTaste);
        }
/*
        ArrayList<AnimeRepoNeo4j> suggestedAnimes_ByFollow = animeRepoNeo4j.getSuggestedAnimes_ByFollowed(currentUsername);
        if(suggestedAnimes_ByFollow == null)
            return "error/genericError";
        else {
            if (suggestedAnimes_ByFollow.isEmpty())
                model.addAttribute("no_s_2", true);
            else
                model.addAttribute("suggestedAnimes_ByFollow", suggestedAnimes_ByFollow);
        }

        ArrayList<UserRepoNeo4j> suggestedUsersToFollow = userRepoNeo4j.findSuggestedUserstoFollow(currentUsername);
        if(suggestedUsersToFollow == null)
            return "error/genericError";
        else {
            if (suggestedAnimes_ByFollow.isEmpty())
                model.addAttribute("no_s_3", true);
            else
                model.addAttribute("suggestedUsersToFollow", suggestedUsersToFollow);
        }
*/
        model.addAttribute("is_logged", Utility.isLogged(session) );

        if(!suggestedAnimes_ByTaste.isEmpty())// || !suggestedAnimes_ByFollow.isEmpty() || !suggestedUsersToFollow.isEmpty())
            return "discoverPage";
        else
            return "error/nothingDiscovered";
    }
}
