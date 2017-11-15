package edu.up.cs301.twixt;

import java.util.ArrayList;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Sean on 11/14/2017.
 */

public class TwixtSmartPlayer extends GameComputerPlayer {

    private ArrayList<Peg> copy;//copy arraylist of linked pegs, empty
    Peg[][] current = new Peg[24][24];
    /*
     * Constructor for the TwixtSmartPlayer class
     */
    public TwixtSmartPlayer(String name) {
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


        // delay for a second to simulate thinking
        sleep(1000);

        //go thru pegs, if hit peg, save into arraylists.
        TwixtGameState turnState = (TwixtGameState) info;
        current = turnState.stateToArray();

        for(int i = 0; i < 24; i++){
            for(int j = 0; j < 24; j++){
                if(current[i][j] != null) {
                    if(current[i][j].getPegTeam() == 0){

                    }
                }
            }
        }

        int xVal;
        int yVal;

        //Peg thisTurnPeg = new Peg(0, xVal, yVal copy);//peg object that is being placed on this turn

        // Submit our move to the game object. We haven't even checked it it's
        // our turn, or that that position is unoccupied.
        //game.sendAction(new PlacePegAction(this, thisTurnPeg));//the this may not be correct


    }
}

