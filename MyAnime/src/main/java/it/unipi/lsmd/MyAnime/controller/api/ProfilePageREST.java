package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProfilePageREST {



    @Autowired
    private UserRepoNeo4j userRepoNeo4j;


    @PostMapping("/api/logout")
    public @ResponseBody String logout(HttpSession session) {
        if (!Utility.isLogged(session)) {
            // Lancia eccezione se utente non loggato
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Accesso non autorizzato");
        }
        session.invalidate(); // Invalida la sessione se l'utente è loggato
        return "{\"outcome_code\": 0}";
    }
}
