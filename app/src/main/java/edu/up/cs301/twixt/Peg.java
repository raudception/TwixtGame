package edu.up.cs301.twixt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Kollin on 11/8/2017.
 * This class populates the game board.
 */

public class Peg {
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

    public void draw(Canvas canvas){

        Paint paint = new Paint();
        if(pegTeam == 0) {
            paint.setColor(Color.BLUE);
        }
        else if(pegTeam == 1){
            paint.setColor(Color.RED);
        }
        else{
            paint.setColor(Color.WHITE);
        }
        canvas.drawCircle(xPos, yPos, 5, paint);
    }

    /**
     * Should override the default equals method for Objects, and just compare the x and y position on the board.
     * @param peg
     * @return
     */
    @Override
    public boolean equals(Object peg){
        Peg peg1 = (Peg) peg;
        if(this == peg){
            return true;
        }
        if(xPos == peg1.getxPos() && yPos == peg1.yPos && pegTeam == peg1.getPegTeam()){
            return true;
        }
        return false;
    }
}
