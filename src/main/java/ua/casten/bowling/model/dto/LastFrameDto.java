package ua.casten.bowling.model.dto;

import lombok.Getter;

@Getter
public class LastFrameDto extends RegularFrameDto {

    private final String thirdRoll;

    public LastFrameDto(String firstRoll, String secondRoll, String thirdRoll, int score) {
        super(firstRoll, secondRoll, score);
        this.thirdRoll = thirdRoll;
    }

}
