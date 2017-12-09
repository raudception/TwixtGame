package edu.up.cs301.twixt;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by Kollin on 10/12/2017.
 * This class holds the necessary information for the state of a TwixtGame
 */

public class TwixtGameState extends GameState implements Serializable {
    //the array that holds the game Board with all its Pegs
    private Peg[][] Board;

    //the current player's turn
    private int turn;
    //the total number of turns, used by OfferDraw and piRule
    private int totalturns =0;

    //used by players to determine if/which one was offered a draw
    private boolean offerDraw0;
    private boolean offerDraw1;

    private boolean switchPlayerNum = false;


    /**
     * Default constructor, creates a new board and sets the turn to 0
     */

    public TwixtGameState (){
        Board = new Peg[24][24];
        turn =0;
    }

    /**
     * Copy Constructor
     * @param fresh
     */
    public TwixtGameState(TwixtGameState fresh){
        this.Board = fresh.getBoard();
        this.turn = fresh.getTurn();
        this.totalturns = fresh.getTotalturns();
        this.offerDraw0 = fresh.getOfferDraw0();
        this.offerDraw1 = fresh.getOfferDraw1();
        this.switchPlayerNum = fresh.getSwitchPlayerNum();
    }


public Peg[][] stateToArray(){
    return makeBoardCopy();
} //deprecated getBoard method

    /**
     * Sets the position on the board to the given Peg, if nullPeg is true, it will delete the given peg
     * @param peg
     * @param nullPeg
     */
    public void placePeg(Peg peg, Boolean nullPeg){
        if(nullPeg){
            Board[peg.getxPos()][peg.getyPos()] = null;
        }
       else if(peg != null) {
            Board[peg.getxPos()][peg.getyPos()] = peg;
        }
    }


    public Peg[][] getBoard() {
        return makeBoardCopy();
    }

    /**
     * Deep Copy of the Board Array
     * @return
     */
    public Peg[][] makeBoardCopy(){
    Peg [][] returnval = new Peg[24][24];
    for(int i = 0; i<24; i++ ){
        for(int j = 0; j<24; j++){
            if(Board[i][j] != null) {
                returnval[i][j] = new Peg(Board[i][j]);
                if(Board[i][j].getLinkedPegs() != null) {
                    ArrayList<Peg> setNew = new ArrayList<Peg>();
                    for (Peg p : Board[i][j].getLinkedPegs()) {
                        setNew.add(p);
                    }
                    returnval[i][j].setLinkedPegs(setNew);
                }
            }
            else{
                returnval[i][j] = null;
            }
        }
    }
    return returnval;
}

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        if(turn ==0 || turn ==1){
            this.turn = turn;
        }

    }

    public int getTotalturns() {
        return totalturns;
    }
    public void incrementTotalTurns(){
        totalturns ++;
    }

    public void setBoard(Peg[][] board, Boolean nullPeg, Peg removePeg){ //needs debugging
        for(int i =0; i<24; i++){
            for(int j =0; j<24; j++){{
                    placePeg(board[i][j], false);
                if(nullPeg){
                    placePeg(removePeg, true);
                }
            }
        }
        }

    }

    public boolean getOfferDraw0(){
        return offerDraw0;
    }

    public boolean getOfferDraw1(){
        return offerDraw1;
    }

    public void setOfferDraw0(boolean b){
        offerDraw0 = b;
    }

    public void setOfferDraw1(boolean b){
        offerDraw1 = b;
    }

    public boolean getSwitchPlayerNum() {
        return switchPlayerNum;
    }

    public void setSwitchPlayerNum(boolean b) {
        this.switchPlayerNum = b;
    }


}
