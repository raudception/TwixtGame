package edu.up.cs301.twixt;

import android.util.Log;

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
    private Peg[][] current = new Peg[24][24];//array with current peg placements
    private Random rand = new Random();//random object for generating random numbers
    private boolean firstMoveMade = false;//for checking if it is the first turn
    private boolean pegPlaced = false;
    private ArrayList<Peg> placedPegs = new ArrayList<Peg>();//copy arraylist of linked pegs, empty
    private Peg lastTurnPeg;
    private Peg thisTurnPeg;
    private Peg firstTurnPeg;
    private TwixtGameState turnState;


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

            if (turnState.getTurn() == 1) {

                if (firstMoveMade == false) {
                    firstMove(firstMoveMade);//places a peg in the end row for the first move

                } else if (firstMoveMade == true) {
                    attemptBlock(firstMoveMade);//places peg as an attempted block if the other player has a 4 in a row
                    basicMove(firstMoveMade);//places pegs continuing from the first placed peg and going across the board.

                }
            }
            Log.d("recieved info", "received info");
        }

    }//end of recieveInfo


    /**
     * This method places the first peg in the left end row
     **/
    public void firstMove(boolean moved) {
        moved = firstMoveMade;
        if (!moved && turnState.getTurn() == 1) {//ensures this is the first move

            if (current[0][11] == null) ///check to make sure there is no other player piece there
            {
                if (pegPlaced == false) {
                    firstTurnPeg = new Peg(0, 11, 1);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, firstTurnPeg));//sends action to game for validation
                    lastTurnPeg = firstTurnPeg;
                    placedPegs.add(lastTurnPeg);//adding the first peg to the array of placed pegs
                    firstMoveMade = true;
                    pegPlaced = true;
                    Log.d("MOVEMADE IS TRUE", "completed firstMove");
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed in left end row
                    pegPlaced = false;
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

        if (moved && turnState.getTurn() == 1) {

            if (lastTurnPeg != null) {
                lastTurnPeg = placedPegs.get((placedPegs.size()) - 1);
            }
            int whatMove = rand.nextInt(4);
            if (whatMove == 0 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() + 2), 1);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                    lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                    placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                    pegPlaced = true;
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                    Log.d("just completed for", "" + whatMove);
                }


            } else if (whatMove == 1 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() - 2), 1);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                    lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                    placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                    pegPlaced = true;
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                    Log.d("just completed for", "" + whatMove);
                }


            } else if (whatMove == 2 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() - 1), 1);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                    lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                    placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                    pegPlaced = true;
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                    Log.d("just completed for", "" + whatMove);
                }


            } else if (whatMove == 3 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() + 1), 1);//peg object that is being placed on this turn
                    //Submit our move to the game object. We haven't even checked it it's
                    // our turn, or that that position is unoccupied.
                    game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                    lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                    placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                    pegPlaced = true;
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                    Log.d("just completed for", "" + whatMove);
                }


            }
        }

    }


    /**
     * This method attempts to block the other player if they have at least 4 links in a row.
     * Done by placing a peg in a spot that inhibits further progress
     **/
    public void attemptBlock(boolean moved) {
        firstMoveMade = moved;
        //This chunk of logic will see if there is 4 in a row of the other player and try to block
        //as of now, it puts pegs in the vicinity of an end of the other players link of 4
        if (moved && turnState.getTurn() == 1) {
            for (int x = 0; x < 24; x++) {
                for (int y = 0; y < 24; y++) {//iterates through the current state of the board
                    if (current[x][y] != null) {//if there is a piece in this spot
                        if (current[x][y].getPegTeam() == 0) {//if the piece belongs to the other player
                            copy = (current[x][y].getLinkedPegs());//getting linked pegs of this peg into a arraylist
                            if (copy != null) {//if there are linked pegs for this piece
                                for (int one = 0; one < copy.size(); one++) {//iterates through copy arraylist
                                    copy2 = (copy.get(one).getLinkedPegs());//arraylist for all linked pegs in the selected pegs linked pegs
                                    if (copy2 != null) {//if there are pegs linked to this peg
                                        for (int two = 0; two < copy2.size(); two++) {
                                            if (copy2.get(two) != current[x][y]) {//making sure that these linked pegs are not the first peg
                                                copy3 = (copy2.get(two).getLinkedPegs());
                                                if (copy3 != null) {
                                                    for (int three = 0; three < copy3.size(); three++) {
                                                        if (copy3.get(three) != copy.get(one)) {//making sure that there are no re counts of pegs
                                                            copy4 = (copy3.get(three).getLinkedPegs());
                                                            if (copy4 != null) {
                                                                for (int four = 0; four < copy4.size(); four++) {
                                                                    if (copy4.get(four) != copy2.get(two)) {


                                                                        int xLoc = copy4.get(four).getxPos();//location of peg at end of a string of 4
                                                                        int yLoc = copy4.get(four).getyPos();//location of peg at end of a string of 4
                                                                        int rndX = rand.nextInt(2);//either 2 or 1
                                                                        int rndY = rand.nextInt(2);//either 2 or 1
                                                                        int tempRnd = rand.nextInt(2);//either 2 or l, will be used to "randomly" choose a valid linkable hole
                                                                        if (rndX == rndY && rndX == 1) {
                                                                            if (tempRnd == 1)
                                                                                rndX += 1;
                                                                            else {
                                                                                rndY += 1;
                                                                            }
                                                                            //the above if else makes sure it is an 'L' shape from the last peg in the row
                                                                        } else if (rndX == rndY && rndX == 2) {
                                                                            if (tempRnd == 2) {
                                                                                rndX -= 1;
                                                                            } else {
                                                                                rndY -= 1;
                                                                            }
                                                                            //the above if else makes it an l shape from last peg
                                                                        }

                                                                        if (tempRnd == 1) {
                                                                            xLoc = xLoc + rndX;//attempts to move in an l shape
                                                                            yLoc = yLoc - rndY;//attempts to move in an l shape so it linkes
                                                                        } else {
                                                                            xLoc = xLoc - rndX;//attempts to move in an l shape
                                                                            yLoc = yLoc + rndY;//attempts to move in an l shape so it linkes
                                                                        }

                                                                        Peg blockPeg = new Peg(xLoc, yLoc, 1);//peg object that is being placed on this turn

                                                                        //Submit our move to the game object. We haven't even checked it it's
                                                                        // our turn, or that that position is unoccupied.
                                                                        if (pegPlaced == false) {
                                                                            game.sendAction(new PlacePegAction(this, blockPeg));//sends action to game for validation
                                                                        } else {
                                                                            game.sendAction(new EndTurnAction(this));//ends this turn with an attempted block
                                                                        }

                                                                        Log.d("IN THE ", "_aTTEMPT BLOCK");
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
                        }
                    }
                }
            }
        }

    }//attemptBlock


}//end of class

