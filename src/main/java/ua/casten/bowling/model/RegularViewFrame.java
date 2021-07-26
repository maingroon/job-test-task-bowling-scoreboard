package ua.casten.bowling.model;

import lombok.Getter;

@Getter
public class RegularViewFrame {

    private final String firstRoll;
    private final String secondRoll;
    private final int score;

    public RegularViewFrame(String firstRoll, String secondRoll, int score) {
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.score = score;
    }

}
