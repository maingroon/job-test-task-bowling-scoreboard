package ua.casten.bowling.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "last_frame")
public class LastFrame extends Frame {

    @Column(name = "third_roll")
    private Integer thirdRoll;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "game_fkey"))
    private Game game;

    public LastFrame() {
        number = 10;
    }

    @Override
    public boolean isPlayed() {
        return firstRoll != null && secondRoll != null && (thirdRoll != null || (!isStrike() && !isSpare()));
    }
}
