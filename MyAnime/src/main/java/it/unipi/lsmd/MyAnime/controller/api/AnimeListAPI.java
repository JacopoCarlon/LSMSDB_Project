package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimeListAPI {
    @Autowired
    UserRepoNeo4j userRepoNeo4j;
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;

    @PostMapping("/api/setAnimeToList")
    @Transactional("transactionManager")
    public @ResponseBody String setAnimeToList(HttpSession session,
                                               @RequestParam("animeTitle") String animeTitle,
                                               @RequestParam("targetList") String targetList) {

        try {
            if (!Utility.isLogged(session))
                return "{\"outcome_code\": 1}";     // User not logged in
            if (Utility.isAdmin(session))
                return "{\"outcome_code\": 2}";     // Invalid action for an admin

            if (!targetList.equals("1") && !targetList.equals("2") && !targetList.equals("3") && !targetList.equals("4") && !targetList.equals("6")) {
                return "{\"outcome_code\": 3}";
            }
            Integer targetListNumber = Integer.parseInt(targetList);

            String username = Utility.getUsername(session);
            if(!animeRepoMongoDB.existsByTitle(animeTitle))
                return "{\"outcome_code\": 4}";     // Anime doesn't exist

            Integer oldStatus = userRepoNeo4j.addWatches(username, animeTitle, targetListNumber);

            if (oldStatus.equals(targetListNumber))
                return "{\"outcome_code\": 5}";

            return "{\"outcome_code\": 0}";
        }
        catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 12}";         // Error while connecting to the database
        }
    }
}
