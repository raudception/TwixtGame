package twixt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tdog9 on 11/9/2017.
 */
public class TwixtGameStateTest {
    @Test
    public void stateToArray() throws Exception {

    }

    @Test
    public void testStateToArray(){
        TwixtGameState test = new TwixtGameState();
        Peg[][] array = test.stateToArray();
        for(int i; i < 24; i++){
            for(int j; i < 24; j++){
                if (array[i][j] != null){
                    assertNotEquals(array[i][j].getXpos(),0);
                    assertNotEquals(array[i][j].getYpos(),0);
                    assertEquals(array[i][j].getTeam,0,1);
                    assertEquals
                }
            }
        }
    }

}