package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.ViewFrame;
import ua.casten.bowling.util.FrameParser;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BowlingServiceImpl implements BowlingService {

    private int gameId;
    private Game currentGame;

    private final GameService gameService;
    private final FrameService frameService;

    @Autowired
    public BowlingServiceImpl(GameService gameService, FrameService frameService) {
        this.gameService = gameService;
        this.frameService = frameService;
        gameId = -1;
    }

    @Override
    public int startNewGame() {
        currentGame = new Game();
        gameService.save(currentGame);
        gameId = currentGame.getId();

        currentGame.getFrames()
                .forEach(frame -> frame.setGame(currentGame));

        currentGame.getFrames()
                .forEach(frameService::save);

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
            frameService.save(frame);
        }
        gameService.save(currentGame);
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
        if (currentGame.getId() == gameId) {
            return currentGame;
        }

        try {
            Game game = gameService.findById(gameId);
            game.setFrames(
                    game.getFrames().stream()
                            .sorted(Comparator.comparingInt(Frame::getNumber))
                            .collect(Collectors.toList())
            );
            return game;
        } catch (BowlingException e) {
            throw new BowlingRuntimeException(e.getMessage());
        }
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

    public boolean isFinished() {
        return currentGame.isFinished();
    }

}
