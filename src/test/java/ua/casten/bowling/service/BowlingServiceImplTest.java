package ua.casten.bowling.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.ViewFrame;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BowlingServiceImplTest {

    private static final String NUMBER_FORMAT_EXCEPTION_MESSAGE = "Enter valid score (without symbols and spaces)";
    private static final String SCORE_UNBOUNDED_EXCEPTION_MESSAGE = "Score cannot be less than 0 or greater than 10";
    private static final String SCORE_SUM_EXCEPTION_MESSAGE = "Sum of rolls in current frame cannot be greater than 10";
    private static final String SCORE_LAST_SUM_EXCEPTION_MESSAGE = "Sum of second and third rolls cannot be greater " +
            "than 10 without second strike";

    @Test
    void testIsStarted() {
        BowlingService bowlingService = new BowlingServiceImpl();
        assertFalse(bowlingService.isStarted());

        bowlingService.startNewGame();
        assertTrue(bowlingService.isStarted());
    }

    @Test
    void testIsFinishedOnNewGame() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        assertFalse(bowlingService.isFinished());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "p2", "1-1", "fd5", "10f", "symbols", "  spaces", "1s"})
    void testNumberFormatException(String stringScore) {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        assertEquals(NUMBER_FORMAT_EXCEPTION_MESSAGE, bowlingService.makeRoll(stringScore));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-2", "-3", "11", "12", "13", "-124", "124"})
    void testScoreUnboundedException(String stringScore) {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        assertEquals(SCORE_UNBOUNDED_EXCEPTION_MESSAGE, bowlingService.makeRoll(stringScore));
    }

    @ParameterizedTest
    @ValueSource(strings = {"6", "7", "8", "9", "10"})
    void testScoreSumException(String stringScore) {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("5");
        assertEquals(SCORE_SUM_EXCEPTION_MESSAGE, bowlingService.makeRoll(stringScore));
    }

    @ParameterizedTest
    @ValueSource(strings = {"6", "7", "8", "9", "10"})
    void testLastSumException(String stringScore) {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        for (int i = 0; i < 10; i++) {
            bowlingService.makeRoll("10");
        }
        bowlingService.makeRoll("5");
        assertEquals(SCORE_LAST_SUM_EXCEPTION_MESSAGE, bowlingService.makeRoll(stringScore));
    }

    @Test
    void testIsFinished1() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        for (int i = 0; i < 12; i++) {
            bowlingService.makeRoll("10");
        }
        assertTrue(bowlingService.isFinished());
    }

    @Test
    void testIsFinished2() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        for (int i = 0; i < 10; i++) {
            bowlingService.makeRoll("4");
            bowlingService.makeRoll("6");
        }
        bowlingService.makeRoll("3");
        assertTrue(bowlingService.isFinished());
    }

    @Test
    void testIsFinished3() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        for (int i = 0; i < 10; i++) {
            bowlingService.makeRoll("2");
            bowlingService.makeRoll("2");
        }
        assertTrue(bowlingService.isFinished());
    }

    @Test
    void testStrikeScore1() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("5");
        bowlingService.makeRoll("5");

        var frame = bowlingService.getFrames()[0];
        assertEquals("", frame.getFirstRoll());
        assertEquals("X", frame.getSecondRoll());
        assertEquals(20, frame.getScore());
    }

    @Test
    void testStrikeScore2() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("10");

        var frame = bowlingService.getFrames()[0];
        assertEquals("", frame.getFirstRoll());
        assertEquals("X", frame.getSecondRoll());
        assertEquals(30, frame.getScore());
    }

    @Test
    void testStrikeScore3() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("4");
        bowlingService.makeRoll("5");

        var frame = bowlingService.getFrames()[0];
        assertEquals("", frame.getFirstRoll());
        assertEquals("X", frame.getSecondRoll());
        assertEquals(19, frame.getScore());
    }

    @Test
    void testSpareScore1() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("6");
        bowlingService.makeRoll("4");
        bowlingService.makeRoll("5");

        var frame = bowlingService.getFrames()[0];
        assertEquals("6", frame.getFirstRoll());
        assertEquals("/", frame.getSecondRoll());
        assertEquals(15, frame.getScore());
    }

    @Test
    void testSpareScore2() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("2");
        bowlingService.makeRoll("8");
        bowlingService.makeRoll("10");

        var frame = bowlingService.getFrames()[0];
        assertEquals("2", frame.getFirstRoll());
        assertEquals("/", frame.getSecondRoll());
        assertEquals(20, frame.getScore());
    }

    @Test
    void testSpareScore3() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("5");
        bowlingService.makeRoll("5");

        var frame = bowlingService.getFrames()[0];
        assertEquals("-", frame.getFirstRoll());
        assertEquals("/", frame.getSecondRoll());
        assertEquals(15, frame.getScore());
    }

    @Test
    void testMissScore1() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("0");

        var frame = bowlingService.getFrames()[0];
        assertEquals("-", frame.getFirstRoll());
        assertEquals("-", frame.getSecondRoll());
        assertEquals(0, frame.getScore());
    }

    @Test
    void testMissScore2() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("7");

        var frame = bowlingService.getFrames()[0];
        assertEquals("-", frame.getFirstRoll());
        assertEquals("/", frame.getSecondRoll());
        assertEquals(17, frame.getScore());
    }

    @Test
    void testMissScore3() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("6");
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("5");

        var frame = bowlingService.getFrames()[0];
        assertEquals("6", frame.getFirstRoll());
        assertEquals("-", frame.getSecondRoll());
        assertEquals(6, frame.getScore());
    }

    @Test
    void testGreatGameFinalScore() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        for (int i = 0; i < 12; i++) {
            bowlingService.makeRoll("10");
        }
        assertEquals(300, bowlingService.getFrames()[9].getScore());
    }

    @Test
    void testSpareGameFinalScore() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        for (int i = 0; i < 10; i++) {
            bowlingService.makeRoll("5");
            bowlingService.makeRoll("5");
        }
        bowlingService.makeRoll("5");
        assertEquals(150, bowlingService.getFrames()[9].getScore());
    }

    @Test
    void testVerifiedGameScore() {
        BowlingService bowlingService = new BowlingServiceImpl();
        bowlingService.startNewGame();
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("7");
        bowlingService.makeRoll("3");
        bowlingService.makeRoll("9");
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("8");
        bowlingService.makeRoll("8");
        bowlingService.makeRoll("2");
        bowlingService.makeRoll("0");
        bowlingService.makeRoll("6");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("10");
        bowlingService.makeRoll("8");
        bowlingService.makeRoll("1");
        assertEquals(167, bowlingService.getFrames()[9].getScore());
    }

}
