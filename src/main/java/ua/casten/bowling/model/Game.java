package ua.casten.bowling.model;

import lombok.Getter;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;

import java.util.stream.IntStream;

@Getter
public class Game {

    private final Frame[] frames;
    private int currentFrameIndex;
    private boolean isFinished;

    public Game() {
        currentFrameIndex = 0;
        frames = new Frame[10];
        for (var i = 0; i < 10; i++) {
            frames[i] = new Frame();
        }
        isFinished = false;
    }

    public void makePoll(int score) throws BowlingException {
        frames[currentFrameIndex].setInGame(true);
        switch (frames[currentFrameIndex].getRollNumber()) {
            case 1:
                makeFirstRoll(score);
                break;
            case 2:
                makeSecondRoll(score);
                break;
            case 3:
                makeThirdRoll(score);
                break;
            default:
                throw new BowlingRuntimeException("Incorrect roll number in current frame.");
        }
        updateFramesData();
    }

    private void makeFirstRoll(int score) {
        var frame = frames[currentFrameIndex];
        frame.setFirstRoll(score);

        if (score == 10 && currentFrameIndex != 9) {
            currentFrameIndex++;
        }
        frame.setRollNumber(2);
    }

    private void makeSecondRoll(int score) throws BowlingException {
        var frame = frames[currentFrameIndex];

        if (frame.getFirstRoll() + score > 10 && !(currentFrameIndex == 9 && frame.isStrike())) {
            throw new BowlingException("Sum of poll in current frame cannot be greater than 10");
        }

        frame.setSecondRoll(score);

        if (currentFrameIndex != 9) {
            currentFrameIndex++;
        } else if (!(frame.isSpare() || frame.isStrike())) {
            finishGame();
        }
        frame.setRollNumber(3);
    }


    private void makeThirdRoll(int score) throws BowlingException {
        var frame = frames[currentFrameIndex];

        if (frame.isStrike() && frame.getSecondRoll() != 10 && frame.getSecondRoll() + score > 10) {
            throw new BowlingException("Sum of second and third poll cannot be greater than 10 without second strike");
        }

        frame.setThirdRoll(score);
        frame.setRollNumber(4);
        isFinished = true;
    }

    private void updateFramesData() {
        IntStream.rangeClosed(0, currentFrameIndex)
                .forEach(this::updateFrameBonus);

        var scoreSum = 0;
        for (var i = 0; i <= currentFrameIndex; i++) {
            var frame = frames[i];
            scoreSum += frame.getFirstRoll() + frame.getSecondRoll() + frame.getThirdRoll() + frame.getBonus();
            frame.setScore(scoreSum);
        }
    }

    private void updateFrameBonus(int index) {
        var frame = frames[index];

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

        var nextFrame = frames[index + 1];
        var bonus = 0;

        if (nextFrame.isStrike()) {
            bonus = 10;

            if (index != 8) {
                bonus += frames[index + 2].getFirstRoll();
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

        var nextFrame = frames[index + 1];
        var bonus = 0;

        if (nextFrame.isStrike()) {
            bonus = 10;
        } else {
            bonus = nextFrame.getFirstRoll();
        }

        return bonus;
    }

    private void finishGame() {
        isFinished = true;
    }

}
