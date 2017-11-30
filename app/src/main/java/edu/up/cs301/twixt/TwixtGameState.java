package edu.up.cs301.twixt;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by Kollin on 10/12/2017.
 */

public class TwixtGameState extends GameState {
    private Peg[][] Board;

    private int turn;
    private int totalturns =0;

    public TwixtGameState (){
        Board = new Peg[24][24];
    turn =0;

    }

    public TwixtGameState(TwixtGameState fresh){
        this.Board = fresh.getBoard();
        this.turn = fresh.getTurn();
        this.totalturns = fresh.getTotalturns();
    }


public Peg[][] stateToArray(){
    return Board;
}
    public void placePeg(Peg peg){
        Board[peg.getxPos()][peg.getyPos()] = peg;
    }
    public Peg[][] getBoard() {
        return Board;
    }

    public void setBoard(Peg[][] board) {
        Board = board;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        if(turn ==0 || turn ==1){
            this.turn = turn;
        }

    }
    public void setTotalTurns(int turns){this.totalturns = turns;}

    public int getTotalturns() {
        return totalturns;
    }
    public void incrementTotalTurns(){
        totalturns ++;
    }



}
