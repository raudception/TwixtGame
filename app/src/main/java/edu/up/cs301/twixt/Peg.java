package edu.up.cs301.twixt;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kollin on 11/8/2017.
 * This class populates the game board.
 */

public class Peg implements Serializable {
    private int xPos, yPos;
    private ArrayList<Peg> linkedPegs;
    private int isEndRow;
    private int pegTeam;

    public Peg(int X, int Y, int team){
        linkedPegs = new ArrayList<Peg>();
        xPos = X;
        yPos = Y;
        pegTeam = team;
        setEndRow();
    }

    public Peg(int X, int Y, int team, ArrayList<Peg> linked){
        linkedPegs = new ArrayList<Peg>();
        xPos = X;
        yPos = Y;
        pegTeam = team;
        linkedPegs = linked;
        setEndRow();
    }

    public String ComparePeg(){
        return "" +xPos +yPos;
    }

    private void setEndRow(){ //determine what end row the peg is in
        if(xPos > 0 && yPos == 0){ //top
            isEndRow =1;
        }
        else if (xPos ==0 && yPos >0){//left
            isEndRow = 4;
        }
        else if(xPos == 23 && yPos >0){ //right
            isEndRow = 2;
        }
        else if (xPos >0 && yPos ==23){ //bottom
            isEndRow = 3;
        }

    }


    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public ArrayList<Peg> getLinkedPegs() {
        return linkedPegs;
    }

    public void setLinkedPegs(ArrayList<Peg> linkedPegs) {
        this.linkedPegs = linkedPegs;
    }

    public int getIsEndRow() {
        return isEndRow;
    }

    public int getPegTeam() {return pegTeam;}


    /**
     * Should override the default equals method for Objects, and just compare the x and y position on the board.
     * @param peg
     * @return
     */
    @Override
    public boolean equals(Object peg){
        if( !(peg instanceof Peg)){ return false;}
        Peg peg1 = (Peg) peg;
        return (xPos == peg1.xPos && yPos == peg1.yPos && pegTeam == peg1.getPegTeam());

    }
}
