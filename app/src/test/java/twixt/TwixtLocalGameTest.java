package twixt;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by tdog9 on 11/9/2017.
 */
public class TwixtLocalGameTest {
    @Test
    public void canMove() throws Exception {

    }

    @Test
    public void makeMove() throws Exception {

    }

    @Test
    public void sendUpdatedStateTo() throws Exception {


    }

    @Test
    public void checkIfGameOver() throws Exception {



    }

    @Test
    public void testCheckIfGameOver(){

        TwixtLocalGame test = new TwixtLocalGame();0        TwixtGameState state = new TwixtGameState();
        ArrayList<Peg> testArray = new ArrayList<Peg>();
        Peg peg1 = new Peg(0,5,0,testArray);
        Peg peg2 = new Peg(2,6,0,testArray);
        Peg peg3 = new Peg(4,7,0,testArray);
        Peg peg4 = new Peg(6,6,0,testArray);
        Peg peg5 = new Peg(8,7,0,testArray);
        Peg peg6 = new Peg(10,6,0,testArray);
        Peg peg7 = new Peg(12,7,0,testArray);
        Peg peg8 = new Peg(14,6,0,testArray);
        Peg peg9 = new Peg(16,7,0,testArray);
        Peg peg10 = new Peg(18,6,0,testArray);
        Peg peg11 = new Peg(20,7,0,testArray);
        Peg peg12 = new Peg(22,6,0,testArray);
        Peg peg13 = new Peg(24,7,0,testArray);

        testArray.add(peg1);
        testArray.add(peg2);
        testArray.add(peg3);
        testArray.add(peg4);
        testArray.add(peg5);
        testArray.add(peg6);
        testArray.add(peg7);
        testArray.add(peg8);
        testArray.add(peg9);
        testArray.add(peg10);
        testArray.add(peg11);
        testArray.add(peg12);
        testArray.add(peg13);

        assertEquals( test.checkIfGameOver(),"Win");



    }

}