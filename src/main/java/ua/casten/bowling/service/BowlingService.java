package ua.casten.bowling.service;

import ua.casten.bowling.model.ViewFrame;

public interface BowlingService {

    int startNewGame();

    String makeRoll(String stringScore);

    ViewFrame[] getFrames();

    void setGameId(int gameId);

    boolean isFinished();

}
