import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventsTest {
    Events e1 = Events.MORT_GM;
    Events e2 = Events.CHIEN;
    Events e3 = Events.REDUC;

    @Test
    public void testEvents() {
        assertEquals(e1.getTYPE(), 'w');
        assertEquals(e2.getTYPE(), 's');
        assertEquals(e3.getTYPE(), 'c');

        assertEquals(e1.getMessage(), "Votre Grand-mère est morte");
    }
}