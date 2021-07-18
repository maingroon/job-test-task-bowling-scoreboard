package ua.casten.bowling.util;

import org.junit.jupiter.api.Test;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.ViewFrame;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrameParserTest {

    @Test
    void testStrikeOnRegularFrame() {
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[1].setFirstRoll(10);
        frames[1].setRollNumber(2);
        frames[1].setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("", viewFrames[0].getFirstRoll());
        assertEquals("X", viewFrames[0].getSecondRoll());
    }

    @Test
    void testSpareOnRegularFrame() {
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[1].setFirstRoll(4);
        frames[1].setSecondRoll(6);
        frames[1].setRollNumber(3);
        frames[1].setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[0].getFirstRoll());
        assertEquals("/", viewFrames[0].getSecondRoll());
    }

    @Test
    void testMissOnRegularFrame() {
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[1].setFirstRoll(0);
        frames[1].setSecondRoll(0);
        frames[1].setRollNumber(3);
        frames[1].setInGame(true);
        frames[2].setFirstRoll(0);
        frames[2].setSecondRoll(5);
        frames[2].setRollNumber(3);
        frames[2].setInGame(true);
        frames[3].setFirstRoll(6);
        frames[3].setSecondRoll(0);
        frames[3].setRollNumber(3);
        frames[3].setInGame(true);
        frames[4].setFirstRoll(7);
        frames[4].setRollNumber(2);
        frames[4].setInGame(true);
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
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[1].setFirstRoll(4);
        frames[1].setSecondRoll(2);
        frames[1].setRollNumber(3);
        frames[1].setInGame(true);
        frames[2].setFirstRoll(4);
        frames[2].setRollNumber(2);
        frames[2].setInGame(true);
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
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[10].setFirstRoll(10);
        frames[10].setSecondRoll(10);
        frames[10].setThirdRoll(10);
        frames[10].setRollNumber(4);
        frames[10].setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("X", viewFrames[9].getSecondRoll());
        assertEquals("X", viewFrames[9].getThirdRoll());

        frames[10].setFirstRoll(10);
        frames[10].setSecondRoll(4);
        frames[10].setThirdRoll(4);
        frames[10].setRollNumber(4);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("4", viewFrames[9].getSecondRoll());
        assertEquals("4", viewFrames[9].getThirdRoll());

        frames[10].setFirstRoll(10);
        frames[10].setSecondRoll(10);
        frames[10].setThirdRoll(4);
        frames[10].setRollNumber(4);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("X", viewFrames[9].getSecondRoll());
        assertEquals("4", viewFrames[9].getThirdRoll());
    }

    @Test
    void testSpareRollOnLastFrame() {
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[10].setFirstRoll(10);
        frames[10].setSecondRoll(6);
        frames[10].setThirdRoll(4);
        frames[10].setRollNumber(4);
        frames[10].setInGame(true);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("X", viewFrames[9].getFirstRoll());
        assertEquals("6", viewFrames[9].getSecondRoll());
        assertEquals("/", viewFrames[9].getThirdRoll());

        frames[10].setFirstRoll(3);
        frames[10].setSecondRoll(7);
        frames[10].setThirdRoll(3);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("3", viewFrames[9].getFirstRoll());
        assertEquals("/", viewFrames[9].getSecondRoll());
        assertEquals("3", viewFrames[9].getThirdRoll());
    }

    @Test
    void testEmptyRollOnLastFrame() {
        Frame[] frames = createFrameArrayFilledNewFrames();
        frames[10].setInGame(false);
        ViewFrame[] viewFrames = FrameParser.parseFrames(frames);

        assertEquals("", viewFrames[9].getFirstRoll());
        assertEquals("", viewFrames[9].getSecondRoll());
        assertEquals("", viewFrames[9].getThirdRoll());

        frames[10].setFirstRoll(4);
        frames[10].setRollNumber(2);
        frames[10].setInGame(true);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[9].getFirstRoll());
        assertEquals("", viewFrames[9].getSecondRoll());
        assertEquals("", viewFrames[9].getThirdRoll());

        frames[10].setFirstRoll(4);
        frames[10].setSecondRoll(6);
        frames[10].setRollNumber(3);
        frames[10].setInGame(true);
        viewFrames = FrameParser.parseFrames(frames);

        assertEquals("4", viewFrames[9].getFirstRoll());
        assertEquals("/", viewFrames[9].getSecondRoll());
        assertEquals("", viewFrames[9].getThirdRoll());
    }

    Frame[] createFrameArrayFilledNewFrames() {
        Frame[] frames = new Frame[11];
        IntStream.range(0, 11)
                .forEach(i -> frames[i] = new Frame());

        return frames;
    }

}
