package com.msmir.controller;

import com.msmir.entity.user.UserRating;
import com.msmir.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/data/rating", produces = "application/json")
  public List<UserRating> getRatingList(@RequestParam(name = "type") String ratingType){
    return userService.getUserRating(ratingType);
  }
}
