package com.msmir.service;

import com.msmir.containers.replay.GameReplayPage;
import com.msmir.entity.GameReplay;
import com.msmir.entity.user.User;
import com.msmir.repository.GameReplayRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GameReplayService {
  private static final int PAGE_SIZE = 5;

  @Autowired
  private GameReplayRepository gameReplayRepository;

  public GameReplayPage getReplayPage(User user, int page){
    if(user != null){
      GameReplayPage replayPage = new GameReplayPage();
      replayPage.setPage(page);
      replayPage.setCurrentTime(LocalDateTime.now());
      replayPage.setReplayList(gameReplayRepository.findReplays(user,
          PageRequest.of(page, PAGE_SIZE)).toList());
      return replayPage;
    }
    return null;
  }

  public int getPageCount(User user){
    if(user != null){
      return gameReplayRepository.findReplays(user,
          PageRequest.of(0, PAGE_SIZE)).getTotalPages();
    }
    return 0;
  }

  public GameReplay getReplay(long replayId){
    return gameReplayRepository.findById(replayId).orElse(null);
  }
}
