package ua.casten.bowling.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Frame {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final int MAX_SCORE = 10;

    private int score;
    private int firstRoll;
    private int secondRoll;
    private int thirdRoll;
    private int rollNumber;
    private int bonus;

    private boolean inGame;

    public Frame() {
        rollNumber = 1;
    }

    public boolean isStrike() {
        return firstRoll == MAX_SCORE;
    }

    public boolean isSpare() {
        return !isStrike() && (firstRoll + secondRoll) == MAX_SCORE;
    }

}
