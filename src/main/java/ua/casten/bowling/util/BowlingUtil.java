package ua.casten.bowling.util;

import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.ViewFrame;

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

    public static List<Frame> sortFrames(List<Frame> frameList) {
        return frameList.stream()
                .sorted(comparing(Frame::getNumber))
                .collect(toList());
    }

    public static List<ViewFrame> parseFrames(Game game) {
        var frameList = sortFrames(game.getFrames());
        List<ViewFrame> viewFrames = new ArrayList<>();

        int i = 0;
        for (; i < frameList.size(); i++) {
            viewFrames.add(parseFrame(frameList.get(i)));
        }

        for (; i < 10; i++) {
            viewFrames.add(new ViewFrame(EMPTY, EMPTY, EMPTY, -1));
        }

        return viewFrames;
    }

    private static ViewFrame parseFrame(Frame frame) {
        String firstRoll = parseFirstRoll(frame);
        String secondRoll = parseSecondRoll(frame);
        String thirdRoll = parseThirdRoll(frame);

        return new ViewFrame(
                firstRoll,
                secondRoll,
                thirdRoll,
                frame.getScore() == null ? 0 : frame.getScore()
        );
    }

    private static String parseFirstRoll(Frame frame) {
        var firstRoll = frame.getFirstRoll();
        if (firstRoll == null || (frame.isStrike() && frame.getNumber() != 10)) {
            return EMPTY;
        }

        if (firstRoll == 0) {
            return MISS;
        }

        if (frame.isStrike() && frame.getNumber() == 10) {
            return STRIKE;
        }

        return firstRoll.toString();
    }

    private static String parseSecondRoll(Frame frame) {
        var secondRoll = frame.getSecondRoll();
        if (frame.getFirstRoll() != null && secondRoll == null && frame.isStrike() && frame.getNumber() != 10) {
            return STRIKE;
        }

        if (secondRoll == null) {
            return EMPTY;
        }

        if (secondRoll == 0) {
            return MISS;
        }

        if (secondRoll == 10 && frame.getFirstRoll() != null && frame.getFirstRoll() == 10) {
            return STRIKE;
        }

        if (frame.isSpare()) {
            return SPARE;
        }

        return secondRoll.toString();
    }

    private static String parseThirdRoll(Frame frame) {
        var thirdRoll = frame.getThirdRoll();
        if (frame.getSecondRoll() == null || thirdRoll == null) {
            return EMPTY;
        }

        if (thirdRoll == 0) {
            return MISS;
        }

        if (thirdRoll == 10 && frame.getSecondRoll() != 0) {
            return STRIKE;
        }

        if (thirdRoll + frame.getSecondRoll() == 10) {
            return SPARE;
        }

        return thirdRoll.toString();
    }

}
