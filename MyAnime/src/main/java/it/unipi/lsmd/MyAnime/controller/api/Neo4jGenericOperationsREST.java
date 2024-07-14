package it.unipi.lsmd.MyAnime.controller.api;


import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Neo4jGenericOperationsREST {

    @Autowired
    UserRepoNeo4j userRepoNeo4j;

    // TODO : all the follow / block stuff !!!
    /*
    @PostMapping("/api/addFollow")
    public @ResponseBody String addFollow(
            @RequestParam("user1") String user1,
            @RequestParam("user2") String user2) {

        String result = userRepoNeo4j.addFollow(user1, user2);
        if(result.equals("CREATED"))
            return "{\"outcome_code\": 0}";
        else if(result.equals("EXISTING"))
            return "{\"outcome_code\": 1}";
        else
            return "{\"outcome_code\": 2}";
    }

    @PostMapping("/api/removeFollow")
    public @ResponseBody String removeFollow(
            @RequestParam("user1") String user1,
            @RequestParam("user2") String user2) {

        if(userRepoNeo4j.removeFollow(user1, user2))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }
    */



















}
