package com.msmir.controller;

import com.msmir.containers.game.Game;
import com.msmir.entity.GameReplay;
import com.msmir.entity.user.User;
import com.msmir.repository.GameRepository;
import com.msmir.service.GameReplayService;
import com.msmir.service.UserService;
import com.msmir.service.WaitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
  @Autowired
  private WaitingService waitingService;
  @Autowired
  private UserService userService;
  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private GameReplayService replayService;

  @GetMapping(value = "/")
  public String mainPage(Model model){
    userService.addUserAvatar(model);
    return "index";
  }

  @GetMapping(value = "/about")
  public String aboutPage(Model model){
    userService.addUserAvatar(model);
    return "about";
  }

  @GetMapping(value = "/rating")
  public String ratingPage(Model model){
    userService.addUserAvatar(model);
    return "rating";
  }

  @GetMapping(value = "/waiting")
  public String waitingPage(Model model){
    User user = userService.getCurrentUser();
    if(user != null){
      Game game =  gameRepository.getGame(user.getGameId());
      if(game != null){
        return "redirect:/game/" + game.getId().toString();
      }
    }
    userService.addUserAvatar(model);
    return "waiting";
  }

  @GetMapping(value = "/profile")
  public String profilePage(Model model){
    userService.addUserAvatar(model);
    return "profile";
  }

  @GetMapping(value = "/game/{gameId}")
  public String gamePage(@PathVariable(name = "gameId") String gameId, Model model){
    userService.addUserAvatar(model);
    Game game = gameRepository.getGame(gameId);
    if (game != null) {
      model.addAttribute("whitePlayerUsername", game.getWhitePlayer().getUsername());
      model.addAttribute("whitePlayerAvatar", userService.getUserAvatar(game.getWhitePlayer()));
      model.addAttribute("blackPlayerUsername", game.getBlackPlayer().getUsername());
      model.addAttribute("blackPlayerAvatar", userService.getUserAvatar(game.getBlackPlayer()));
    }

    return "game";
  }

  @GetMapping(value = "/replay")
  public String replayPage(@RequestParam(name = "id") long replayId, Model model){
    userService.addUserAvatar(model);
    GameReplay replay = replayService.getReplay(replayId);
    if(replay != null){
      model.addAttribute("whitePlayerUsername", replay.realWhitePlayer().getUsername());
      model.addAttribute("whitePlayerAvatar", userService.getUserAvatar(replay.realWhitePlayer()));
      model.addAttribute("blackPlayerUsername", replay.realBlackPlayer().getUsername());
      model.addAttribute("blackPlayerAvatar", userService.getUserAvatar(replay.realBlackPlayer()));
    }
    return "replay";
  }
}
