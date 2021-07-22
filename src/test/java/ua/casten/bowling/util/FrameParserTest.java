package ua.casten.bowling.util;

import org.junit.jupiter.api.Test;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.ViewFrame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrameParserTest {

    @Test
    void testStrikeOnRegularFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(0).setFirstRoll(10);
        frames.get(0).setRollNumber(2);
        frames.get(0).setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("", viewFrames[0].getFirstRoll());
        assertEquals("X", viewFrames[0].getSecondRoll());
    }

    @Test
    void testSpareOnRegularFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(0).setFirstRoll(4);
        frames.get(0).setSecondRoll(6);
        frames.get(0).setRollNumber(3);
        frames.get(0).setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[0].getFirstRoll());
        assertEquals("/", viewFrames[0].getSecondRoll());
    }

    @Test
    void testMissOnRegularFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(0).setFirstRoll(0);
        frames.get(0).setSecondRoll(0);
        frames.get(0).setRollNumber(3);
        frames.get(0).setInGame(true);
        frames.get(1).setFirstRoll(0);
        frames.get(1).setSecondRoll(5);
        frames.get(1).setRollNumber(3);
        frames.get(1).setInGame(true);
        frames.get(2).setFirstRoll(6);
        frames.get(2).setSecondRoll(0);
        frames.get(2).setRollNumber(3);
        frames.get(2).setInGame(true);
        frames.get(3).setFirstRoll(7);
        frames.get(3).setRollNumber(2);
        frames.get(3).setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("-", viewFrames[0].getFirstRoll());
        assertEquals("-", viewFrames[0].getSecondRoll());
        assertEquals("-", viewFrames[1].getFirstRoll());
        assertEquals("5", viewFrames[1].getSecondRoll());
        assertEquals("6", viewFrames[2].getFirstRoll());
        assertEquals("-", viewFrames[2].getSecondRoll());
        assertEquals("7", viewFrames[3].getFirstRoll());
        assertEquals("", viewFrames[3].getSecondRoll());
    }

    @Test
    void testScoreRollOnRegularFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(0).setFirstRoll(4);
        frames.get(0).setSecondRoll(2);
        frames.get(0).setRollNumber(3);
        frames.get(0).setInGame(true);
        frames.get(1).setFirstRoll(4);
        frames.get(1).setRollNumber(2);
        frames.get(1).setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[0].getFirstRoll());
        assertEquals("2", viewFrames[0].getSecondRoll());
        assertEquals("4", viewFrames[1].getFirstRoll());
        assertEquals("", viewFrames[1].getSecondRoll());
    }

    @Test
    void testFrameIsNotInGame() {
        ViewFrame[] viewFrames = FrameParser.parseFrames(createFrameArrayFilledNewFrames());

        assertEquals("", viewFrames[0].getFirstRoll());
        assertEquals("", viewFrames[0].getSecondRoll());
    }

    @Test
    void testStrikeRollOnLastFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(9).setFirstRoll(10);
        frames.get(9).setSecondRoll(10);
        frames.get(9).setThirdRoll(10);
        frames.get(9).setRollNumber(4);
        frames.get(9).setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("X", viewFrames[9].getSecondRoll());
        assertEquals("X", viewFrames[9].getThirdRoll());

        frames.get(9).setFirstRoll(10);
        frames.get(9).setSecondRoll(4);
        frames.get(9).setThirdRoll(4);
        frames.get(9).setRollNumber(4);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("4", viewFrames[9].getSecondRoll());
        assertEquals("4", viewFrames[9].getThirdRoll());

        frames.get(9).setFirstRoll(10);
        frames.get(9).setSecondRoll(10);
        frames.get(9).setThirdRoll(4);
        frames.get(9).setRollNumber(4);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("X", viewFrames[9].getSecondRoll());
        assertEquals("4", viewFrames[9].getThirdRoll());
    }

    @Test
    void testSpareRollOnLastFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(9).setFirstRoll(10);
        frames.get(9).setSecondRoll(6);
        frames.get(9).setThirdRoll(4);
        frames.get(9).setRollNumber(4);
        frames.get(9).setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("6", viewFrames[9].getSecondRoll());
        assertEquals("/", viewFrames[9].getThirdRoll());

        frames.get(9).setFirstRoll(3);
        frames.get(9).setSecondRoll(7);
        frames.get(9).setThirdRoll(3);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("3", viewFrames[9].getFirstRoll());
        assertEquals("/", viewFrames[9].getSecondRoll());
        assertEquals("3", viewFrames[9].getThirdRoll());
    }

    @Test
    void testEmptyRollOnLastFrame() {
        List<Frame> frames = createFrameArrayFilledNewFrames();
        frames.get(9).setInGame(false);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("", viewFrames[9].getFirstRoll());
        assertEquals("", viewFrames[9].getSecondRoll());
        assertEquals("", viewFrames[9].getThirdRoll());

        frames.get(9).setFirstRoll(4);
        frames.get(9).setRollNumber(2);
        frames.get(9).setInGame(true);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[9].getFirstRoll());
        assertEquals("", viewFrames[9].getSecondRoll());
        assertEquals("", viewFrames[9].getThirdRoll());

        frames.get(9).setFirstRoll(4);
        frames.get(9).setSecondRoll(6);
        frames.get(9).setRollNumber(3);
        frames.get(9).setInGame(true);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[9].getFirstRoll());
        assertEquals("/", viewFrames[9].getSecondRoll());
        assertEquals("", viewFrames[9].getThirdRoll());
    }

    List<Frame> createFrameArrayFilledNewFrames() {
        List<Frame> frames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            frames.add(new Frame());
        }
        return frames;
    }

}
