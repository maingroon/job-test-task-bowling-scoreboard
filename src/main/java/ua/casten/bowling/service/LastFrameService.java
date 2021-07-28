package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.LastFrame;
import ua.casten.bowling.repository.LastFrameRepository;

import static ua.casten.bowling.constant.Constants.MAX_ROLL_SCORE;

@Service
public class LastFrameService implements FrameService {

    private final LastFrameRepository lastFrameRepository;

    @Autowired
    public LastFrameService(LastFrameRepository lastFrameRepository) {
        this.lastFrameRepository = lastFrameRepository;
    }

    @Override
    public void makeRoll(Game game, int score) throws BowlingException {
        var lastFrame = game.getLastFrame();

        if (lastFrame == null) {
            lastFrame = createNewFrame(game);
        }

        if (lastFrame.getFirstRoll() == null) {
            lastFrame.setFirstRoll(score);
        } else if (lastFrame.getSecondRoll() == null) {
            makeSecondLastFrameRoll(lastFrame, score);
        } else if (lastFrame.getThirdRoll() == null) {
            makeThirdLastFrameRoll(lastFrame, score);
        } else {
            throw new BowlingRuntimeException("Cannot roll fourth time in last frame.");
        }

        if (lastFrame.isPlayed()) {
            game.setFinished(true);
        }
    }

    private LastFrame createNewFrame(Game game) {
        var frame = new LastFrame();
        frame.setGame(game);
        game.setLastFrame(frame);

        return frame;
    }

    private void makeSecondLastFrameRoll(LastFrame lastFrame, int score) throws BowlingException {
    if (lastFrame.getFirstRoll() != MAX_ROLL_SCORE && lastFrame.getFirstRoll() + score > MAX_ROLL_SCORE) {
        throw new BowlingException("Sum of first and second rolls in last frame cannot be greater than 10 " +
                "without strike in first roll.");
    }

    lastFrame.setSecondRoll(score);
}

    private void makeThirdLastFrameRoll(LastFrame lastFrame, int score) throws BowlingException {
        if (lastFrame.isPlayed()) {
            throw new BowlingException("Sum of second and third rolls in last frame cannot be greater than 10 " +
                    "without second roll strike or spare.");
        }

        lastFrame.setThirdRoll(score);
    }

    @Override
    public void updateFramesData(Game game) {
        var score = game.getAllSortedFrames().get(8).getScore();
        var lastFrame = game.getLastFrame();
        var secondRoll = lastFrame.getSecondRoll() == null ? 0 : lastFrame.getSecondRoll();
        var thirdRoll = lastFrame.getThirdRoll() == null ? 0 : lastFrame.getThirdRoll();

        score += lastFrame.getFirstRoll() + secondRoll + thirdRoll;
        lastFrame.setScore(score);
        lastFrameRepository.save(lastFrame);
    }

}
