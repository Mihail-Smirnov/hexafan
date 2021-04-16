package com.msmir.controller;

import com.msmir.entity.user.UserRating;
import com.msmir.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/data/rating", produces = "application/json")
  public Map<String, List<UserRating>> getRatingList(){
    Map<String, List<UserRating>> ratingMap = new HashMap<>();
    for(String ratingType : List.of("WINNERS", "GAME_TIME")){
      ratingMap.put(ratingType, userService.getUserRating(ratingType));
    }
    return ratingMap;
  }
}
