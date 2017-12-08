package edu.up.cs301.twixt;

import java.util.ArrayList;
import java.util.Random;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Sean P.Gillespie on 11/14/2017.
 *
 * This class creates a player will win quickly if not blocked
 * This player works from the left if player 1 or top if player 0
 * and links pegs together to win the game.  If it is blocked, it
 * makes a random move and then tries to go again from the same spot
 *
 *
 */

public class TwixtSmartPlayer extends GameComputerPlayer {

    private Peg[][] current = new Peg[24][24];//array with current peg placements
    private Random rand = new Random();//random object for generating random numbers
    private boolean firstMoveMade = false;//for checking if it is the first turn
    private boolean pegPlaced = false;//if a peg is placed
    private ArrayList<Peg> placedPegs = new ArrayList<Peg>();// arraylist of linked pegs
    private Peg lastTurnPeg;//the peg that was placed last turn, played off of
    private Peg thisTurnPeg;//peg that is being placed this turn
    private Peg firstTurnPeg;//peg that is placed on the first turn
    private TwixtGameState turnState;//the state of the board on each turn

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

            if (turnState.getTurn() == this.playerNum) {//if it is the AI players turn

                if (!firstMoveMade) {
                    firstMove(firstMoveMade);//places a peg in the end row for the first move, only happens once

                } else {
                    basicMove(firstMoveMade);//what it tries to do from first turn on

                }
            }
        }


    }//end of recieveInfo


    /**
     * This method places the first peg
     * top end row if Ai is player 0
     * left end row if AI is player 1
     **/
    public void firstMove(boolean moved) {
        moved = firstMoveMade;
        if (!moved && turnState.getTurn() == this.playerNum) {//ensures this is the first move

            if (this.playerNum == 1 && current[0][11] == null) //If the AI is player 1
            {
                if (pegPlaced == false) {//if peg has not been placed
                    firstTurnPeg = new Peg(0, 11, this.playerNum);//peg object that is being placed on this turn
                    game.sendAction(new PlacePegAction(this, firstTurnPeg));//sends action to game for validation
                    lastTurnPeg = firstTurnPeg;//sets this peg as last peg placed for next turn
                    placedPegs.add(lastTurnPeg);//adding the first peg to the array of placed pegs
                    firstMoveMade = true;//first turn is over
                    pegPlaced = true;//peg has been placed
                } else {
                    pegPlaced = false;//peg has not been placed so it can move next turn
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed in left end row

                }
            }

            if (this.playerNum == 0 && current[11][0] == null) //if the AI is player 0
            {
                if (pegPlaced == false){//if peg has not been placed
                    firstTurnPeg = new Peg(11, 0, this.playerNum);//peg object that is being placed on this turn
                    game.sendAction(new PlacePegAction(this, firstTurnPeg));//sends action to game for validation
                    lastTurnPeg = firstTurnPeg;//setting peg as last peg placed after it is placed
                    placedPegs.add(lastTurnPeg);//adding the first peg to the array of placed pegs
                    firstMoveMade = true;//first turn is over
                    pegPlaced = true;//peg has been placed
                } else {
                    pegPlaced = false;//lets peg be placed next turn
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed in left end row

                }
            }

        }


    }//end of firstMove


    /**
     *
     * Basic Move
     *
     * This method places a peg that follows from the previously placed peg.
     * only happens after the first move.
     * Goes in one of four moves from the last peg placed
     * in the positive direction
     **/

    public void basicMove(boolean moved) {
        firstMoveMade = moved;

        if (moved && turnState.getTurn() == this.playerNum && this.playerNum == 1) {//if the AI is player 1

            if (lastTurnPeg != null) {
                lastTurnPeg = placedPegs.get((placedPegs.size()) - 1);//gets last peg placed
            }
            int whatMove = rand.nextInt(4);//random number for switching up possible positive moves, 4 possibilities, 0-3

            if (whatMove == 0 && lastTurnPeg != null) {

                if (pegPlaced == false) {//if peg has not been placed
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() + 2), this.playerNum);//peg object that is being placed on this turn
                    
                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }

                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 1 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() - 2), this.playerNum);//peg object that is being placed on this turn

                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 2 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() - 1), this.playerNum);//peg object that is being placed on this turn

                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 3 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() + 1), this.playerNum);//peg object that is being placed on this turn

                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            }
        } else if (moved && turnState.getTurn() == this.playerNum && this.playerNum == 0) {//if the AI is player 0

            if (lastTurnPeg != null) {
                lastTurnPeg = placedPegs.get((placedPegs.size()) - 1);
            }

            int whatMove = rand.nextInt(4);
            if (whatMove == 0 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() - 1), (lastTurnPeg.getyPos() + 2), this.playerNum);//peg object that is being placed on this turn
                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 1 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 1), (lastTurnPeg.getyPos() + 2), this.playerNum);//peg object that is being placed on this turn
                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 2 && lastTurnPeg != null) {
                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() - 2), (lastTurnPeg.getyPos() + 1), this.playerNum);//peg object that is being placed on this turn
                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            } else if (whatMove == 3 && lastTurnPeg != null) {

                if (pegPlaced == false) {
                    thisTurnPeg = new Peg((lastTurnPeg.getxPos() + 2), (lastTurnPeg.getyPos() + 1), this.playerNum);//peg object that is being placed on this turn
                    if (isLegalMove(thisTurnPeg)) {

                            game.sendAction(new PlacePegAction(this, thisTurnPeg));//sends action to game for validation
                            lastTurnPeg = thisTurnPeg;//makes the peg that just got placed the last turn peg
                            placedPegs.add(lastTurnPeg);//and adds it to placed pegs
                            pegPlaced = true;

                    } else {
                        lastResort(firstMoveMade);//place random peg and then try again next turn
                    }
                } else {
                    game.sendAction(new EndTurnAction(this));//ends this turn with a peg placed
                    pegPlaced = false;
                }


            }
        }//end of player 0 stuff


    }//end of basic move


    /**
     *
     if the next move is taken by another peg, it places a random peg for this turn.
     Next turn it will try from the previous place again
     * @param moved
     */

    public void lastResort(boolean moved) {
        firstMoveMade = moved;
        int rndX = rand.nextInt(21) + 1;//x loc of the random peg
        int rndY = rand.nextInt(21) + 1;//y loc of the random peg
        Peg lastResortPeg = new Peg(rndX, rndY, this.playerNum);
        if (pegPlaced == false && isLegalMove(current[rndX][rndY])) {
            game.sendAction(new PlacePegAction(this, lastResortPeg));//sends action to game for validation
            pegPlaced = true;
        } else {
            game.sendAction(new EndTurnAction(this));
            pegPlaced = true;
        }
    }

    /**
     * checks to see if a place on the board is a legal place to place a peg
     * of a certain player
     *
     * @param p
     * @return
     */

    public boolean isLegalMove(Peg p) {

        thisTurnPeg = p;

        if (this.playerNum == 0) {
            if (thisTurnPeg.getxPos() > 0 && thisTurnPeg.getxPos() < 23) {
                if (thisTurnPeg.getyPos() >= 0 && thisTurnPeg.getyPos() <= 23) {
                    if (current[thisTurnPeg.getxPos()][thisTurnPeg.getyPos()] == null) {
                        return true;
                    }
                }
            }
        }

        if (this.playerNum == 1) {
            if (thisTurnPeg.getxPos() >= 0 && thisTurnPeg.getxPos() <= 23) {
                if (thisTurnPeg.getyPos() > 0 && thisTurnPeg.getyPos() < 23) {
                    if (current[thisTurnPeg.getxPos()][thisTurnPeg.getyPos()] == null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }//end of isLegalMove


}//end of class

