package ua.casten.bowling.service;

import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.ViewFrame;

@Service
public class BowlingServiceImpl implements BowlingService {

    private Game game;

    public BowlingServiceImpl() {
        startNewGame();
    }

    @Override
    public void startNewGame() {
        game = new Game();
    }

    @Override
    public String makePoll(String stringScore) {
        int intScore;

        try {
            intScore = Integer.parseInt(stringScore.trim());
        } catch (NumberFormatException e) {
            return "Enter valid score (without symbols and spaces).";
        }

        if (intScore < 0 || intScore > 10) {
            return "Score cannot be less than 0 or greater than 10";
        }

        try {
            game.makePoll(intScore);
        } catch (BowlingException e) {
            return e.getMessage();
        }

        return "";
    }

    @Override
    public ViewFrame[] getFrames() {
        return FrameParser.parseFrames(game.getFrames());
    }

}
