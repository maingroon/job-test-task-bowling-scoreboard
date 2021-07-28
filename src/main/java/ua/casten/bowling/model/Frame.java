package ua.casten.bowling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static ua.casten.bowling.constant.Constants.MAX_ROLL_SCORE;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected long id;

    @Column(name = "number", nullable = false)
    protected int number;

    @Column(name = "first_roll")
    protected Integer firstRoll;

    @Column(name = "second_roll")
    protected Integer secondRoll;

    @Column(name = "score")
    protected Integer score;

    @Column(name = "bonus")
    protected Integer bonus;

    public boolean isStrike() {
        return firstRoll == MAX_ROLL_SCORE;
    }

    public boolean isSpare() {
        return !isStrike() && (firstRoll + secondRoll) == MAX_ROLL_SCORE;
    }

    public abstract boolean isPlayed();

}
