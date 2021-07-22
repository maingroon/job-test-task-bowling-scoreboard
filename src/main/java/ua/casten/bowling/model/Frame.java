package ua.casten.bowling.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "frame")
public class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final int MAX_SCORE = 10;

    @Column(name = "first_roll", nullable = false)
    private int firstRoll;

    @Column(name = "second_roll", nullable = false)
    private int secondRoll;

    @Column(name = "third_roll", nullable = false)
    private int thirdRoll;

    @Column(name = "roll_number", nullable = false)
    private int rollNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "game_fkey"))
    private Game game;

    @Transient
    private int bonus;

    @Transient
    private int score;

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
