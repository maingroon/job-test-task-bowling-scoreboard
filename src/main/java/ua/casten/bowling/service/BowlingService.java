package ua.casten.bowling.service;

import ua.casten.bowling.model.ViewFrame;

public interface BowlingService {

    void startNewGame();

    String makePoll(String stringScore);

    ViewFrame[] getFrames();

    boolean isFinished();

}
