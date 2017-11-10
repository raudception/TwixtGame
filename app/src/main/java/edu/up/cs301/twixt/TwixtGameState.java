package edu.up.cs301.twixt;

import java.util.ArrayList;

import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by Kollin on 10/12/2017.
 */

public class TwixtGameState extends GameState {
private ArrayList<Peg> Board;



    private int turn;


    public TwixtGameState (){
    Board = new ArrayList();
    turn =0;

    }

    public TwixtGameState(TwixtGameState fresh){
        fresh.setBoard(this.getBoard());
        fresh.setTurn(this.getTurn());
    }


public Peg[][] stateToArray(){ //placeholder
    Peg[][] array = new Peg[24][24];
    return array;
}

    public ArrayList<Peg> getBoard() {
        return Board;
    }

    public void setBoard(ArrayList<Peg> board) {
        Board = board;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

}
