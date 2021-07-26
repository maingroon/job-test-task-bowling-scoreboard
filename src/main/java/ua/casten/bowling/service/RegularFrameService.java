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
            if (!regularFrame.isStrike() && regularFrame.getFirstRoll() + score > 10) {
                throw new BowlingRuntimeException("Sum of rolls in regular frame cannot be greater than 10");
            }
            regularFrame.setSecondRoll(score);
        } else {
            throw new BowlingRuntimeException("Cannot roll third time in regular frame.");
        }

        updateFramesData(frames, game.getLastFrame());
        return true;
    }

    public void updateFramesData(List<RegularFrame> frames, LastFrame lastFrame) {
        var score = 0;

        for (var i = 0; i < frames.size(); i++) {
            var frame = frames.get(i);
            frame.setBonus(getFrameBonus(frames, lastFrame, i));
            var firstRoll = frame.getFirstRoll() == null ? 0 : frame.getFirstRoll();
            var secondRoll = frame.getSecondRoll() == null ? 0 : frame.getSecondRoll();
            score += firstRoll + secondRoll + frame.getBonus();
            frame.setScore(score);
        }

        regularFrameRepository.saveAll(frames);
    }

    private int getFrameBonus(List<RegularFrame> frames, LastFrame lastFrame, int frameIndex) {
        var bonus = 0;
        var frame = frames.get(frameIndex);

        if (frame.getFirstRoll() != null && frame.isStrike()) {
            bonus += getStrikeBonus(frames, lastFrame, frameIndex);
        } else if (frame.getFirstRoll() != null && frame.getSecondRoll() != null && frame.isSpare()) {
            bonus += getSpareBonus(frames, lastFrame, frameIndex);
        }

        return bonus;
    }

    private int getStrikeBonus(List<RegularFrame> frames, LastFrame lastFrame, int frameIndex) {
        if (frameIndex + 1 != 9 && frameIndex + 1 >= frames.size()) {
            return 0;
        }

        var bonus = getSpareBonus(frames, lastFrame, frameIndex);

        if (frameIndex < 7) {
            var nextFrame = frames.get(frameIndex + 1);

            if (nextFrame.getFirstRoll() != null && nextFrame.isStrike() && frameIndex + 2 < frames.size()) {
                bonus += getSpareBonus(frames, lastFrame, frameIndex + 2);
            } else if (nextFrame.getFirstRoll() != null && !nextFrame.isStrike()) {
                bonus += nextFrame.getSecondRoll() == null ? 0 : nextFrame.getSecondRoll();
            }
        } else if (frameIndex == 7) {
            bonus += getSpareBonus(frames, lastFrame, frameIndex + 1);
        } else if (lastFrame != null && frames.get(frameIndex).isStrike()) {
            bonus += lastFrame.getSecondRoll() == null ? 0 : lastFrame.getSecondRoll();
        }

        return bonus;
    }

    private int getSpareBonus(List<RegularFrame> frames, LastFrame lastFrame, int frameIndex) {

        var bonus = 0;

        if (frameIndex < 8 && frameIndex + 1 < frames.size()) {
            var nextFrame = frames.get(frameIndex + 1);
            bonus += nextFrame.getFirstRoll() == null ? 0 : nextFrame.getFirstRoll();
        } else if (frameIndex >= 8 && lastFrame != null) {
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
