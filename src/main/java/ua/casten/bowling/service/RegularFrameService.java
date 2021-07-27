package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.LastFrame;
import ua.casten.bowling.model.RegularFrame;
import ua.casten.bowling.repository.RegularFrameRepository;
import ua.casten.bowling.util.BowlingUtil;

import java.util.List;

import static ua.casten.bowling.constant.Constants.MAX_ROLL_SCORE;

@Service
public class RegularFrameService {

    private final RegularFrameRepository regularFrameRepository;

    @Autowired
    public RegularFrameService(RegularFrameRepository regularFrameRepository) {
        this.regularFrameRepository = regularFrameRepository;
    }

    public boolean makeRoll(Game game, int score) {
        var frames = game.getRegularFrames();

        if (frames.size() == 9 && (frames.get(8).isStrike() || frames.get(8).getSecondRoll() != null)) {
            return false;
        }

        frames = BowlingUtil.sortFrames(frames);
        var regularFrame = getCurrentFrame(game, frames);

        if (regularFrame.getFirstRoll() == null) {
            regularFrame.setFirstRoll(score);
        } else if (regularFrame.getSecondRoll() == null) {
            makeSecondRoll(regularFrame, score);
        } else {
            throw new BowlingRuntimeException("Cannot roll third time in regular frame.");
        }

        updateFramesData(frames, game.getLastFrame());
        return true;
    }

    private void makeSecondRoll(RegularFrame regularFrame, int score) {
        if (!regularFrame.isStrike() && regularFrame.getFirstRoll() + score > MAX_ROLL_SCORE) {
            throw new BowlingRuntimeException("Sum of rolls in regular frame cannot be greater than 10");
        }
        regularFrame.setSecondRoll(score);
    }

    public void updateFramesData(List<RegularFrame> frames, LastFrame lastFrame) {
        var score = 0;

        for (var i = 0; i < frames.size(); i++) {
            var frame = frames.get(i);
            frame.setBonus(getFrameBonus(frames, lastFrame, i));
            var secondRoll = frame.getSecondRoll() == null ? 0 : frame.getSecondRoll();
            score += frame.getFirstRoll() + secondRoll + frame.getBonus();
            frame.setScore(score);
        }

        regularFrameRepository.saveAll(frames);
    }

    private int getFrameBonus(List<RegularFrame> frames, LastFrame lastFrame, int frameIndex) {
        var bonus = 0;
        var frame = frames.get(frameIndex);

        if ((frameIndex != 8 && frameIndex + 1 >= frames.size()) || frame.getFirstRoll() == null) {
            return 0;
        }

        if (frame.isStrike()) {
            bonus += getStrikeBonus(frames, lastFrame, frameIndex);
        } else if (frame.getSecondRoll() != null && frame.isSpare()) {
            bonus += getSpareBonus(frames, lastFrame, frameIndex);
        }

        return bonus;
    }

    private int getStrikeBonus(List<RegularFrame> frames, LastFrame lastFrame, int frameIndex) {
        var bonus = getSpareBonus(frames, lastFrame, frameIndex);
        var nextFrame = frameIndex < 7 ? frames.get(frameIndex + 1) : lastFrame;

        if (nextFrame == null || nextFrame.getFirstRoll() == null) {
            return bonus;
        }

        if (frameIndex < 8) {
            if (nextFrame.getFirstRoll() == MAX_ROLL_SCORE) {
                bonus += getSpareBonus(frames, lastFrame, frameIndex + 2);
            } else {
                bonus += nextFrame.getSecondRoll() == null ? 0 : nextFrame.getSecondRoll();
            }
        } else if (frameIndex == 8 && nextFrame.getFirstRoll() == MAX_ROLL_SCORE) {
            bonus += lastFrame.getSecondRoll() == null ? 0 : lastFrame.getSecondRoll();
        }

        return bonus;
    }

    private int getSpareBonus(List<RegularFrame> frames, LastFrame lastFrame, int frameIndex) {
        var bonus = 0;

        if (frameIndex + 1 < frames.size()) {
            var nextFrame = frames.get(frameIndex + 1);
            bonus += nextFrame.getFirstRoll() == null ? 0 : nextFrame.getFirstRoll();
        } else if (lastFrame != null) {
            bonus += lastFrame.getFirstRoll() == null ? 0 : lastFrame.getFirstRoll();
        }

        return bonus;
    }


    private RegularFrame getCurrentFrame(Game game, List<RegularFrame> frames) {
        RegularFrame frame;
        var frameIndex = frames.size() - 1;

        if (frames.isEmpty() || (frames.get(frameIndex).getSecondRoll() != null || frames.get(frameIndex).isStrike())) {
            frame = new RegularFrame();
            frame.setNumber(frameIndex + 1);
            frame.setGame(game);
            game.getRegularFrames().add(frame);
            frames.add(frame);
        } else {
            frame = frames.get(frameIndex);
        }

        return frame;
    }

}
