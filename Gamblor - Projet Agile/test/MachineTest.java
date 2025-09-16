import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MachineTest {
    
    public void afficherRessorcesTest(int i) {
        if(i == 0) {
            SymbolesGamble.returnAscii(SymbolesGamble.SPADE);
        }else if(i == 1) {
            SymbolesGamble.returnAscii(SymbolesGamble.STAR);
        }else if(i == 2) {
            SymbolesGamble.returnAscii(SymbolesGamble.HEART);
        }
    }
}
