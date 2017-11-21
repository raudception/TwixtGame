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
    private boolean pegUsed = false;
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
        Log.i("local game","receving");

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
                pegUsed = false;

            }
            sendAllUpdatedState();
        }

        if(action instanceof OfferDrawAction){
            if(action.getPlayer().equals(players[official.getTurn()])){
                Log.i("Offer Draw Action", "Player: " + playerNames[official.getTurn()] + " Offered draw"); //placeholder
                sendAllUpdatedState();
                return true;
            }
        }

        if(action instanceof PlaceLinkAction){ //doesn't check for existing connections
            if(action.getPlayer().equals(players[official.getTurn()])){
                PlaceLinkAction pla = (PlaceLinkAction) action;
                int x1 = pla.getHoldPeg1().getxPos();
                int y1 = pla.getHoldPeg1().getyPos();
                int x2 = pla.getHoldPeg2().getxPos();
                int y2 = pla.getHoldPeg2().getyPos();
                ArrayList<Peg> temp = official.getBoard();

                if( ((x1 - x2) ==1 || (x1 - x2) ==-1) && ((y1-y2 ==2) || (y1-y2) ==-2)){
                    ArrayList<Peg> newlinked1 = pla.getHoldPeg1().getLinkedPegs(); //add peg 2 to the first peg's arraylist
                    newlinked1.add(pla.getHoldPeg2());
                    pla.getHoldPeg1().setLinkedPegs(newlinked1);

                    ArrayList<Peg> newlinked2 = pla.getHoldPeg2().getLinkedPegs(); //add the peg1 to the second peg's linked pegs
                    newlinked2.add(pla.getHoldPeg1());
                    pla.getHoldPeg2().setLinkedPegs(newlinked2);
                }

                else if(((x1 - x2) ==2 || (x1 - x2) ==-2) && ((y1-y2 ==1) || (y1-y2) ==-2)){
                    ArrayList<Peg> newlinked1 = pla.getHoldPeg1().getLinkedPegs(); //add peg 2 to the first peg's arraylist
                    newlinked1.add(pla.getHoldPeg2());
                    pla.getHoldPeg1().setLinkedPegs(newlinked1);

                    ArrayList<Peg> newlinked2 = pla.getHoldPeg2().getLinkedPegs(); //add the peg1 to the second peg's linked pegs
                    newlinked2.add(pla.getHoldPeg1());
                    pla.getHoldPeg2().setLinkedPegs(newlinked2);
                }
            }
            sendAllUpdatedState();
            return true;
        }

        if(action instanceof PlacePegAction){
            Log.i("action is","place peg");
            if(action.getPlayer().equals(players[official.getTurn()]) && !pegUsed){
                Log.i("action is","happening");
                int endRows =0;

                if(official.getTurn() ==0){endRows =1;}
                else{endRows =2;}

                PlacePegAction rmP = (PlacePegAction) action;
                Peg peg = rmP.getHoldPeg();
                int x = peg.getxPos();
                int y = peg.getyPos();

                ArrayList<Peg> temp = official.getBoard();
                Peg[][] temparray = official.stateToArray();

                if(temparray[x][y] == null){
                    Log.i("End of Place", "xy = null");

                    if( (endRows ==1) && (x!=0) && (x!=23)){ //don't allow placing in opponent's end Rows
                        peg = new Peg(x,y,official.getTurn(),addPegLinks(peg));
                        temp.add(peg); //add the peg to the temp array
                    }
                    else if ( (endRows ==2) && (y!=0) && (y != 23) ){
                        peg = new Peg(x,y,official.getTurn(),addPegLinks(peg));
                        temp.add(peg); //add the peg to the temp array
                    }
                    else{
                        return false;
                    }
                }
                Log.i("End of Place", "End of Peg");
                official.setBoard(temp); //set the board's state, including the new peg
                pegUsed = true;
                sendAllUpdatedState();
                return true;
               // sendAllUpdatedState();
            }
        }

        if(action instanceof RemoveLinkAction){
            if(action.getPlayer().equals(players[official.getTurn()])){
                RemoveLinkAction rla = (RemoveLinkAction) action;
                Log.i("removing","links please");
                if(rla.getHoldPeg1().getLinkedPegs() != null && rla.getHoldPeg2().getLinkedPegs() != null){
                    Log.i("removing","links");
                    if(rla.getHoldPeg1().getLinkedPegs().contains(rla.getHoldPeg2()) && rla.getHoldPeg2().getLinkedPegs().contains(rla.getHoldPeg1()) ){ //if each peg has the other remove them
                        rla.getHoldPeg1().getLinkedPegs().remove(rla.getHoldPeg2());
                        rla.getHoldPeg2().getLinkedPegs().remove(rla.getHoldPeg1());
                    }
                }
            }
            sendAllUpdatedState();
            return true;
        }

        if(action instanceof RemovePegAction){ //doesn't remove it from other linkedPegs
            if(action.getPlayer().equals(players[official.getTurn()])){
                RemovePegAction rmP = (RemovePegAction) action;
                Peg peg = rmP.getHoldPeg();
                int x = peg.getxPos();
                int y = peg.getyPos();
                ArrayList<Peg> temp = official.getBoard();
                for(int i = 0; i<temp.size(); i++){
                    if((temp.get(i).getxPos() == x) && (temp.get(i).getyPos() == y) && (temp.get(i).getPegTeam() == official.getTurn())){
                        Peg removepeg = temp.get(i);
                        temp.remove(removepeg);
                        Log.i("Remove Peg", "Peg Removed");
                        official.setBoard(temp);
                    }
                }
            }
            sendAllUpdatedState();
            return true;
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
            sendAllUpdatedState();
            return true;
        }

        return false;
    }//makeMove

    /**
     * This method is called in the add PegAction of makemove.  It adds pegs to the linkedPegs ArrayList for the Peg input
     * @param peg
     */
    public ArrayList<Peg> addPegLinks(Peg peg){
            ArrayList<Peg> setlinked = new ArrayList<Peg>();
        Peg[][] temparray = official.stateToArray();
        int x = peg.getxPos();
        int y = peg.getyPos();
            Log.i("End of Place", "In Do Set: " );
            for (int xp = 0; xp < 24; xp++) {
                for (int yp = 0; yp < 24; yp++) {
                    if (((x - xp) == 1 || (x - xp) == -1) && ((y - yp == 2) || (y - yp) == -2)) {
                        if(temparray[xp][yp] != null){
                            if(temparray[xp][yp].getPegTeam() ==official.getTurn()){
                                setlinked.add(temparray[xp][yp]);
                            }
                        }
                    }
                    if (((x - xp) == 2 || (x - xp) == -2) && ((y - yp == 1) || (y - yp) == -1)) {
                        if(temparray[xp][yp] != null){
                            if(temparray[xp][yp].getPegTeam() ==official.getTurn()){
                                setlinked.add(temparray[xp][yp]);
                            }
                        }
                    }
                }
            }
            addCurentPegTo(setlinked, peg); //add the current peg to the linked pegs arraylist
            return setlinked;
    }

    /**
     * Add's the current Peg to each peg in the linked arraylist's linkedPegs
     * @param linked
     * @param current
     */
    public void addCurentPegTo(ArrayList<Peg> linked, Peg current){
        ArrayList<Peg> tempBoard = official.getBoard();
        for(Peg p: linked){
            if(!p.getLinkedPegs().contains(current)){
                p.getLinkedPegs().add(current);
            }
        }
    }

    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        TwixtGameState copy = new TwixtGameState();
        copy =  new TwixtGameState(official);
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
                topBottom = gameOverHelper(topPegs, 1, usedPegsTop);
        }
        if(rightPegs.size() >0) { //only run this if there are Pegs in the array/right row of the board
                rightLeft = gameOverHelper(rightPegs, 2, usedPegsRight);
        }

        if(topBottom){
            return playerNames[0] + " Won!"; //might be the wrong player names, this is the human player
        }
        if(rightLeft){
            return playerNames[1] + " Won!";
        }

        return null;//neither player won

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
        boolean won = false;
        int found = endRow +2;
        Peg [][] array = official.stateToArray();

       if(input != null){
           for (Peg p: input){
               usedPegs.add(p);
               if(p.getIsEndRow() == found){
                   return true;
               }
               if(p.getLinkedPegs() != null){
                   for(Peg g: p.getLinkedPegs()){
                       if(!usedPegs.contains(g)){ //don't add the original peg, or any peg that has been used
                           output.add(array[g.getxPos()][g.getyPos()]);
                           Log.i("Helper output", "add");
                       }
                   }
                   if(gameOverHelper(output, endRow, usedPegs)){
                       Log.i("Helper", "True");
                       return true;
                   }
               }
           }
       }
       if(won){
           return true;
       }
        return false;
    }
}// class TwixtLocalGame
