package edu.up.cs301.twixt;

import android.util.Log;

import java.util.ArrayList;


import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * class TwixtLocalGame controls the play of the game
 *
 * @author Andrew M. Nuxoll, modified by Kollin A. Raudsepp
 * @version November 2017
 */
public class TwixtLocalGame extends LocalGame {
    //the official instance of the TwixtGameState
    private TwixtGameState official;

    //if a given player has already placed a peg on their turn
    private boolean pegUsed = false;

    //what the last peg placed was by a certain player
    Peg lastPeg;


    /**
     * This ctor creates a new game state
     */
    public TwixtLocalGame() {
        // TwixtGameState test = new TwixtGameState();
        official = new TwixtGameState();

    }

    /**
     * can the player with the given id take an action right now?
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx == official.getTurn()) { //this may or may not match the passed in value, playerIdx
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method is called when a new action arrives from a player
     *
     * @return true if the action was taken or false if the action was invalid/illegal.
     */
    @Override
    protected boolean makeMove(GameAction action) {
        if (action instanceof EndTurnAction) { //this action ends the current player's turn
            if (action.getPlayer().equals(players[official.getTurn()])) {
                if (official.getTurn() == 1) {
                    official.setTurn(0);
                    official.incrementTotalTurns(); //keep track of the total number of turns
                } else if (official.getTurn() == 0) {
                    official.setTurn(1);
                    official.incrementTotalTurns(); //keep track of the total number of turns
                } else {
                    Log.i("makeMove", "Invalid Turn: " + official.getTurn());
                    return false;
                }
                pegUsed = false;
                lastPeg = new Peg(0, 0, -1);

                return true;

            }

        }
        else if (action instanceof OfferDrawAction) { //this action allows players to offer a draw after 20 turns
            if (action.getPlayer().equals(players[official.getTurn()]) && official.getTotalturns() > 20) {
                if (official.getTurn() == 1) {
                    official.setOfferDraw0(true);
                } else if (official.getTurn() == 0) {
                    official.setOfferDraw1(true);
                }


                return true;
            }
        }
        else if (action instanceof PlaceLinkAction) { //this action allows players to manually place links between legal pegs
            if (action.getPlayer().equals(players[official.getTurn()])) {
                PlaceLinkAction pla = (PlaceLinkAction) action;
                if (pla.getHoldPeg1() != null && pla.getHoldPeg2() != null) {
                    int x1 = pla.getHoldPeg1().getxPos();
                    int y1 = pla.getHoldPeg1().getyPos();
                    int x2 = pla.getHoldPeg2().getxPos();
                    int y2 = pla.getHoldPeg2().getyPos();
                    Peg peg1 = pla.getHoldPeg1();
                    Peg peg2 = pla.getHoldPeg2();
                    if (peg1.getPegTeam() == official.getTurn() && peg2.getPegTeam() == official.getTurn()) {
                        if (((x1 - x2) == 1 || (x1 - x2) == -1) && ((y1 - y2 == 2) || (y1 - y2) == -2) && canAddLinks(peg1, peg2)) {
                            ArrayList<Peg> newlinked1 = peg1.getLinkedPegs(); //add peg 2 to the first peg's arraylist
                            newlinked1.add(peg2);
                            peg1.setLinkedPegs(newlinked1);

                            ArrayList<Peg> newlinked2 = peg2.getLinkedPegs(); //add the peg1 to the second peg's linked pegs
                            newlinked2.add(peg1);
                            peg2.setLinkedPegs(newlinked2);

                            official.placePeg(peg1, false);
                            official.placePeg(peg2, false);
                        } else if (((x1 - x2) == 2 || (x1 - x2) == -2) && ((y1 - y2 == 1) || (y1 - y2) == -1) && canAddLinks(pla.getHoldPeg1(), pla.getHoldPeg2())) {
                            ArrayList<Peg> newlinked1 = peg1.getLinkedPegs(); //add peg 2 to the first peg's arraylist
                            newlinked1.add(peg2);
                            peg1.setLinkedPegs(newlinked1);

                            ArrayList<Peg> newlinked2 = peg2.getLinkedPegs(); //add the peg1 to the second peg's linked pegs
                            newlinked2.add(peg1);
                            peg2.setLinkedPegs(newlinked2);

                            official.placePeg(peg1, false);
                            official.placePeg(peg2, false);
                        }
                    }
                }
            }

            return true;
        }
        else if (action instanceof PlacePegAction) { //this action allows players to place a new peg on the board
            if (action.getPlayer().equals(players[official.getTurn()]) && !pegUsed) {
                int endRows = 0;

                if (official.getTurn() == 0) {
                    endRows = 1;
                } else {
                    endRows = 2;
                }

                PlacePegAction rmP = (PlacePegAction) action;
                Peg peg = rmP.getHoldPeg();
                if (peg != null) {
                    int x = peg.getxPos();
                    int y = peg.getyPos();

                    Peg[][] temparray = official.stateToArray();
                    if (x > -1 && x < 24 && y > -1 && y < 24) { //don't allow placing out of bounds
                        if (temparray[x][y] == null) {

                            if ((endRows == 1) && (x != 0) && (x != 23)) { //don't allow placing in opponent's end Rows
                                peg = new Peg(x, y, official.getTurn(), addPegLinks(peg));
                                official.placePeg(peg, false);//add the peg to the Board array
                                lastPeg = peg;
                                pegUsed = true;
                            } else if ((endRows == 2) && (y != 0) && (y != 23)) {
                                peg = new Peg(x, y, official.getTurn(), addPegLinks(peg));
                                official.placePeg(peg, false); //add the peg to the array
                                lastPeg = peg;
                                pegUsed = true;
                            } else {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        else if (action instanceof RemoveLinkAction) { //This action allows players to remove links from their pegs
            if (action.getPlayer().equals(players[official.getTurn()])) {
                RemoveLinkAction rla = (RemoveLinkAction) action;

                if (rla.getHoldPeg1() != null && rla.getHoldPeg2() != null) {
                    Peg peg1 = rla.getHoldPeg1();
                    Peg peg2 = rla.getHoldPeg2();
                    if (peg1.getLinkedPegs().contains(peg2) && peg1.getPegTeam() == official.getTurn()) { //removes links from pegs independently
                        peg1.getLinkedPegs().remove(peg2);
                        official.placePeg(peg1, false);
                    }
                    if (peg2.getLinkedPegs().contains(peg1) && peg2.getPegTeam() == official.getTurn()) {
                        peg2.getLinkedPegs().remove(peg1);
                        official.placePeg(peg2, false);
                    }
                }

                return true;
            }
            return false;
        }
        else if (action instanceof RemovePegAction) {//this action allows players to remove their pegs
            if (action.getPlayer().equals(players[official.getTurn()])) {
                RemovePegAction rmP = (RemovePegAction) action;
                if (rmP.getHoldPeg() != null) {
                    Peg peg = rmP.getHoldPeg();//get the x and y location of the peg
                    int x = peg.getxPos();
                    int y = peg.getyPos();
                    Peg[][] temp = official.getBoard();
                            if (temp[x][y] != null) {
                                if ((temp[x][y].getPegTeam() == official.getTurn())) {
                                    Peg removepeg = temp[x][y];
                                    if (removepeg.equals(lastPeg)) { //allow removing a peg that was placed in the same turn
                                        pegUsed = false;
                                    }

                                    for (Peg p : removepeg.getLinkedPegs()) { //
                                        temp[p.getxPos()][p.getyPos()].getLinkedPegs().remove(peg);
                                    }
                                    official.setBoard(temp, true, removepeg);
                                }
                            }
                    return true;
                }
                return false;
            }
        }

        else if (action instanceof PiRuleAction) {
            if (action.getPlayer().equals(players[official.getTurn()])) {
                if (official.getTotalturns() == 1) {
                    GamePlayer player0 = players[0];
                    players[0] = players[1];
                    players[1] = player0; //unsure if this is working correctly
                } else {
                }
            }
            return true;
        }
        return false;


    }//makeMove

    /**
     * This method is called in the add PegAction of makemove.  It adds pegs to the linkedPegs ArrayList for the Peg input
     *  the canAddLinks Method completes functionality, not alllowing lines to cross each other
     *
     * @param peg
     */
    private ArrayList<Peg> addPegLinks(Peg peg) {
        ArrayList<Peg> setlinked = new ArrayList<Peg>();
        Peg[][] temparray = official.stateToArray();
        int x = peg.getxPos();
        int y = peg.getyPos();

        for (int xp = 0; xp < 24; xp++) {
            for (int yp = 0; yp < 24; yp++) {
                if (((x - xp) == 1 || (x - xp) == -1) && ((y - yp == 2) || (y - yp) == -2)) {
                    if (temparray[xp][yp] != null) {
                        if (temparray[xp][yp].getPegTeam() == official.getTurn()) {
                            if (canAddLinks(peg, temparray[xp][yp])) {
                                setlinked.add(temparray[xp][yp]);
                            }

                        }
                    }
                }
                if (((x - xp) == 2 || (x - xp) == -2) && ((y - yp == 1) || (y - yp) == -1)) {
                    if (temparray[xp][yp] != null) {
                        if (temparray[xp][yp].getPegTeam() == official.getTurn()) {
                            if (canAddLinks(peg, temparray[xp][yp])) {
                                setlinked.add(temparray[xp][yp]);
                            }
                        }
                    }
                }
            }
        }
        addCurentPegTo(setlinked, peg); //add the current peg to the linked pegs arraylist
        return setlinked;
    }

    /**
     * Returns False if the slope of the lines between peg1 and peg2 is not the same as the slop of peg3 and peg4.
     *
     * @param peg1
     * @param peg2
     * @param peg3
     * @param peg4
     * @return
     */
    private boolean slopeSame(Peg peg1, Peg peg2, Peg peg3, Peg peg4) {
        float x1 = peg1.getxPos();
        float y1 = peg1.getyPos();
        float x2 = peg2.getxPos();
        float y2 = peg2.getyPos();

        float x3 = peg3.getxPos();
        float y3 = peg3.getyPos();
        float x4 = peg4.getxPos();
        float y4 = peg4.getyPos();
        float slope1 = (y1 - y2) / (x1 - x2);
        float slope2 = (y3 - y4) / (x3 - x4);
        if (slope1 == slope2) {
            return true;
        }


        return false;
    }

    /**
     * Compares the mins and maxes of both sets of peg's x and y values to determine if they have x and y overlap, a requirement if any links are to cross
     * for links crossing.
     *
     * @param peg
     * @param comp
     * @param temp
     * @param p
     * @return
     */
    private boolean XYCross(Peg peg, Peg comp, Peg temp, Peg p) {
        int x1 = peg.getxPos();
        int y1 = peg.getyPos();
        int x2 = comp.getxPos();
        int y2 = comp.getyPos();

        int x3 = temp.getxPos();
        int y3 = temp.getyPos();
        int x4 = p.getxPos();
        int y4 = p.getyPos();

        int linkMinX = Math.min(x1, x2);
        int linkMinY = Math.min(y1, y2);
        int linkMaxX = Math.max(x1, x2);
        int linkMaxY = Math.max(y1, y2);

        int checkMinX = Math.min(x3, x4);
        int checkMinY = Math.min(y3, y4);
        int checkMaxX = Math.max(x3, x4);
        int checkMaxY = Math.max(y3, y4);

        if (linkMinX < checkMaxX && checkMinX < linkMaxX) {
            if (linkMinY < checkMaxY && checkMinY < linkMaxY) {
                return true;
            }
        }

        return false;


    }

    /**
     * Add's the current Peg to each peg in the linked arraylist's linkedPegs
     *
     * @param linked
     * @param current
     */
    private void addCurentPegTo(ArrayList<Peg> linked, Peg current) {
        Peg[][] temp = official.getBoard();
        for (Peg p : linked) {
            if (p.getLinkedPegs() != null && !p.getLinkedPegs().contains(current)) {
                temp[p.getxPos()][p.getyPos()].getLinkedPegs().add(current);
            }
        }
        official.setBoard(temp, false, null);
    }

    /**
     * Iterates through a region around the pegs, to determine if any other pegs have links that will interfere with placing
     * a new link.
     *
     * @param peg
     * @param comp
     * @return
     */
    private boolean canAddLinks(Peg peg, Peg comp) {
        int x1 = peg.getxPos();
        int y1 = peg.getyPos();
        int x2 = comp.getxPos();
        int y2 = comp.getyPos();
        Peg[][] temp = official.getBoard();

        int oldI;
        int oldJ;
        int maxI;
        int maxJ;

        if (x1 < x2) { //determine the range of the for loops
            if (y1 < y2) {
                oldI = x1 - 1;
                oldJ = y1 - 1;
                maxI = x2 + 1;
                maxJ = y2 + 1;
            } else { //y1>y2
                oldI = x1 - 1;
                oldJ = y2 - 1;
                maxI = x2 + 1;
                maxJ = y1 + 1;
            }
        } else { //x1>x2
            if (y1 < y2) {
                oldI = x2 - 1;
                oldJ = y1 - 1;
                maxI = x1 + 1;
                maxJ = y2 + 1;
            } else {
                oldI = x2 - 1;
                oldJ = y2 - 1;
                maxI = x1 + 1;
                maxJ = y1 + 1;
            }
        }
        //iterate through the array at the given values
        for (int i = oldI; i < maxI; i++) {
            for (int j = oldJ; j < maxJ; j++) {
                if ((i < 24 && i > -1) && (j < 24 && j > -1)) { //check for valid array locations
                    if (temp[i][j] != null) {

                        if (!(temp[i][j].equals(peg) || temp[i][j].equals(comp))) { //check if the peg is the same, if it is, stop, cause it cannot overlap

                            if (temp[i][j].getLinkedPegs() != null) {
                                for (Peg p : temp[i][j].getLinkedPegs()) {
                                    if (!(p.equals(peg) || p.equals(comp))) { //check if the peg we have is the same as the two pegs we are trying to connect

                                        if (XYCross(peg, comp, temp[i][j], p)) { //check maxes and mins
                                            if (!(slopeSame(peg, comp, temp[i][j], p))) { //if the slope is not the same/ they aren't parallel
                                                return false;
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

        return true; //if no pegs are crossing
    }

    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        TwixtGameState copy;
        copy = new TwixtGameState(official);
        p.sendInfo(copy);
    }//sendUpdatedSate

    /**
     * Check if the game is over
     *
     * @return a message that tells who has won the game, or null if the
     * game is not over
     */
    @Override
    protected String checkIfGameOver() {
        Peg[][] test = official.stateToArray();
        ArrayList<Peg> usedPegsTop = new ArrayList<Peg>();
        ArrayList<Peg> usedPegsRight = new ArrayList<Peg>();
        ArrayList<Peg> topPegs = new ArrayList<Peg>();
        ArrayList<Peg> rightPegs = new ArrayList<Peg>();
        boolean rightLeft = false;
        boolean topBottom = false;
        for (int i = 0; i < 24; i++) { //add the top row of pegs into an array list
            if (test[i][0] != null) {
                topPegs.add(test[i][0]);
            }
        }
        for (int i = 1; i < 24; i++) { //add the right row of pegs into an arraylist
            if (test[23][i] != null) {
                rightPegs.add(test[23][i]);
            }
        }
        if (topPegs.size() > 0) { //only run this if there are Pegs in the array/top row of the board
            topBottom = gameOverHelper(topPegs, 1, usedPegsTop);
        }
        if (rightPegs.size() > 0) { //only run this if there are Pegs in the array/right row of the board
            rightLeft = gameOverHelper(rightPegs, 2, usedPegsRight);
        }

        if (topBottom) {
            return playerNames[0] + " Won!"; //might be the wrong player names, this is the human player
        }
        if (rightLeft) {
            return playerNames[1] + " Won!";
        }

        return null;//neither player won

    }

    /**
     * This method takes in an arrayList of Pegs, a given endRow Value, and an ArrayList of Pegs that have
     * already been iterated over.  It is recursive, and returns true when a Peg has been found, connected to the
     * input array of Pegs, that has an opposite endRow value.
     *
     * @param input
     * @param endRow
     * @param usedPegs
     * @return
     */
    private boolean gameOverHelper(ArrayList<Peg> input, int endRow, ArrayList<Peg> usedPegs) {
        ArrayList<Peg> output = new ArrayList<Peg>();
        boolean won = false;
        int found = endRow + 2;
        Peg[][] array = official.stateToArray();

        if (input != null) {
            for (Peg p : input) {
                if (p != null) {
                    usedPegs.add(p);
                    if (p.getIsEndRow() == found) {
                        return true;
                    }
                    if (p.getLinkedPegs() != null) {
                        for (Peg g : p.getLinkedPegs()) {
                            if (!usedPegs.contains(g)) { //don't add the original peg, or any peg that has been used
                                output.add(array[g.getxPos()][g.getyPos()]);
                            }
                        }
                        if (gameOverHelper(output, endRow, usedPegs)) {
                            return true;
                        }
                    }
                }
            }
        }
        if (won) {
            return true;
        }
        return false;
    }
}// class TwixtLocalGame
