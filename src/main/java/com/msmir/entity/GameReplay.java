package com.msmir.entity;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameSettings;
import com.msmir.entity.user.User;
import com.msmir.vm.FigureMoveVm;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_replay")
public class GameReplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User whitePlayer;
    @ManyToOne
    private User blackPlayer;
    private String winner;
    private LocalDateTime endDate;
    @Embedded
    private GameSettings settings;
    @Column(columnDefinition = "TEXT")
    private String movesText;

    public GameReplay() {
    }

    public GameReplay(Game game) {
        this.whitePlayer = game.getWhitePlayer();
        this.blackPlayer = game.getBlackPlayer();
        this.endDate = game.getStatus().getFinishTime();
        this.settings = game.getSettings();
        this.movesText = toMovesText(game.getMoves());
        this.winner = game.getStatus().getWinner();
    }

    private String toMovesText(List<FigureMoveVm> moves){
        StringBuilder builder = new StringBuilder();
        for(FigureMoveVm move : moves){
            builder.append(move.getFrom()).append(":").append(move.getTo()).append(";");
        }
        return builder.toString();
    }

    private List<FigureMoveVm> parseMovesText(String movesText){
        if(movesText == null){
            return null;
        }

        List<FigureMoveVm> moves = new ArrayList<>();
        String[] moveArray = movesText.split(";");
        for(String moveString : moveArray){
            String[] moveStringSplit = moveString.split(":");
            if(moveStringSplit.length == 2){
                moves.add(new FigureMoveVm(moveStringSplit[0], moveStringSplit[1]));
            }
        }

        return moves;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhitePlayer() {
        return whitePlayer.getUsername();
    }

    public void setWhitePlayer(User whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer.getUsername();
    }

    public void setBlackPlayer(User blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    public void setMovesText(String movesText) {
        this.movesText = movesText;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<FigureMoveVm> getMoves() {
        return parseMovesText(this.movesText);
    }

    public User realWhitePlayer(){
        return whitePlayer;
    }

    public User realBlackPlayer(){
        return blackPlayer;
    }
}
