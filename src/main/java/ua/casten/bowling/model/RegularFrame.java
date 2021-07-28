package ua.casten.bowling.model;

import lombok.*;

import javax.persistence.*;

import static ua.casten.bowling.constant.Constants.MAX_ROLL_SCORE;

@Entity
@Getter
@Setter
@Table(name = "regular_frame")
public class RegularFrame extends Frame {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "game_fkey"))
    private Game game;

    @Override
    public boolean isPlayed() {
        return firstRoll != null && (isStrike() || secondRoll != null);
    }

}
