package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.*;
import ua.casten.bowling.repository.GameRepository;
import ua.casten.bowling.repository.LastFrameRepository;
import ua.casten.bowling.repository.RegularFrameRepository;
import ua.casten.bowling.util.BowlingUtil;

import java.awt.image.RGBImageFilter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BowlingServiceImpl implements BowlingService {

    private final GameRepository gameRepository;
    private final RegularFrameService regularFrameService;
    private final LastFrameService lastFrameService;

    @Autowired
    public BowlingServiceImpl(GameRepository gameRepository, RegularFrameService regularFrameService, LastFrameService lastFrameService) {
        this.gameRepository = gameRepository;
        this.regularFrameService = regularFrameService;
        this.lastFrameService = lastFrameService;
    }

    @Override
    public long startNewGame() {
        return gameRepository.save(new Game()).getId();
    }

    @Override
    public void makeRoll(Game game, String stringScore) throws BowlingException {
        if (game.isFinished()) {
            throw new BowlingRuntimeException("A new roll cannot be made, the game is over.");
        }

        var score = BowlingUtil.validateScore(stringScore);

        if (!regularFrameService.makeRoll(game, score)) {
            lastFrameService.makeRoll(game, score);
        }

        updateGameData(game);
    }

    private void updateGameData(Game game) {
        var regularFrames = BowlingUtil.sortFrames(game.getRegularFrames());
        var lastFrame = game.getLastFrame();

        if (lastFrame != null) {
            game.setFullScore(lastFrame.getScore());
        } else {
            game.setFullScore(regularFrames.get(regularFrames.size() - 1).getScore());
        }

        gameRepository.save(game);
    }
//
//    private void makeRegularFrameRoll(Frame frame, int score) {
//        if (frame.getFirstRoll() == null) {
//            frame.setFirstRoll(score);
//        } else if (frame.getSecondRoll() == null) {
//            makeSecondRegularFrameRoll(frame, score);
//        } else {
//            throw new BowlingRuntimeException("Cannot roll third time in regular frame.");
//        }
//    }
//
//    private void makeSecondRegularFrameRoll(Frame frame, int score) {
//        if (frame.getFirstRoll() + score > 10) {
//            throw new BowlingRuntimeException("Sum of rolls in regular frame cannot be greater than 10");
//        }
//        frame.setSecondRoll(score);
//    }
//
//    private void makeLastFrameRoll(Frame frame, int score) throws BowlingException {
//        if (frame.getFirstRoll() == null) {
//            frame.setFirstRoll(score);
//        } else if (frame.getSecondRoll() == null) {
//            makeSecondLastFrameRoll(frame, score);
//        } else if (frame.getThirdRoll() == null && (frame.isStrike() || frame.isSpare())) {
//            makeThirdLastFrameRoll(frame, score);
//        } else {
//            throw new BowlingRuntimeException("Cannot roll fourth time in last frame w.");
//        }
//    }
//
//    private void makeSecondLastFrameRoll(Frame frame, int score) throws BowlingException {
//        if (frame.getFirstRoll() + score > 10 && frame.getFirstRoll() != 10) {
//            throw new BowlingException("Sum of first and second rolls in last frame cannot be greater than 10 " +
//                    "without strike in first roll.");
//        }
//        frame.setSecondRoll(score);
//        if (!frame.isStrike() && !frame.isSpare()) {
//            frame.getGame().setFinished(true);
//        }
//    }
//
//    private void makeThirdLastFrameRoll(Frame frame, int score) throws BowlingException {
//        if (!frame.isSpare() && frame.getSecondRoll() != 10 && frame.getSecondRoll() + score > 10) {
//            throw new BowlingException("Sum of second and third rolls in last frame cannot be greater than 10 " +
//                    "without second roll strike or spare.");
//        }
//        frame.setThirdRoll(score);
//        frame.getGame().setFinished(true);
//    }
//
//    private void updateGameData(Game game) {
//        var fullScore = 0;
//        var frames = BowlingUtil.sortFrames(game.getFrames());
//
//        for (int i = 0; i < frames.size(); i++) {
//            var frame = frames.get(i);
//            var firstRoll = frame.getFirstRoll() == null ? 0 : frame.getFirstRoll();
//            var secondRoll = frame.getSecondRoll() == null ? 0 : frame.getSecondRoll();
//            var thirdRoll = frame.getThirdRoll() == null ? 0 : frame.getThirdRoll();
//            frame.setBonus(getBonus(i, frames));
//            fullScore += frame.getBonus() + firstRoll + secondRoll + thirdRoll;
//            frame.setScore(fullScore);
//        }
//
//        game.setFullScore(fullScore);
//        frameRepository.saveAll(game.getFrames());
//    }
//
//    private int getBonus(int frameIndex, List<Frame> frames) {
//        var frame = frames.get(frameIndex);
//
//        if (frameIndex == 9 || frames.size() <= frameIndex + 1) {
//            return 0;
//        }
//
//        if (frame.isStrike()) {
//            return getStrikeBonus(frameIndex, frames);
//        }
//
//        if (frame.isSpare()) {
//            return getSpareBonus(frameIndex, frames);
//        }
//
//        return 0;
//    }
//
//    private int getStrikeBonus(int frameIndex, List<Frame> frames) {
//        var bonus = 0;
//        var nextFrame = frames.get(frameIndex + 1);
//
//        if (nextFrame.isStrike()) {
//            bonus += 10;
//
//            if (frameIndex == 8) {
//                var secondRoll = nextFrame.getSecondRoll();
//                bonus += secondRoll == null ? 0 : secondRoll;
//            } else if (frames.size() > frameIndex + 2) {
//                var firstRoll = frames.get(frameIndex + 2).getFirstRoll();
//                bonus += firstRoll == null ? 0 : firstRoll;
//            }
//        } else {
//            var firstRoll = nextFrame.getFirstRoll() == null ? 0 : nextFrame.getFirstRoll();
//            var secondRoll = nextFrame.getSecondRoll() == null ? 0 : nextFrame.getSecondRoll();
//            bonus += firstRoll + secondRoll;
//        }
//
//        return bonus;
//    }
//
//    private int getSpareBonus(int frameIndex, List<Frame> frames) {
//        var bonus = 0;
//
//        if (frames.size() > frameIndex + 1) {
//            var firstRoll = frames.get(frameIndex + 1).getFirstRoll();
//            bonus += firstRoll == null ? 0 : firstRoll;
//        }
//
//        return bonus;
//    }

}
