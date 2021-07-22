package ua.casten.bowling.util;

import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.ViewFrame;

import java.util.List;

public class FrameParser {

    private static final String STRIKE = "X";
    private static final String SPARE = "/";
    private static final String MISS = "-";
    private static final String EMPTY = "";

    private FrameParser() {
    }

    public static ViewFrame[] parseFrames(List<Frame> frames) {
        ViewFrame[] viewFrames = new ViewFrame[10];
        for (var i = 0; i < 9; i++) {
            viewFrames[i] = parseFrame(frames.get(i));
        }
        viewFrames[9] = parseLastFrame(frames.get(9));
        return viewFrames;
    }

    private static ViewFrame parseFrame(Frame frame) {
        var firstRoll = EMPTY;
        var secondRoll = EMPTY;

        if (frame.isInGame()) {
            firstRoll = parseFirstRoll(frame);
            secondRoll = parseSecondRoll(frame);
        }

        return new ViewFrame(
                firstRoll,
                secondRoll,
                EMPTY,
                frame.getScore(),
                frame.isInGame()
        );
    }

    private static String parseFirstRoll(Frame frame) {
        if (frame.isStrike()) {
            return EMPTY;
        }

        if (frame.getRollNumber() >= 2 && frame.getFirstRoll() == 0) {
            return MISS;
        }

        return String.valueOf(frame.getFirstRoll());
    }

    private static String parseSecondRoll(Frame frame) {
        if (frame.isStrike()) {
            return STRIKE;
        }

        if (frame.getRollNumber() <= 2) {
            return EMPTY;
        }

        if (frame.isSpare()) {
            return SPARE;
        }

        if (frame.getRollNumber() > 2 && frame.getSecondRoll() == 0) {
            return MISS;
        }

        return String.valueOf(frame.getSecondRoll());
    }

    private static ViewFrame parseLastFrame(Frame frame) {
        var firstRoll = EMPTY;
        var secondRoll = EMPTY;
        var thirdRoll = EMPTY;

        if (frame.isInGame()) {
            firstRoll = parseLastFrameFirstRoll(frame);
            secondRoll = parseLastFrameSecondRoll(frame);
            thirdRoll = parseLastFrameThirdRoll(frame);
        }

        return new ViewFrame(
                firstRoll,
                secondRoll,
                thirdRoll,
                frame.getScore(),
                frame.isInGame()
        );
    }

    private static String parseLastFrameFirstRoll(Frame lastFrame) {
        if (lastFrame.getFirstRoll() == 10) {
            return STRIKE;
        }

        if (lastFrame.getFirstRoll() == 0) {
            return MISS;
        }

        return String.valueOf(lastFrame.getFirstRoll());
    }

    private static String parseLastFrameSecondRoll(Frame lastFrame) {
        if (lastFrame.getRollNumber() < 3) {
            return EMPTY;
        }

        if (lastFrame.getSecondRoll() == 10) {
            return STRIKE;
        }

        if (lastFrame.isSpare()) {
            return SPARE;
        }

        if (lastFrame.getRollNumber() > 2 && lastFrame.getSecondRoll() == 0) {
            return MISS;
        }

        return String.valueOf(lastFrame.getSecondRoll());
    }

    private static String parseLastFrameThirdRoll(Frame lastFrame) {
        if (lastFrame.getRollNumber() < 4) {
            return EMPTY;
        }

        if (lastFrame.getThirdRoll() == 10) {
            return STRIKE;
        }

        if (lastFrame.getFirstRoll() == 10 && lastFrame.getSecondRoll() + lastFrame.getThirdRoll() == 10) {
            return SPARE;
        }

        if (lastFrame.getRollNumber() > 3 && lastFrame.getThirdRoll() == 0) {
            return MISS;
        }

        return String.valueOf(lastFrame.getThirdRoll());
    }

}
