package ua.casten.bowling.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "regular_frame")
public class RegularFrame extends Frame {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final int MAX_SCORE = 10;

    @Column(name = "bonus")
    protected Integer bonus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "game_fkey"))
    private Game game;

    public boolean isStrike() {
        return firstRoll == MAX_SCORE;
    }

    public boolean isSpare() {
        return !isStrike() && (firstRoll + secondRoll) == MAX_SCORE;
    }

}
