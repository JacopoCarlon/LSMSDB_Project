package it.unipi.lsmd.MyAnime.controller.api;

import com.google.gson.Gson;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchREST {
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @GetMapping("/api/search")
    public @ResponseBody String search(@RequestParam("term") String term,
                                       @RequestParam("category") String category){

        //  if(category.equals("anime")){
        //      return new Gson().toJson(animeRepoMongoDB.find5Anime(term));
        //  }
        //  else if(category.equals("user")){
        //      return new Gson().toJson(userRepoMongoDB.find5User(term));
        //  }
        //  else
            return "Invalid category";
    }
}
