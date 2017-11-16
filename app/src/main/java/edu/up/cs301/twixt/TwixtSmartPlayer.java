package edu.up.cs301.twixt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Sean on 11/14/2017.
 */

public class TwixtSmartPlayer extends GameComputerPlayer {

    private ArrayList<Peg> copy;//copy arraylist of linked pegs, empty
    private ArrayList<Peg> copy2;//copy arraylist of linked pegs, empty
    private ArrayList<Peg> copy3;//copy arraylist of linked pegs, empty
    private ArrayList<Peg> copy4;//copy arraylist of linked pegs, empty
    Peg[][] current = new Peg[24][24];
    Random rand = new Random();


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

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                if (current[i][j] != null) {
                    if (current[i][j].getPegTeam() == 0) {
                        copy = (current[i][j].getLinkedPegs());
                        if (copy != null) {
                            for (int k = 0; k < copy.size(); k++) {
                                copy2 = (copy.get(i).getLinkedPegs());
                                if (copy2 != null) {
                                    for (int l = 0; l < copy2.size(); l++) {
                                        copy3 = (copy2.get(i).getLinkedPegs());
                                        if (copy3 != null) {
                                            for (int m = 0; m < copy3.size(); m++) {
                                                copy4 = (copy3.get(i).getLinkedPegs());
                                                if (copy4 != null) {

                                                    int xLoc = copy4.get(m).getxPos();
                                                    int yLoc = copy4.get(m).getyPos();
                                                    int rndX = rand.nextInt(2);
                                                    int rndY = rand.nextInt(2);
                                                    int tempRnd = rand.nextInt(2);//will be used to "randomly" choose a valid linkable hole
                                                    if (rndX == rndY && rndX == 1)
                                                    {
                                                        if (tempRnd == 1)
                                                            rndX += 1;
                                                        else {
                                                            rndY += 1;
                                                        }
                                                    }
                                                    else
                                                    {
                                                        if (tempRnd == 2) {
                                                            rndX -= 1;
                                                        } else {
                                                            rndY -= 1;
                                                        }
                                                    }

                                                    if(tempRnd == 1){
                                                        xLoc = xLoc + rndX;//attempts to move in an l shape
                                                        yLoc = yLoc + rndY;//attempts to move in an l shape so it linkes
                                                    }
                                                    else
                                                    {
                                                        xLoc = xLoc - rndX;//attempts to move in an l shape
                                                        yLoc = yLoc - rndY;//attempts to move in an l shape so it linkes
                                                    }

                                                Peg thisTurnPeg = new Peg(xLoc, yLoc, 0, copy);//peg object that is being placed on this turn

                                                //Submit our move to the game object. We haven't even checked it it's
                                                // our turn, or that that position is unoccupied.
                                                game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


//Peg thisTurnPeg = new Peg(0, xVal, yVal copy);//peg object that is being placed on this turn

// Submit our move to the game object. We haven't even checked it it's
// our turn, or that that position is unoccupied.
//game.sendAction(new PlacePegAction(this, thisTurnPeg));//the this may not be correct


}
}

