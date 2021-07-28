package ua.casten.bowling.service;

import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.Game;

public interface FrameService {

    void makeRoll(Game game, int score) throws BowlingException;

    void updateFramesData(Game game);

}
