package edu.up.cs301.twixt;

import java.util.ArrayList;

/**
 * Created by Kollin on 11/8/2017.
 */

public class Peg {
    private int xPos, yPos;
    private ArrayList<Peg> linkedPegs;



    private int isEndRow;
    private int pegTeam;

    public Peg(int X, int Y, int team, ArrayList<Peg> linked){
        xPos = X;
        yPos = Y;
        pegTeam = team;
        linkedPegs = linked;
        setEndRow();
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

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
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

    public void setPegTeam(int pegTeam) {this.pegTeam = pegTeam;}
}
