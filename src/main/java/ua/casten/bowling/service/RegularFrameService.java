package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.RegularFrame;
import ua.casten.bowling.repository.RegularFrameRepository;

import java.util.ArrayList;
import java.util.List;

import static ua.casten.bowling.constant.Constants.MAX_ROLL_SCORE;

@Service
public class RegularFrameService implements FrameService {

    private final RegularFrameRepository regularFrameRepository;

    @Autowired
    public RegularFrameService(RegularFrameRepository regularFrameRepository) {
        this.regularFrameRepository = regularFrameRepository;
    }

    @Override
    public void makeRoll(Game game, int score) throws BowlingException {
        var lastPlayedFrame = game.getLastPlayedFrame();

        if (lastPlayedFrame == null || lastPlayedFrame.isPlayed()) {
            var number = lastPlayedFrame == null ? 1 : lastPlayedFrame.getNumber() + 1;
            lastPlayedFrame = createNewFrame(game, number);
        }

        if (lastPlayedFrame.getFirstRoll() == null) {
            lastPlayedFrame.setFirstRoll(score);
        } else if (lastPlayedFrame.getSecondRoll() == null) {
            makeSecondRoll(lastPlayedFrame, score);
        } else {
            throw new BowlingRuntimeException("Cannot roll third time in regular frame.");
        }
    }

    private RegularFrame createNewFrame(Game game, int number) {
        var frame = new RegularFrame();
        frame.setNumber(number);
        frame.setGame(game);
        game.getRegularFrames().add(frame);

        return frame;
    }

    private void makeSecondRoll(Frame frame, int score) throws BowlingException {
        if (!frame.isStrike() && frame.getFirstRoll() + score > MAX_ROLL_SCORE) {
            throw new BowlingException("Sum of rolls in regular frame cannot be greater than 10");
        }
        frame.setSecondRoll(score);
    }

    @Override
    public void updateFramesData(Game game) {
        var score = 0;
        var frames = game.getAllSortedFrames();
        List<RegularFrame> regularFrames = new ArrayList<>();

        for (var i = 0; i < frames.size() && i < 9; i++) {
            var frame = frames.get(i);
            frame.setBonus(getFrameBonus(frames, i));
            var secondRoll = frame.getSecondRoll() == null ? 0 : frame.getSecondRoll();
            score += frame.getFirstRoll() + secondRoll + frame.getBonus();
            frame.setScore(score);
            regularFrames.add((RegularFrame) frame);
        }

        regularFrameRepository.saveAll(regularFrames);
    }

    private int getFrameBonus(List<Frame> frames, int frameIndex) {
        var bonus = 0;
        var frame = frames.get(frameIndex);

        if (frame.getFirstRoll() == null) {
            return 0;
        }

        if (frame.isStrike()) {
            bonus += getStrikeBonus(frames, frameIndex);
        } else if (frame.getSecondRoll() != null && frame.isSpare()) {
            bonus += getSpareBonus(frames, frameIndex);
        }

        return bonus;
    }

    private int getStrikeBonus(List<Frame> frames, int frameIndex) {
        var bonus = getSpareBonus(frames, frameIndex);
        var nextFrame = frameIndex + 1 < frames.size() ? frames.get(frameIndex + 1) : null;

        if (nextFrame == null || nextFrame.getFirstRoll() == null) {
            return bonus;
        }

        if (nextFrame.isStrike() && frameIndex != 8) {
            bonus += getSpareBonus(frames, frameIndex + 1);
        } else {
            bonus += nextFrame.getSecondRoll() == null ? 0 : nextFrame.getSecondRoll();
        }

        return bonus;
    }

    private int getSpareBonus(List<Frame> frames, int frameIndex) {
        var bonus = 0;

        if (frameIndex + 1 < frames.size()) {
            var nextFrame = frames.get(frameIndex + 1);
            bonus += nextFrame.getFirstRoll() == null ? 0 : nextFrame.getFirstRoll();
        }

        return bonus;
    }

}
