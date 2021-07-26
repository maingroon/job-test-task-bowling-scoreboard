package ua.casten.bowling.util;

import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class BowlingUtil {

    private static final String STRIKE = "X";
    private static final String SPARE = "/";
    private static final String MISS = "-";
    private static final String EMPTY = "";

    private BowlingUtil() {
    }

    public static List<RegularFrame> sortFrames(List<RegularFrame> frameList) {
        return frameList.stream()
                .sorted(comparing(RegularFrame::getNumber))
                .collect(toList());
    }

    public static int validateScore(String stringScore) throws BowlingException {
        int score;

        try {
            score = Integer.parseInt(stringScore.trim());
        } catch (NumberFormatException e) {
            throw new BowlingException("Enter valid score (without symbols and spaces).");
        }

        if (score < 0 || score > 10) {
            throw new BowlingException("Score cannot be less than 0 or greater than 10.");
        }

        return score;
    }

    public static List<RegularViewFrame> parseRegularFrames(Game game) {
        var regularFrames = sortFrames(game.getRegularFrames());
        List<RegularViewFrame> regularViewFrames = new ArrayList<>(9);
        int i = 0;

        for (; i < regularFrames.size(); i++) {
            regularViewFrames.add(parseRegularFrame(regularFrames.get(i)));
        }

        for (; i < 9; i++) {
            regularViewFrames.add(new RegularViewFrame(EMPTY, EMPTY, -1));
        }

        return regularViewFrames;
    }

    private static RegularViewFrame parseRegularFrame(RegularFrame regularFrame) {
        var firstRoll = parseRegularFirstFrame(regularFrame);
        var secondRoll = parseRegularSecondFrame(regularFrame);
        var score = regularFrame.getScore();

        return new RegularViewFrame(
                firstRoll,
                secondRoll,
                score
        );
    }

    private static String parseRegularFirstFrame(RegularFrame regularFrame) {
        var firstRoll = regularFrame.getFirstRoll();

        if (firstRoll == null || regularFrame.isStrike()) {
            return EMPTY;
        }

        if (firstRoll == 0) {
            return MISS;
        }

        return firstRoll.toString();
    }

    private static String parseRegularSecondFrame(RegularFrame regularFrame) {
        var secondRoll = regularFrame.getSecondRoll();

        if (regularFrame.getFirstRoll() != null && regularFrame.isStrike()) {
            return STRIKE;
        }

        if (secondRoll == null) {
            return EMPTY;
        }

        if (secondRoll == 0) {
            return MISS;
        }

        if (regularFrame.isSpare()) {
            return SPARE;
        }

        return secondRoll.toString();
    }

    public static LastViewFrame parseLastFrame(Game game) {
        var lastFrame = game.getLastFrame();

        var firstRoll = EMPTY;
        var secondRoll = EMPTY;
        var thirdRoll = EMPTY;
        var score = -1;

        if (lastFrame != null) {
            firstRoll = parseLastFrameFirstRoll(lastFrame);
            secondRoll = parseLastFrameSecondRoll(lastFrame);
            thirdRoll = parseLastFrameFThirdRoll(lastFrame);
            score = lastFrame.getScore();
        }

        return new LastViewFrame(
                firstRoll,
                secondRoll,
                thirdRoll,
                score
        );
    }

    private static String parseLastFrameFirstRoll(LastFrame lastFrame) {
        var firstRoll = lastFrame.getFirstRoll();

        if (firstRoll == null) {
            return EMPTY;
        }

        if (firstRoll == 10) {
            return STRIKE;
        }

        if (firstRoll == 0) {
            return MISS;
        }

        return firstRoll.toString();
    }

    private static String parseLastFrameSecondRoll(LastFrame lastFrame) {
        var secondRoll = lastFrame.getSecondRoll();

        if (secondRoll == null) {
            return EMPTY;
        }

        if (secondRoll == 10 && lastFrame.getFirstRoll() != 0) {
            return STRIKE;
        }

        if (secondRoll == 0) {
            return MISS;
        }

        if (secondRoll + lastFrame.getFirstRoll() == 10) {
            return SPARE;
        }

        return secondRoll.toString();
    }

    private static String parseLastFrameFThirdRoll(LastFrame lastFrame) {
        var thirdRoll = lastFrame.getThirdRoll();

        if (thirdRoll == null) {
            return EMPTY;
        }

        if (thirdRoll == 0) {
            return MISS;
        }

        if (thirdRoll == 10 && lastFrame.getSecondRoll() != 0) {
            return STRIKE;
        }

        if (thirdRoll + lastFrame.getSecondRoll() == 10) {
            return SPARE;
        }

        return thirdRoll.toString();
    }

//    private static ViewFrame parseFrame(Frame frame) {
//        String firstRoll = parseFirstRoll(frame);
//        String secondRoll = parseSecondRoll(frame);
//        String thirdRoll = parseThirdRoll(frame);
//
//        return new ViewFrame(
//                firstRoll,
//                secondRoll,
//                thirdRoll,
//                frame.getScore() == null ? 0 : frame.getScore()
//        );
//    }
//
//    private static String parseFirstRoll(Frame frame) {
//        var firstRoll = frame.getFirstRoll();
//        if (firstRoll == null || (frame.isStrike() && frame.getNumber() != 10)) {
//            return EMPTY;
//        }
//
//        if (firstRoll == 0) {
//            return MISS;
//        }
//
//        if (frame.isStrike() && frame.getNumber() == 10) {
//            return STRIKE;
//        }
//
//        return firstRoll.toString();
//    }
//
//    private static String parseSecondRoll(Frame frame) {
//        var secondRoll = frame.getSecondRoll();
//        if (frame.getFirstRoll() != null && secondRoll == null && frame.isStrike() && frame.getNumber() != 10) {
//            return STRIKE;
//        }
//
//        if (secondRoll == null) {
//            return EMPTY;
//        }
//
//        if (secondRoll == 0) {
//            return MISS;
//        }
//
//        if (secondRoll == 10 && frame.getFirstRoll() != null && frame.getFirstRoll() == 10) {
//            return STRIKE;
//        }
//
//        if (frame.isSpare()) {
//            return SPARE;
//        }
//
//        return secondRoll.toString();
//    }
//
//    private static String parseThirdRoll(Frame frame) {
//        var thirdRoll = frame.getThirdRoll();
//        if (frame.getSecondRoll() == null || thirdRoll == null) {
//            return EMPTY;
//        }
//
//        if (thirdRoll == 0) {
//            return MISS;
//        }
//
//        if (thirdRoll == 10 && frame.getSecondRoll() != 0) {
//            return STRIKE;
//        }
//
//        if (thirdRoll + frame.getSecondRoll() == 10) {
//            return SPARE;
//        }
//
//        return thirdRoll.toString();
//    }
//
}
