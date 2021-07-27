package ua.casten.bowling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.casten.bowling.service.BowlingService;
import ua.casten.bowling.util.BowlingUtil;

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

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<RegularFrame> regularFrames = new ArrayList<>(9);

    @OneToOne(mappedBy = "game", fetch = FetchType.LAZY)
    private LastFrame lastFrame;

    public Frame getLastPlayedFrame() {
        if (lastFrame == null) {
            return !regularFrames.isEmpty() ? regularFrames.get(regularFrames.size() - 1) : null;
        } else {
            return lastFrame;
        }
    }

    public List<Frame> getAllSortedFrames() {
        List<Frame> frames = new ArrayList<>(BowlingUtil.sortFrames(regularFrames));
        if (lastFrame != null) {
            frames.add(lastFrame);
        }

        return frames;
    }

}
