package ua.casten.bowling.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
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

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "first_roll")
    private Integer firstRoll;

    @Column(name = "second_roll")
    private Integer secondRoll;

    @Column(name = "third_roll")
    private Integer thirdRoll;

    @Column(name = "bonus")
    private Integer bonus;

    @Column(name = "score")
    private Integer score;

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
