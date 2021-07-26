package ua.casten.bowling.service;

import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.Game;

public interface BowlingService {

    long startNewGame();

    void makeRoll(Game game, String stringScore) throws BowlingException;

}
