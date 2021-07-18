package ua.casten.bowling.model;

import lombok.Getter;

@Getter
public class ViewFrame {

    private final String firstRoll;
    private final String secondRoll;
    private final String thirdRoll;
    private final int score;

    private boolean inGame;

    public ViewFrame(String firstRoll, String secondRoll, String thirdRoll, int score, boolean inGame) {
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.thirdRoll = thirdRoll;
        this.score = score;
        this.inGame = inGame;
    }

}
