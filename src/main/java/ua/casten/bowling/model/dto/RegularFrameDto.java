package ua.casten.bowling.model.dto;

import lombok.Getter;

@Getter
public class RegularFrameDto {

    private final String firstRoll;
    private final String secondRoll;
    private final int score;

    public RegularFrameDto(String firstRoll, String secondRoll, int score) {
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.score = score;
    }

}
