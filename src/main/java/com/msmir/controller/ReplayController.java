package com.msmir.controller;

import com.msmir.containers.replay.GameReplayFull;
import com.msmir.containers.replay.GameReplayPage;
import com.msmir.entity.GameReplay;
import com.msmir.service.GameReplayService;
import com.msmir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplayController {

  @Autowired
  private GameReplayService replayService;
  @Autowired
  private UserService userService;

  @GetMapping(value = "/data/myReplays", produces = "application/json")
  public GameReplayPage getReplays(@RequestParam(name = "page") int page){
    return replayService.getReplayPage(userService.getCurrentUser(), page);
  }

  @GetMapping(value = "/data/replay", produces = "application/json")
  public GameReplay getReplay(@RequestParam(name = "id") long id){
    return replayService.getReplay(id);
  }

  @GetMapping(value = "/data/myReplaysPageCount", produces = "application/json")
  public int getPageCount(){
    return replayService.getPageCount(userService.getCurrentUser());
  }

  @GetMapping(value = "/data/fullReplay", produces = "application/json")
  public GameReplayFull getFullReplay(@RequestParam(name = "id") long id){
    GameReplay replay = replayService.getReplay(id);
    if(replay != null){
      return new GameReplayFull(replay);
    }
    return null;
  }
}
