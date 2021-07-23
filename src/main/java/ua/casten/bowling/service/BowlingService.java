package ua.casten.bowling.service;

import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.ViewFrame;

import java.util.List;

public interface BowlingService {

    int startNewGame();

    void makeRoll(Game game, String stringScore) throws BowlingException;

    List<ViewFrame> getViewFrames(Game game);

}
