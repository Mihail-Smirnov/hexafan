package com.msmir.containers.replay;

import com.msmir.entity.GameReplay;
import java.time.LocalDateTime;
import java.util.List;

public class GameReplayPage {
  private int page;
  private List<GameReplay> replayList;
  private LocalDateTime currentTime;

  public GameReplayPage() {
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public List<GameReplay> getReplayList() {
    return replayList;
  }

  public void setReplayList(List<GameReplay> replayList) {
    this.replayList = replayList;
  }

  public LocalDateTime getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(LocalDateTime currentTime) {
    this.currentTime = currentTime;
  }
}
