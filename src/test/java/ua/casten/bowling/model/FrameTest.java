package ua.casten.bowling.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrameTest {
    private final Frame strikeFrame = new Frame();
    private final Frame spokeFrame = new Frame();
    private final Frame scoreFrame = new Frame();

    FrameTest() {
        fillFrames();
    }

    void fillFrames() {
        strikeFrame.setFirstRoll(10);
        strikeFrame.setSecondRoll(0);
        strikeFrame.setThirdRoll(5);
        spokeFrame.setFirstRoll(8);
        spokeFrame.setSecondRoll(2);
        spokeFrame.setThirdRoll(8);
        scoreFrame.setFirstRoll(4);
        scoreFrame.setFirstRoll(5);
    }

    @Test
    void testStrike() {
        assertTrue(strikeFrame.isStrike());
        assertFalse(spokeFrame.isStrike());
        assertFalse(scoreFrame.isStrike());
    }

    @Test
    void testSpare() {
        assertTrue(spokeFrame.isSpare());
        assertFalse(strikeFrame.isSpare());
        assertFalse(scoreFrame.isSpare());
    }

}
