package edu.up.cs301.pig;

import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by Kollin on 10/12/2017.
 */

public class PigGameState extends GameState {
    private int playerturnid;
    private int player0score;
    private int player1score;
    private int running;
    private int dievalue;


    public PigGameState (){
        playerturnid =0;
        player0score =0;
        player1score =0;
        running =0;
        dievalue =0;


    }

    public PigGameState(PigGameState fresh){
        fresh.SetPlayer1Score(player1score);
        fresh.SetPlayer0Score(player0score);
        fresh.SetRunning(running);
        fresh.SetPlayerTurnId(playerturnid);
    }



    public int GetPlayerTurnId (){
        return playerturnid;
    }

    public int GetPlayer0SCore (){
        return player1score;
    }
    public int GetPlayer1Score (){
        return player1score;
    }

    public int GetRunning (){
        return running;
    }

    public void SetPlayerTurnId (int newturn){
        if(playerturnid ==1){
            playerturnid =0;
        }
        if(playerturnid ==0){
            playerturnid =1;
        }

    }
    public void SetPlayer0Score (int score){
        player0score = score;
    }
    public void SetPlayer1Score (int score){
        player1score= score;
    }
    public void SetRunning (int newrunning){
        running = newrunning;
    }
    public void SetDieValue (int hold) {dievalue = hold;}
    public int GetDieValue(){return dievalue;}


}
