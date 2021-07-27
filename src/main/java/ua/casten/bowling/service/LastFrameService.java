package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.LastFrame;
import ua.casten.bowling.model.RegularFrame;
import ua.casten.bowling.repository.LastFrameRepository;
import ua.casten.bowling.util.BowlingUtil;

import java.util.List;

import static ua.casten.bowling.constant.Constants.MAX_ROLL_SCORE;

@Service
public class LastFrameService {

    private final LastFrameRepository lastFrameRepository;
    private final RegularFrameService regularFrameService;

    @Autowired
    public LastFrameService(LastFrameRepository lastFrameRepository, RegularFrameService regularFrameService) {
        this.lastFrameRepository = lastFrameRepository;
        this.regularFrameService = regularFrameService;
    }

    public void makeRoll(Game game, int score) throws BowlingException {
        var lastFrame = getCurrentFrame(game);

        if (lastFrame.getFirstRoll() == null) {
            lastFrame.setFirstRoll(score);
        } else if (lastFrame.getSecondRoll() == null) {
            makeSecondLastFrameRoll(game, lastFrame, score);
        } else if (lastFrame.getThirdRoll() == null) {
            makeThirdLastFrameRoll(game, lastFrame, score);
        } else {
            throw new BowlingRuntimeException("Cannot roll fourth time in last frame w.");
        }

        var regularFrames = BowlingUtil.sortFrames(game.getRegularFrames());
        updateFrameData(regularFrames, lastFrame);
    }

    private void makeSecondLastFrameRoll(Game game, LastFrame lastFrame, int score) throws BowlingException {
        if (lastFrame.getFirstRoll() != MAX_ROLL_SCORE && lastFrame.getFirstRoll() + score > MAX_ROLL_SCORE) {
            throw new BowlingException("Sum of first and second rolls in last frame cannot be greater than 10 " +
                    "without strike in first roll.");
        }

        lastFrame.setSecondRoll(score);

        if (lastFrame.getFirstRoll() != MAX_ROLL_SCORE && lastFrame.getFirstRoll() + score < MAX_ROLL_SCORE) {
            game.setFinished(true);
        }
    }

    private void makeThirdLastFrameRoll(Game game, LastFrame lastFrame, int score) throws BowlingException {
        if (lastFrame.getSecondRoll() != MAX_ROLL_SCORE
            && lastFrame.getFirstRoll() + lastFrame.getSecondRoll() != MAX_ROLL_SCORE
            && lastFrame.getSecondRoll() + lastFrame.getThirdRoll() > MAX_ROLL_SCORE) {
            throw new BowlingException("Sum of second and third rolls in last frame cannot be greater than 10 " +
                    "without second roll strike or spare.");
        }

        lastFrame.setThirdRoll(score);
        game.setFinished(true);
    }

    public void updateFrameData(List<RegularFrame> frames, LastFrame lastFrame) {
        lastFrameRepository.save(lastFrame);
        regularFrameService.updateFramesData(frames, lastFrame);

        var score = frames.get(8).getScore();
        var secondRoll = lastFrame.getSecondRoll() == null ? 0 : lastFrame.getSecondRoll();
        var thirdRoll = lastFrame.getThirdRoll() == null ? 0 : lastFrame.getThirdRoll();

        score += lastFrame.getFirstRoll() + secondRoll + thirdRoll;
        lastFrame.setScore(score);
        lastFrameRepository.save(lastFrame);
    }

    private LastFrame getCurrentFrame(Game game) {
        LastFrame frame = game.getLastFrame();

        if (frame == null) {
            frame = new LastFrame();
            frame.setGame(game);
            game.setLastFrame(frame);
        } else {
            frame = game.getLastFrame();
        }

        return frame;
    }

}
