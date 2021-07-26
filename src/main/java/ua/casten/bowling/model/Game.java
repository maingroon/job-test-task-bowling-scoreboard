package ua.casten.bowling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "full_score")
    private int fullScore;

    @Column(name = "finished", nullable = false)
    private boolean isFinished;

    @Setter
    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<Frame> frames = new ArrayList<>(10);

    public void finishGame() {
        isFinished = true;
    }

}
