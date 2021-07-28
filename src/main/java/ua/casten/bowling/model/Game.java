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

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RegularFrame> regularFrames = new ArrayList<>(9);

    @OneToOne(mappedBy = "game", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private LastFrame lastFrame;

    public Frame getLastPlayedFrame() {
        var frames = getAllSortedFrames();
        if (frames.isEmpty()) {
            return null;
        }

        return frames.get(frames.size() - 1);
    }

    public List<Frame> getAllSortedFrames() {
        List<Frame> frames = new ArrayList<>(BowlingUtil.sortFrames(regularFrames));
        if (lastFrame != null) {
            frames.add(lastFrame);
        }

        return frames;
    }

}
