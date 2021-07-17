package ua.casten.bowling.model;

import org.junit.jupiter.api.Test;
import ua.casten.bowling.exception.BowlingException;

import static org.junit.jupiter.api.Assertions.*;

class TestGame {

    private final Game verifiedGame = new Game();

    TestGame() throws BowlingException {
        fillVerifiedGame();
    }

    void fillVerifiedGame() throws BowlingException {
        verifiedGame.makePoll(10);
        verifiedGame.makePoll(3);
        verifiedGame.makePoll(7);
        verifiedGame.makePoll(9);
        verifiedGame.makePoll(0);
        verifiedGame.makePoll(10);
        verifiedGame.makePoll(8);
        verifiedGame.makePoll(0);
        verifiedGame.makePoll(2);
        verifiedGame.makePoll(8);
        verifiedGame.makePoll(0);
        verifiedGame.makePoll(6);
        verifiedGame.makePoll(10);
        verifiedGame.makePoll(10);
        verifiedGame.makePoll(10);
        verifiedGame.makePoll(8);
        verifiedGame.makePoll(1);
    }

    @Test
    void testFinishVerifiedGame() {
        assertTrue(verifiedGame.isFinished());
    }

    @Test
    void testVerifiedGameScore() {
        assertEquals(167, verifiedGame.getFullScore());
    }

    @Test
    void testVerifiedGameEachFrameBonus() {
        var frames = verifiedGame.getFrames();
        assertEquals(10, frames[1].getBonus());
        assertEquals(9, frames[2].getBonus());
        assertEquals(0, frames[3].getBonus());
        assertEquals(8, frames[4].getBonus());
        assertEquals(0, frames[5].getBonus());
        assertEquals(0, frames[6].getBonus());
        assertEquals(0, frames[7].getBonus());
        assertEquals(20, frames[8].getBonus());
        assertEquals(18, frames[9].getBonus());
        assertEquals(0, frames[10].getBonus());
    }

    @Test
    void testVerifiedGameEachFrameScore() {
        var frames = verifiedGame.getFrames();
        assertEquals(20, frames[1].getScore());
        assertEquals(39, frames[2].getScore());
        assertEquals(48, frames[3].getScore());
        assertEquals(66, frames[4].getScore());
        assertEquals(74, frames[5].getScore());
        assertEquals(84, frames[6].getScore());
        assertEquals(90, frames[7].getScore());
        assertEquals(120, frames[8].getScore());
        assertEquals(148, frames[9].getScore());
        assertEquals(167, frames[10].getScore());
    }

    @Test
    void testOnlyStrikeGameScore() throws BowlingException {
        var game = new Game();
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);

        assertEquals(300, game.getFullScore());
    }

    @Test
    void testSecondPollException() throws BowlingException {
        var game = new Game();
        game.makePoll(5);

        assertThrows(BowlingException.class, () -> game.makePoll(6));
    }

    @Test
    void testFinishOnNotFinishedGame() throws BowlingException {
        var game = new Game();
        game.makePoll(10);
        game.makePoll(8);
        game.makePoll(1);
        game.makePoll(0);
        game.makePoll(10);

        assertFalse(game.isFinished());
    }

    @Test
    void testThirdPollException() throws BowlingException {
        var game = new Game();
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(10);
        game.makePoll(8);

        assertThrows(BowlingException.class, () -> game.makePoll(3));
    }

}
