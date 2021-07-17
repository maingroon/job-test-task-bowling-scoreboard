package ua.casten.bowling.service;

import ua.casten.bowling.model.Frame;

public interface BowlingService {

    void startNewGame();

    Frame[] getFrames();

}
