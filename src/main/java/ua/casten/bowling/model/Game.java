package ua.casten.bowling.model;

import lombok.Getter;

@Getter
public class Game {

    private final Frame[] frames;
    private int currentFrameIndex;
    private boolean isFinished;

    public Game() {
        currentFrameIndex = 0;
        frames = new Frame[10];
        for (var i = 0; i < 10; i++) {
            frames[i] = new Frame();
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
