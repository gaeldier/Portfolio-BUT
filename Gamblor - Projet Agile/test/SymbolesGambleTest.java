import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SymbolesGambleTest {

    private SymbolesGamble symbolesGamble;
    private String pathSpade = "./res/spade.txt";
    private String pathHeart = "./res/heart.txt";
    private String pathStar = "./res/star.txt";
    private Machine machine;

    @BeforeEach
    public void setUp() {
        symbolesGamble = SymbolesGamble.SPADE;
        machine = new Machine();
    }

    @Test
    public void testGetSymboles() {
        int[] tab = new int[]{1,2,0};
        String retour = "";
        String compare = "";
        SymbolesGamble.returnAscii(SymbolesGamble.SPADE);
        SymbolesGamble.returnAscii(SymbolesGamble.SPADE);
        //System.out.println(SymbolesGamble.returnAscii(SymbolesGamble.SPADE));
        assertEquals(retour, compare);
    }
}
