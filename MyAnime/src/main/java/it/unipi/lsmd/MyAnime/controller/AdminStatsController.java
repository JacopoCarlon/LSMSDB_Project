package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.query.GenreScored;
import it.unipi.lsmd.MyAnime.model.query.ReviewsPerDate;
import it.unipi.lsmd.MyAnime.model.query.UsersPerDate;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminStatsController {
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    ReviewRepoMongoDB reviewRepoMongoDB;

    @RequestMapping(value={"/adminStats.html","/adminStats"})
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

            System.out.println(">> START: calculating admin stats");

            List<GenreScored> genreScored = animeRepoMongoDB.getGenreScored();

            List<UsersPerDate> usersPerDates = userRepoMongoDB.getUsersPerDates();

            List<ReviewsPerDate> reviewsPerDates = reviewRepoMongoDB.getReviewsPerDates();

            model.addAttribute("genreScored", genreScored);
            model.addAttribute("usersPerDates", usersPerDates);
            model.addAttribute("reviewsPerDates", reviewsPerDates);

            System.out.println(">>> END: All new watchers and average scores updated successfully");
            return "adminStats";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/genericError";
        }
    }
}
