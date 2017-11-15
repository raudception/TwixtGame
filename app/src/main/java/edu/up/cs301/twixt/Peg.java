package edu.up.cs301.twixt;

import java.util.ArrayList;

/**
 * Created by Kollin on 11/8/2017.
 */

public class Peg {
    private int xPos, yPos;
    public ArrayList<Peg> linkedPegs;



    private int isEndRow;
    private int pegTeam;

    public Peg(int X, int Y, int team, ArrayList<Peg> linked){
        xPos = X;
        yPos = Y;
        pegTeam = team;
        linkedPegs = linked;
    }

    private void checkIfEndRow(){

        isEndRow = 0;//placeholder
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

    public void setIsEndRow(int isEndRow) {this.isEndRow = isEndRow;}

    public int getPegTeam() {return pegTeam;}

    public void setPegTeam(int pegTeam) {this.pegTeam = pegTeam;}
}
