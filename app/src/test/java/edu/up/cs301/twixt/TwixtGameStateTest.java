package edu.up.cs301.twixt;


import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created by Kollin on 11/9/2017.
 */
public class TwixtGameStateTest {
    @org.junit.Test
    public void stateToArray() throws Exception {
        TwixtGameState official = new TwixtGameState();
        ArrayList<Peg> test = new ArrayList<Peg>();
        Peg testpeg = new Peg(1,2,0,test);
        test.add(testpeg);
        official.setBoard(test);
        official.stateToArray();
        Assert.assertTrue(official.getBoard() == null);

    }

    @org.junit.Test
    public void getBoard() throws Exception {

    }

    @org.junit.Test
    public void setBoard() throws Exception {

    }

    @org.junit.Test
    public void getTurn() throws Exception {

    }

    @org.junit.Test
    public void setTurn() throws Exception {

    }

}