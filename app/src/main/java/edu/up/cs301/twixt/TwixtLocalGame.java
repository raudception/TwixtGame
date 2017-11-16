package edu.up.cs301.twixt;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * class PigLocalGame controls the play of the game
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class TwixtLocalGame extends LocalGame {
    private TwixtGameState official;
    /**
     * This ctor creates a new game state
     */
    public TwixtLocalGame() {
        TwixtGameState test = new TwixtGameState();
        official = new TwixtGameState(test);
    }

    /**
     * can the player with the given id take an action right now?
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx == official.getTurn()){ //this may or may not match the passed in value, playerIdx
            return true;
        }
        else{
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
        if(action instanceof EndTurnAction){
            if(action.getPlayer().equals(players[official.getTurn()])){
                if (official.getTurn() ==1){
                    official.setTurn(0);
                    official.incrementTotalTurns(); //keep track of the total number of turns
                }
                else if (official.getTurn() ==0){
                    official.setTurn(1);
                    official.incrementTotalTurns(); //keep track of the total number of turns
                }
                else{
                    Log.i("makeMove", "Invalid Turn: " + official.getTurn());
                }

            }
            sendAllUpdatedState();
        }

        if(action instanceof OfferDrawAction){
            if(action.getPlayer().equals(players[official.getTurn()])){
                //no action yet
            }
        }

        if(action instanceof PlaceLinkAction){ //unknown how to finish adding links
            if(action.getPlayer().equals(players[official.getTurn()])){
                PlaceLinkAction rmP = (PlaceLinkAction) action;
                int x1 = rmP.getHoldPeg1().getxPos();
                int y1 = rmP.getHoldPeg1().getyPos();
                int x2 = rmP.getHoldPeg2().getxPos();
                int y2 = rmP.getHoldPeg2().getyPos();
                ArrayList<Peg> temp = official.getBoard();



            }
        }

        if(action instanceof PlacePegAction){ //add checking what player is placing where
            if(action.getPlayer().equals(players[official.getTurn()])){

                PlacePegAction rmP = (PlacePegAction) action;
                Peg peg = rmP.getHoldPeg();
                int x = peg.getxPos();
                int y = peg.getyPos();

                ArrayList<Peg> temp = official.getBoard();
                Peg[][] temparray = official.stateToArray();

                if(temparray[x][y] == null){
                    temp.add(peg);
                }
                official.setBoard(temp);

//                for(int i = 0; i<temp.size(); i++){
//                    if((temp.get(i).getxPos() == x) && (temp.get(i).getyPos() == y)){
//                        Peg removepeg = temp.get(i);
//                        temp.remove(removepeg);
//                        Log.i("Remove Peg", "Peg Removed");
//                        official.setBoard(temp);
//                    }
//                }
            }
        }

        if(action instanceof RemoveLinkAction){
            if(action.getPlayer().equals(players[official.getTurn()])){

            }
        }

        if(action instanceof RemovePegAction){
            if(action.getPlayer().equals(players[official.getTurn()])){
                RemovePegAction rmP = (RemovePegAction) action;
                Peg peg = rmP.getHoldPeg();
                int x = peg.getxPos();
                int y = peg.getyPos();
                ArrayList<Peg> temp = official.getBoard();
                for(int i = 0; i<temp.size(); i++){
                    if((temp.get(i).getxPos() == x) && (temp.get(i).getyPos() == y)){
                        Peg removepeg = temp.get(i);
                        temp.remove(removepeg);
                        Log.i("Remove Peg", "Peg Removed");
                        official.setBoard(temp);
                    }
                }
            }
        }

        if(action instanceof SwitchSidesAction){ //unsure of functionality
            if(action.getPlayer().equals(players[official.getTurn()])){
                if(official.getTotalturns() ==1){
                    GamePlayer player0 = players[0];
                    players[0] =players[1];
                    players[1] =player0; //unsure if this is working correctly
                }
                else{
                    Log.i("Invalid Total Turns", "Switch Sides Action Error");
                }
            }
        }

        return false;
    }//makeMove

    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        TwixtGameState copy = new TwixtGameState();
        copy = new  TwixtGameState(copy);
        p.sendInfo(copy);
    }//sendUpdatedSate

    /**
     * Check if the game is over
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
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

        for(int i =0; i<24; i++){ //add the top row of pegs into an array list
            if(test[i][0] != null){
                topPegs.add(test[i][0]);
            }
        }
        for(int i =1; i<24; i++){ //add the right row of pegs into an arraylist
            if(test[23][i] != null){
                rightPegs.add(test[23][i]);
            }
        }
        if(topPegs.size() >0) { //only run this if there are Pegs in the array/top row of the board
            for (int i = 0; i < topPegs.size(); i++) {
                topBottom = gameOverHelper(topPegs, 1, usedPegsTop);
            }
        }
        if(rightPegs.size() >0) { //only run this if there are Pegs in the array/right row of the board
            for (int i = 0; i < rightPegs.size(); i++) {
                rightLeft = gameOverHelper(rightPegs, 1, usedPegsRight);
            }
        }

        if(topBottom){
            return playerNames[0] + " Won!"; //might be the wrong player names, this is the human player
        }
        if(rightLeft){
            return playerNames[1] + " Won!";
        }

        return null;//neither player won


        //            usedPegs.add(test[i][0]); //add the current peg to the array list of used pegs, to prevent looping
//            if(test[i][0] != null) {
//                temp = test[i][0].getLinkedPegs();
//
//                for (int y = 0; y < temp.size(); y++){
//                    if(temp.get(y).getLinkedPegs() != null){
//                        gameOverHelper(temp.get(y).getLinkedPegs(), 1, usedPegs);
//
//                    }
//                }
//            }
    }

    /**
     * This method takes in an arrayList of Pegs, a given endRow Value, and an ArrayList of Pegs that have
     * already iterated over.  It is recursive, and returns true when a Peg has been found, connected to the
     * input array of Pegs, that has an opposite value.
     * @param input
     * @param endRow
     * @param usedPegs
     * @return
     */
    private boolean gameOverHelper(ArrayList<Peg> input, int endRow, ArrayList<Peg> usedPegs) {
        ArrayList<Peg> output = new ArrayList<Peg>();
        if (input != null) {
            int found = endRow +2; //if we find a Peg with the opposite endRow value (we only pass in 1 or 2
            boolean won =false;
            //go through the pegs here

                for(int i =0; i<input.size(); i++){
                    if(endRow ==1){ //top to bottom
                        if(input.get(i).getIsEndRow() ==found){
                            won = true;
                        }
                    }

                }


            if(output != null){


                for(int i =0; i< output.size(); i++){

                }
                gameOverHelper(output, 1, usedPegs);
            }
        }
        return false;
    }
}// class TwixtLocalGame
