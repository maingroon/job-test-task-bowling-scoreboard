package ua.casten.bowling.util;

import ua.casten.bowling.model.*;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class BowlingUtil {

    private BowlingUtil() {
    }

    public static List<RegularFrame> sortFrames(List<RegularFrame> frameList) {
        return frameList.stream()
                .sorted(comparing(RegularFrame::getNumber))
                .collect(toList());
    }

}
