package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupREST {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    UserRepoNeo4j userRepoNeo4j;

    @PostMapping("/api/signup")
    @Transactional
    public @ResponseBody String signup(
            @RequestParam("name") String name
    ) {

        return "";
    }
}
