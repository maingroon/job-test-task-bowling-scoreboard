package ua.casten.bowling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private final List<Frame> frames = new ArrayList<>(10);

    @Column(name = "current_frame_index", nullable = false)
    private int currentFrameIndex;

    @Column(name = "finished", nullable = false)
    private boolean isFinished;

    public Game() {
        currentFrameIndex = 0;
        for (var i = 0; i < 10; i++) {
            frames.add(new Frame());
        }
        isFinished = false;
    }

    public void increaseCurrentFrameIndex() {
        currentFrameIndex++;
    }

    public void finishGame() {
        isFinished = true;
    }

}
