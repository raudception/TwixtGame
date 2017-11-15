package edu.up.cs301.twixt;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        if(action instanceof PlacePegAction){
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
        for(int i =0; i<24; i++){
                ArrayList<Peg> temp = test[i][0].getLinkedPegs();
                    for(int y =0; y<temp.size(); y++){
                        ArrayList<Peg> linked = temp.get(i).getLinkedPegs();
                            for(int t =0; t<linked.size(); t++){

                            }
                    }
        }
        return null;
    }

    //private ArrayList<Peg> gameOverHelper();
}// class PigLocalGame
