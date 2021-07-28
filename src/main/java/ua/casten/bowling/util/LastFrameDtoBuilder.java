package ua.casten.bowling.util;

import ua.casten.bowling.model.LastFrame;
import ua.casten.bowling.model.dto.LastFrameDto;

import static ua.casten.bowling.constant.Constants.*;

public class LastFrameDtoBuilder {

    private LastFrameDtoBuilder() {
    }

    public static LastFrameDto transferLastFrame(LastFrame lastFrame) {
        var firstRoll = EMPTY;
        var secondRoll = EMPTY;
        var thirdRoll = EMPTY;
        var score = -1;

        if (lastFrame != null) {
            firstRoll = transferLastFrameFirstRoll(lastFrame);
            secondRoll = transferLastFrameSecondRoll(lastFrame);
            thirdRoll = transferLastFrameFThirdRoll(lastFrame);
            score = lastFrame.getScore();
        }

        return new LastFrameDto(
                firstRoll,
                secondRoll,
                thirdRoll,
                score
        );
    }

    private static String transferLastFrameFirstRoll(LastFrame lastFrame) {
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

    private static String transferLastFrameSecondRoll(LastFrame lastFrame) {
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

    private static String transferLastFrameFThirdRoll(LastFrame lastFrame) {
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

}
