package edu.up.cs301.twixt;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Sean on 11/14/2017.
 *
 * This class is a dumb computer player that makes random moves
 * However, it can win if given enough moves (I.E. hundreds)
 */

public class TwixtDumbPlayer extends GameComputerPlayer {

    int xVal;//random x position for peg
    int yVal;//random y  position for peg
    private Peg[][] current = new Peg[24][24];//array with current peg placements
    private TwixtGameState turnState;//the state of the board on this turn
    private Peg thisTurnPeg;//teh peg being placed this turn
    private boolean doneSwitchNum = false; //if the player numebr has been switched

    /**
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

        if (info instanceof TwixtGameState) {
            //converts arraylist into 2d array of the board
            turnState = (TwixtGameState) info;
            current = turnState.stateToArray();
            if(turnState.getSwitchPlayerNum() && !doneSwitchNum){
                this.playerNum  ^= 1;
                doneSwitchNum = true;
            }
        }


        // pick peg positions at random on the board
        Random r = new Random();

        if (this.playerNum == 0) {//if AI is player 0
            xVal = r.nextInt(22) + 1;//random x value that is valid
            yVal = r.nextInt(24);//random y value that is valid
        } else if (this.playerNum == 1) {//if AI is player 1
            xVal = r.nextInt(24);//random x value that is valid
            yVal = r.nextInt(22) + 1;//random y value that is valid
        }


        // delay for a second to simulate thinking
        sleep(1000);

        if (current[xVal][yVal] == null) {
            thisTurnPeg = new Peg(xVal, yVal, this.playerNum);//peg object that is being placed on this turn
        }
        // Submit our move to the game object. We haven't even checked it it's
        // our turn, or that that position is unoccupied.
        game.sendAction(new PlacePegAction(this, thisTurnPeg));//the this may not be correct

        game.sendAction(new EndTurnAction(this));//ends this turn


    }
}

