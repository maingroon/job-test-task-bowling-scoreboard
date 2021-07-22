package ua.casten.bowling.service;

import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.ViewFrame;
import ua.casten.bowling.util.FrameParser;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class BowlingServiceImpl implements BowlingService {

    private int gameId;
    private Game currentGame;
    private final Map<Integer, Game> gameMap;

    public BowlingServiceImpl() {
        gameMap = new HashMap<>();
        gameId = -1;
    }

    @Override
    public int startNewGame() {
        gameId = gameMap.size();
        currentGame = new Game();
        gameMap.put(gameId, currentGame);

        return gameId;
    }

    @Override
    public String makeRoll(String stringScore) {
        int intScore;

        try {
            intScore = Integer.parseInt(stringScore.trim());
        } catch (NumberFormatException e) {
            return "Enter valid score (without symbols and spaces)";
        }

        if (intScore < 0 || intScore > 10) {
            return "Score cannot be less than 0 or greater than 10";
        }

        try {
            makeRoll(intScore);
        } catch (BowlingException e) {
            return e.getMessage();
        }

        return "";
    }

    private void makeRoll(int score) throws BowlingException {
        var currentFrameIndex = currentGame.getCurrentFrameIndex();
        var frame = currentGame.getFrames().get(currentFrameIndex);
        frame.setInGame(true);
        switch (frame.getRollNumber()) {
            case 1:
                makeFirstRoll(currentFrameIndex, score);
                break;
            case 2:
                makeSecondRoll(currentFrameIndex, score);
                break;
            case 3:
                makeThirdRoll(currentFrameIndex, score);
                break;
            default:
                throw new BowlingRuntimeException("Incorrect roll number in current frame.");
        }
        updateFramesData(currentFrameIndex);
    }

    private void makeFirstRoll(int currentFrameIndex, int score) {
        var frame = currentGame.getFrames().get(currentFrameIndex);
        frame.setFirstRoll(score);

        if (score == 10 && currentFrameIndex != 9) {
            currentGame.increaseCurrentFrameIndex();
        }
        frame.setRollNumber(2);
    }

    private void makeSecondRoll(int currentFrameIndex, int score) throws BowlingException {
        var frame = currentGame.getFrames().get(currentFrameIndex);

        if (frame.getFirstRoll() + score > 10 && !(currentFrameIndex == 9 && frame.isStrike())) {
            throw new BowlingException("Sum of rolls in current frame cannot be greater than 10");
        }

        frame.setSecondRoll(score);

        if (currentFrameIndex != 9) {
            currentGame.increaseCurrentFrameIndex();
        } else if (!(frame.isSpare() || frame.isStrike())) {
            currentGame.finishGame();
        }
        frame.setRollNumber(3);
    }

    private void makeThirdRoll(int currentFrameIndex, int score) throws BowlingException {
        var frame = currentGame.getFrames().get(currentFrameIndex);

        if (frame.isStrike() && frame.getSecondRoll() != 10 && frame.getSecondRoll() + score > 10) {
            throw new BowlingException("Sum of second and third rolls cannot be greater than 10 without second strike");
        }

        frame.setThirdRoll(score);
        frame.setRollNumber(4);
        currentGame.finishGame();
    }

    private void updateFramesData(int currentFrameIndex) {
        var frames = currentGame.getFrames();

        IntStream.rangeClosed(0, currentFrameIndex)
                .forEach(index -> updateFrameBonus(frames.get(index), index));

        var scoreSum = 0;
        for (var i = 0; i <= currentFrameIndex; i++) {
            var frame = frames.get(i);
            scoreSum += frame.getFirstRoll() + frame.getSecondRoll() + frame.getThirdRoll() + frame.getBonus();
            frame.setScore(scoreSum);
        }
    }

    private void updateFrameBonus(Frame frame, int index) {
        if (frame.isStrike()) {
            frame.setBonus(getStrikeBonus(index));
        } else if (frame.isSpare()) {
            frame.setBonus(getSpareBonus(index));
        }
    }

    private int getStrikeBonus(int index) {
        if (index == 9) {
            return 0;
        }

        var frames = currentGame.getFrames();
        var nextFrame = frames.get(index + 1);
        var bonus = 0;

        if (nextFrame.isStrike()) {
            bonus = 10;

            if (index != 8) {
                bonus += frames.get(index + 2).getFirstRoll();
            } else {
                bonus += nextFrame.getSecondRoll();
            }
        } else {
            bonus = nextFrame.getFirstRoll() + nextFrame.getSecondRoll();
        }

        return bonus;
    }

    private int getSpareBonus(int index) {
        if (index == 9) {
            return 0;
        }

        var frames = currentGame.getFrames();
        var nextFrame = frames.get(index + 1);
        var bonus = 0;

        if (nextFrame.isStrike()) {
            bonus = 10;
        } else {
            bonus = nextFrame.getFirstRoll();
        }

        return bonus;
    }

    private Game getCurrentGame() {
        if (!gameMap.containsKey(gameId)) {
            throw new BowlingRuntimeException("Incorrect game id.");
        }

        return gameMap.get(gameId);
    }

    @Override
    public ViewFrame[] getFrames() {
        return FrameParser.parseFrames(currentGame.getFrames());
    }

    @Override
    public void setGameId(int gameId) {
        this.gameId = gameId;
        currentGame = getCurrentGame();
    }

    public boolean isStarted() {
        return !gameMap.isEmpty();
    }

    public boolean isFinished() {
        return currentGame.isFinished();
    }

}
