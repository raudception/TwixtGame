package edu.up.cs301.twixt;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Kollin on 11/9/2017.
 */
public class TwixtGameStateTest {
    @Test
    public void stateToArray() throws Exception {
//        TwixtGameState official = new TwixtGameState();
//        ArrayList<Peg> test = new ArrayList<Peg>();
//        Peg testpeg = new Peg(1, 2, 0, test);
//        test.add(testpeg);
//        official.setBoard(test);
//        Peg[][] testarray = official.stateToArray();
//        Assert.assertTrue(testarray[1][2].equals(testpeg));

    }
    @Test
    public void copyConstructor() throws Exception{
        TwixtGameState official = new TwixtGameState();
        official.setTurn(1);
        TwixtGameState test = new TwixtGameState(official);
        Assert.assertTrue(test.getTurn() ==1);
    }
}