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

}
