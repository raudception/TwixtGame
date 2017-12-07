package edu.up.cs301.twixt;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * Created by Sean on 11/14/2017.
 */

public class TwixtSmartPlayer extends GameComputerPlayer {

    private ArrayList<Peg> copy;//copy arraylist of linked pegs, empty
    private ArrayList<Peg> copy2;//copy arraylist of linked pegs, empty
    private ArrayList<Peg> copy3;//copy arraylist of linked pegs, empty
    private ArrayList<Peg> copy4;//copy arraylist of linked pegs, empty
    private Peg[][] current = new Peg[24][24];//array with current peg placements
    private Random rand = new Random();//random object for generating random numbers
    private boolean firstMoveMade = false;//for checking if it is the first turn
    private boolean pegPlaced = false;
    private ArrayList<Peg> placedPegs = new ArrayList<Peg>();//copy arraylist of linked pegs, empty
    private Peg lastTurnPeg;
    private Peg thisTurnPeg;
    private Peg firstTurnPeg;
    private TwixtGameState turnState;
    private int blockTurnCounter = 0;

    /**
     * Constructor for TwixtSmartPlayer class
     **/
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


        if (info instanceof TwixtGameState) {
            //converts arraylist into 2d array of the board
            turnState = (TwixtGameState) info;
            current = turnState.stateToArray();

            if (turnState.getTurn() == this.playerNum) {

                if (!firstMoveMade) {
                    firstMove(firstMoveMade);//places a peg in the end row for the first move

                } else {
                    basicMove(firstMoveMade);

                }
            }
        }


    }//end of recieveInfo


    /**
     * This method places the first peg in the left end row
     **/
    public void firstMove(boolean moved) {
        moved = firstMoveMade;
        if (!moved && turnState.getTurn() == this.playerNum) {//ensures this is the first move

            if (this.playerNum == 1 && current[0][11] == null) ///check to make sure there is no other player piece there
            {
                if (pegPlaced == false) {
                    firstTurnPeg = new Peg(0, 11, this.playerNum);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, firstTurnPeg));//sends action to game for validation
                    lastTurnPeg = firstTurnPeg;
                    placedPegs.add(lastTurnPeg);//adding the first peg to the array of placed pegs
                    firstMoveMade = true;
                    pegPlaced = true;
                } else {
                    pegPlaced = false;
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed in left end row

                }
            }

            if (this.playerNum == 0 && current[11][0] == null) ///check to make sure there is no other player piece there
            {
                if (pegPlaced == false) {
                    firstTurnPeg = new Peg(11, 0, this.playerNum);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, firstTurnPeg));//sends action to game for validation
                    lastTurnPeg = firstTurnPeg;
                    placedPegs.add(lastTurnPeg);//adding the first peg to the array of placed pegs
                    firstMoveMade = true;
                    pegPlaced = true;
                } else {
                    pegPlaced = false;
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed in left end row

                }
            }

        }


    }


    /**
     * This method places a peg that follows from the previously placed peg.
     * only happens after the first move
     **/

    public void basicMove(boolean moved) {
        firstMoveMade = moved;

        if (moved && turnState.getTurn() == this.playerNum && this.playerNum == 1) {

            if (lastTurnPeg != null) {
                lastTurnPeg = placedPegs.get((placedPegs.size()) - 1);
            }
            int whatMove = rand.nextInt(4);

            if (whatMove == 0 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() + 2), this.playerNum);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }

                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 1 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() - 2), this.playerNum);//peg object that is being placed on this turn

                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 2 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() - 1), this.playerNum);//peg object that is being placed on this turn

                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 3 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() + 1), this.playerNum);//peg object that is being placed on this turn

                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            }
        } else if (moved && turnState.getTurn() == this.playerNum && this.playerNum == 0) {

            if (lastTurnPeg != null) {
                lastTurnPeg = placedPegs.get((placedPegs.size()) - 1);
            }

            int whatMove = rand.nextInt(4);
            if (whatMove == 0 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() - 1), (lastTurnPeg.getyPos() + 2), this.playerNum);//peg object that is being placed on this turn
                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 1 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() + 2), this.playerNum);//peg object that is being placed on this turn
                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 2 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() - 2), (lastTurnPeg.getyPos() + 1), this.playerNum);//peg object that is being placed on this turn
                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 3 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() + 1), this.playerNum);//peg object that is being placed on this turn
                    if(isLegalMove(thisTurnPeg)) {
                        game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                        lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                        placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                        pegPlaced = true;
                    }
                    else
                    {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            }
        }//end of player 0 stuff


    }//end of basic move


    /**
     * This method attempts to block the other player if they have at least 4 links in a row.
     * Done by placing a peg in a spot that might inhibit further progress
     **/
    public void attemptBlock(boolean moved) {

    }//attemptBlock

    public void lastResort(boolean moved) {
        firstMoveMade = moved;
        int rndX = rand.nextInt(20)+1;
        int rndY = rand.nextInt(20)+1;
        Peg lastResortPeg = new Peg(rndX, rndY, this.playerNum);
        if (pegPlaced == false) {
            game.sendAction(new PlacePegAction(this, lastResortPeg));//sends action to game for validation
            pegPlaced = true;
        } else {
            game.sendAction(new EndTurnAction(this));
            pegPlaced = true;
        }
    }

    public boolean isLegalMove(Peg p) {

        thisTurnPeg = p;

        if (this.playerNum == 0) {
            if (thisTurnPeg.getxPos() > 0 && thisTurnPeg.getxPos() < 23) {
                if (thisTurnPeg.getyPos() >= 0 && thisTurnPeg.getyPos() <= 23) {
                    if (current[thisTurnPeg.getxPos()][thisTurnPeg.getyPos()] == null){
                        return true;
                    }
                }
            }
        }

        if (this.playerNum == 1) {
            if (thisTurnPeg.getxPos() >= 0 && thisTurnPeg.getxPos() <= 23) {
                if (thisTurnPeg.getyPos() > 0 && thisTurnPeg.getyPos() < 23) {
                    if (current[thisTurnPeg.getxPos()][thisTurnPeg.getyPos()] == null){
                        return true;
                    }
                }
            }
        }

        return false;
    }

}//end of class

