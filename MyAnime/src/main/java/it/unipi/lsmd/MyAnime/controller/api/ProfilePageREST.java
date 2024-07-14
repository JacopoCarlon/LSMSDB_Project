package it.unipi.lsmd.MyAnime.controller.api;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.server.ResponseStatusException;

import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;

@RestController
public class ProfilePageREST {

    @Autowired
    private UserRepoNeo4j userRepoNeo4j;

    @PostMapping("/api/logout")
    public @ResponseBody String logout(HttpSession session) {
        if (!Utility.isLogged(session)) {
            // Lancia un'eccezione se l'utente non è loggato
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Accesso non autorizzato");
        }

        session.invalidate(); // Invalida la sessione se l'utente è loggato
        return "{\"outcome_code\": 0}";
    }



    //  TODO : this graph query
    /*
    @GetMapping("/api/userLikedAnime")
    public @ResponseBody String userLikedAnime( HttpSession session,
                                                @RequestParam("username") String username) {
        if (!Utility.isLogged(session)) {
            return "{\"outcome_code\": 1}"; // User not found
        }


        return new Gson().toJson(userRepoNeo4j.getLikedAnimeByUsername(username));
    }



    // TODO : this graph query
    @GetMapping("/api/userFollowingUsers")
    public @ResponseBody String userFollowingUsers( HttpSession session,
                                                   @RequestParam("username") String username) {
        if (!Utility.isLogged(session)) {
            return "{\"outcome_code\": 1}"; // User not found
        }

        return new Gson().toJson(userRepoNeo4j.getFollowingUsersByUsername(username));
    }


    // TODO : this graph query
    @GetMapping("/api/userFollowerUsers")
    public @ResponseBody String userFollowerUsers( HttpSession session,
                                                   @RequestParam("username") String username) {
        if (!Utility.isLogged(session)) {
            return "{\"outcome_code\": 1}"; // User not found
        }

        return new Gson().toJson(userRepoNeo4j.getFollowerUsersByUsername(username));
    }
    */


}
