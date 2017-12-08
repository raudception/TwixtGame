package edu.up.cs301.twixt;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kollin Raudsepp on 11/8/2017.
 * This class populates the game board.
 */

public class Peg implements Serializable {
    private int xPos, yPos;
    private ArrayList<Peg> linkedPegs;
    private int isEndRow;
    private int pegTeam;

    /**
     * Constructor without setting the linked pegs array, for use when creating actions
     * @param X
     * @param Y
     * @param team
     */
    public Peg(int X, int Y, int team){
        linkedPegs = new ArrayList<Peg>();
        xPos = X;
        yPos = Y;
        pegTeam = team;
        setEndRow();
    }

    /**
     * Constructor for use by the TwixtLocalGame when creating a peg to place on the board, includes the linkedPegs arraylist
     * @param X
     * @param Y
     * @param team
     * @param linked
     */
    public Peg(int X, int Y, int team, ArrayList<Peg> linked){
        linkedPegs = new ArrayList<Peg>();
        xPos = X;
        yPos = Y;
        pegTeam = team;
        linkedPegs = linked;
        setEndRow();
    }

    /**
     * Copy Constructor
     * @param p
     */
    public Peg(Peg p){
        this.xPos = p.getxPos();
        this.yPos = p.getyPos();
        this.isEndRow = p.getIsEndRow();
        this.pegTeam = p.getPegTeam();
        ArrayList<Peg> newLinked = new ArrayList<Peg>();
        if(p.getLinkedPegs() != null) {
            for (Peg g : p.getLinkedPegs()){
                newLinked.add(g);
            }
        }
        this.linkedPegs = newLinked;
    }

    /**
     * Determines what endRow the given peg is in, which allows the LocalGame/checkIfGameOver to easily check where pegs are
     */
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
        ArrayList<Peg> newlinked = new ArrayList<Peg>();
        for(Peg p: linkedPegs){
            newlinked.add(p);
        }
        this.linkedPegs = newlinked;
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
