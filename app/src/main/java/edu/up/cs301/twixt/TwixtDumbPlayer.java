package edu.up.cs301.twixt;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Sean on 11/14/2017.
 */

public class TwixtDumbPlayer extends GameComputerPlayer {

    private ArrayList<Peg> copy;//copy arraylist of linked pegs, empty

    /*
     * Constructor for the TwixtDumbPlayer class
     */
    public TwixtDumbPlayer(String name) {
        super(name);

    }

    @Override
    /**
     * Called when the player receives a game-state (or other info) from the
     * game.
     *
     * @param info
     * 		the message from the game
     */
    public void receiveInfo(GameInfo info) {

        // pick peg positions at random on the board
        Random r = new Random();
        int xVal = r.nextInt(23) + 1;//random x value, math may not be right as of now
        int yVal = r.nextInt(23) + 1;//random y value, math may not be right as of now.




        // delay for a second to simulate thinking
        sleep(1000);

        Peg thisTurnPeg = new Peg(xVal, yVal, 0, copy);//peg object that is being placed on this turn

        // Submit our move to the game object. We haven't even checked it it's
        // our turn, or that that position is unoccupied.
        game.sendAction(new PlacePegAction(this, thisTurnPeg));//the this may not be correct


    }
}

