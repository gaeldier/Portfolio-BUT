package modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrientation {

    @Test
    public void testNordEst() {
        Orientation o = Orientation.NORD_EST;
        assertEquals(0, o.dx1);
        assertEquals(-1, o.dy1);
        assertEquals(1, o.dx2);
        assertEquals(0, o.dy2);
    }

    @Test
    public void testNordOuest() {
        Orientation o = Orientation.NORD_OUEST;
        assertEquals(0, o.dx1);
        assertEquals(-1, o.dy1);
        assertEquals(-1, o.dx2);
        assertEquals(0, o.dy2);
    }

    @Test
    public void testSudEst() {
        Orientation o = Orientation.SUD_EST;
        assertEquals(0, o.dx1);
        assertEquals(1, o.dy1);
        assertEquals(1, o.dx2);
        assertEquals(0, o.dy2);
    }

    @Test
    public void testSudOuest() {
        Orientation o = Orientation.SUD_OUEST;
        assertEquals(0, o.dx1);
        assertEquals(1, o.dy1);
        assertEquals(-1, o.dx2);
        assertEquals(0, o.dy2);
    }
}
