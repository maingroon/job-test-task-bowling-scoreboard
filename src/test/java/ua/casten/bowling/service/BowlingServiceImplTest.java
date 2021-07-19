package ua.casten.bowling.service;

import org.junit.jupiter.api.Test;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.ViewFrame;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BowlingServiceImplTest {

    private static final String NUMBER_FORMAT_EXCEPTION_MESSAGE = "Enter valid score (without symbols and spaces)";
    private static final String SCORE_UNBOUNDED_EXCEPTION_MESSAGE = "Score cannot be less than 0 or greater than 10";
    private static final String SCORE_SUM_EXCEPTION_MESSAGE = "Sum of poll in current frame cannot be greater than 10";
    private static final String SCORE_LAST_SUM_EXCEPTION_MESSAGE = "Sum of second and third poll cannot be greater than 10 without second strike";

    @Test
    void testInputSymbolsIntoMakePollException() {
        BowlingServiceImpl bowlingService = new BowlingServiceImpl();

        assertEquals(NUMBER_FORMAT_EXCEPTION_MESSAGE, bowlingService.makePoll("symbols12"));
        assertEquals(NUMBER_FORMAT_EXCEPTION_MESSAGE, bowlingService.makePoll("12symbols"));
        assertEquals(NUMBER_FORMAT_EXCEPTION_MESSAGE, bowlingService.makePoll("  s"));
        assertEquals(NUMBER_FORMAT_EXCEPTION_MESSAGE, bowlingService.makePoll("^32"));
        assertEquals(NUMBER_FORMAT_EXCEPTION_MESSAGE, bowlingService.makePoll("fe"));
    }

    @Test
    void testInputInvalidScoreException() {
        BowlingServiceImpl bowlingService = new BowlingServiceImpl();

        assertEquals(SCORE_UNBOUNDED_EXCEPTION_MESSAGE, bowlingService.makePoll("-2"));
        assertEquals(SCORE_UNBOUNDED_EXCEPTION_MESSAGE, bowlingService.makePoll("-1"));
        assertEquals(SCORE_UNBOUNDED_EXCEPTION_MESSAGE, bowlingService.makePoll("11"));
        assertEquals(SCORE_UNBOUNDED_EXCEPTION_MESSAGE, bowlingService.makePoll("12"));
    }

    @Test
    void testInputSumOfScoresGreaterThan10() {
        BowlingServiceImpl bowlingService = new BowlingServiceImpl();
        bowlingService.makePoll("6");

        assertEquals(SCORE_SUM_EXCEPTION_MESSAGE, bowlingService.makePoll("5"));
        assertEquals(SCORE_SUM_EXCEPTION_MESSAGE, bowlingService.makePoll("6"));
        assertEquals(SCORE_SUM_EXCEPTION_MESSAGE, bowlingService.makePoll("10"));
    }

    @Test
    void testInputSumOfScoresInLastFrameGreaterThan10() {
        BowlingServiceImpl bowlingService = new BowlingServiceImpl();
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("10");
        bowlingService.makePoll("7");

        assertEquals(SCORE_LAST_SUM_EXCEPTION_MESSAGE, bowlingService.makePoll("4"));
        assertEquals(SCORE_LAST_SUM_EXCEPTION_MESSAGE, bowlingService.makePoll("5"));
        assertEquals(SCORE_LAST_SUM_EXCEPTION_MESSAGE, bowlingService.makePoll("10"));
    }

    @Test
    void testInputValidScoresAndIsFinished() {
        BowlingServiceImpl bowlingService = new BowlingServiceImpl();

        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("7"));
        assertEquals("", bowlingService.makePoll("3"));
        assertEquals("", bowlingService.makePoll("0"));
        assertEquals("", bowlingService.makePoll("8"));
        assertEquals("", bowlingService.makePoll("9"));
        assertEquals("", bowlingService.makePoll("0"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));
        assertEquals("", bowlingService.makePoll("10"));

        assertTrue(bowlingService.isFinished());
    }

    @Test
    void testGetFramesAndStartNewGame() {
        BowlingServiceImpl bowlingService = new BowlingServiceImpl();
        ViewFrame[] viewFrames = bowlingService.getFrames();

        bowlingService.makePoll("10");
        bowlingService.makePoll("8");
        bowlingService.makePoll("2");
        bowlingService.makePoll("5");
        bowlingService.makePoll("0");
        bowlingService.makePoll("0");
        bowlingService.makePoll("6");

        assertFalse(Arrays.equals(bowlingService.getFrames(), viewFrames));
        assertFalse(bowlingService.isFinished());

        viewFrames = bowlingService.getFrames();
        bowlingService.startNewGame();

        assertFalse(Arrays.equals(bowlingService.getFrames(), viewFrames));
    }

}
