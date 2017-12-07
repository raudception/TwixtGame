package edu.up.cs301.twixt;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by Kollin on 10/12/2017.
 */

public class TwixtGameState extends GameState implements Serializable {
    private Peg[][] Board;

    private int turn;
    private int totalturns =0;
    private GamePlayer[] players;

    private boolean offerDraw0;
    private boolean offerDraw1;

    public TwixtGameState (){
        Board = new Peg[24][24];
        turn =0;


    }

    public TwixtGameState(TwixtGameState fresh){
        this.Board = fresh.getBoard();
        this.turn = fresh.getTurn();
        this.totalturns = fresh.getTotalturns();
        this.offerDraw0 = fresh.getOfferDraw0();
        this.offerDraw1 = fresh.getOfferDraw1();
    }


public Peg[][] stateToArray(){
    return makeBoardCopy();
} //deprecated getBoard method

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

public Peg[][] makeBoardCopy(){
    Peg [][] returnval = new Peg[24][24];
    for(int i = 0; i<24; i++ ){
        for(int j = 0; j<24; j++){
            returnval[i][j] = Board[i][j];
            if(Board[i][j] != null && Board[i][j].getLinkedPegs() != null){
                ArrayList<Peg> setNew = new ArrayList<Peg>();
                for(Peg p: Board[i][j].getLinkedPegs()){
                    setNew.add(p);
                }
                returnval[i][j].setLinkedPegs(setNew);
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
    public void setTotalTurns(int turns){this.totalturns = turns;}

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
//                if(Board[i][j] != null && Board[i][j].getLinkedPegs() != null){
//                    ArrayList<Peg> newSet = new ArrayList<Peg>();
//                    for(Peg p : Board[i][j].getLinkedPegs()){
//                        newSet.add(p);
//                    }
//                    Board[i][j].setLinkedPegs(newSet);
//                }
            }
        }
        }

    }

    public boolean getOfferDraw0(){
        Log.i("draw","was got");
        return offerDraw0;
    }

    public boolean getOfferDraw1(){
        return offerDraw1;
    }

    public void setOfferDraw0(boolean b){
        offerDraw0 = b;
    }

    public void setOfferDraw1(boolean b){
        Log.i("draw","was set");
        offerDraw1 = b;
    }

}
