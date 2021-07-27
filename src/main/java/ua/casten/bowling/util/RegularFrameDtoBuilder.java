package ua.casten.bowling.util;

import ua.casten.bowling.model.RegularFrame;
import ua.casten.bowling.model.RegularFrameDto;

import java.util.ArrayList;
import java.util.List;

import static ua.casten.bowling.constant.Constants.*;
import static ua.casten.bowling.constant.Constants.SPARE;

public class RegularFrameDtoBuilder {

    private RegularFrameDtoBuilder() {
    }

    public static List<RegularFrameDto> transferRegularFrames(List<RegularFrame> regularFrames) {
        List<RegularFrameDto> regularFramesDTO = new ArrayList<>(9);
        int i = 0;

        for (; i < regularFrames.size(); i++) {
            regularFramesDTO.add(transferRegularFrame(regularFrames.get(i)));
        }

        for (; i < 9; i++) {
            regularFramesDTO.add(new RegularFrameDto(EMPTY, EMPTY, -1));
        }

        return regularFramesDTO;
    }

    private static RegularFrameDto transferRegularFrame(RegularFrame regularFrame) {
        var firstRoll = transferRegularFirstFrame(regularFrame);
        var secondRoll = transferRegularSecondFrame(regularFrame);
        var score = regularFrame.getScore();

        return new RegularFrameDto(
                firstRoll,
                secondRoll,
                score
        );
    }

    private static String transferRegularFirstFrame(RegularFrame regularFrame) {
        var firstRoll = regularFrame.getFirstRoll();

        if (firstRoll == null || regularFrame.isStrike()) {
            return EMPTY;
        }

        if (firstRoll == 0) {
            return MISS;
        }

        return firstRoll.toString();
    }

    private static String transferRegularSecondFrame(RegularFrame regularFrame) {
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

}
