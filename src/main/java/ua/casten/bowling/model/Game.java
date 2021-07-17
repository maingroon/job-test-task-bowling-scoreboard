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
    private int fullScore;

    public Game() {
        currentFrameIndex = 1;
        frames = new Frame[11]; // Size one more for matching array index and frame number.
        for (var i = 0; i < 11; i++) {
            frames[i] = new Frame();
        }
        isFinished = false;
        fullScore = 0;
    }

    public Frame getFrame(int index) {
        return frames[index];
    }

    public void makePoll(int score) throws BowlingException {
        switch (frames[currentFrameIndex].getRollNumber()) {
            case 1:
                makeFirstPoll(score);
                break;
            case 2:
                makeSecondPoll(score);
                break;
            case 3:
                makeThirdPoll(score);
                break;
            default:
                throw new BowlingRuntimeException("Incorrect roll number in current frame.");
        }
        frames[currentFrameIndex].setInGame(true);
        updateFramesBonus();
        updateFramesScore();
    }

    private void makeFirstPoll(int score) {
        var frame = frames[currentFrameIndex];
        frame.setFirstRoll(score);

        if (score == 10 && currentFrameIndex != 10) {
            currentFrameIndex++;
        } else {
            frame.setRollNumber(2);
        }
    }

    private void makeSecondPoll(int score) throws BowlingException {
        var frame = frames[currentFrameIndex];

        if (frame.getFirstRoll() + score > 10 && !(currentFrameIndex == 10 && frame.isStrike())) {
            throw new BowlingException("Sum of poll in current frame cannot be greater than 10");
        }

        frame.setSecondRoll(score);

        if (currentFrameIndex != 10) {
            currentFrameIndex++;
        } else if (frame.isStrike() || frame.isSpare()) {
            frame.setRollNumber(3);
        } else {
            finishGame();
        }
    }

    private void makeThirdPoll(int score) throws BowlingException {
        var frame = frames[currentFrameIndex];

        if (frame.isStrike() && frame.getSecondRoll() != 10 && frame.getSecondRoll() + score > 10) {
            throw new BowlingException("Sum of second and third poll cannot be greater than 10 without second strike.");
        }

        frame.setThirdRoll(score);
        isFinished = true;
    }
    private void updateFramesBonus() {
        IntStream.rangeClosed(1, 10)
                .forEach(this::updateFrameBonus);
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
        if (index == 10) {
            return 0;
        }

        var nextFrame = frames[index + 1];
        var bonus = 0;

        if (nextFrame.isStrike()) {
            bonus = 10;

            if (index != 9) {
                var nextNextFrame = frames[index + 2];

                if (nextNextFrame.isStrike()) {
                    bonus += 10;
                } else {
                    bonus += nextFrame.getFirstRoll();
                }
            } else {
                if (nextFrame.getSecondRoll() == 10) {
                    bonus += 10;
                } else {
                    bonus += nextFrame.getSecondRoll();
                }
            }
        } else {
            bonus = nextFrame.getFirstRoll() + nextFrame.getSecondRoll();
        }

        return bonus;
    }

    private int getSpareBonus(int index) {
        if (index == 10) {
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

    private void updateFramesScore() {
        var scoreSum = 0;
        for (var i = 1; i < frames.length; i++) {
            var frame = frames[i];
            scoreSum += frame.getFirstRoll() + frame.getSecondRoll() + frame.getThirdRoll() + frame.getBonus();
            frame.setScore(scoreSum);
        }
        fullScore = scoreSum;
    }

    private void finishGame() {
        isFinished = true;
    }

}
