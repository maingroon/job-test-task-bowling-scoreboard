package ua.casten.bowling.model;

import lombok.Getter;

@Getter
public class LastViewFrame extends RegularViewFrame {

    private final String thirdRoll;

    public LastViewFrame(String firstRoll, String secondRoll, String thirdRoll, int score) {
        super(firstRoll, secondRoll, score);
        this.thirdRoll = thirdRoll;
    }

}
