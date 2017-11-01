package edu.up.cs301.pig;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameState;

import android.util.Log;

/**
 * class PigLocalGame controls the play of the game
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class PigLocalGame extends LocalGame {
    private PigGameState official;
    /**
     * This ctor creates a new game state
     */
    public PigLocalGame() {
        PigGameState test = new PigGameState();
        official = new PigGameState(test);
    }

    /**
     * can the player with the given id take an action right now?
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx == official.GetPlayerTurnId()){
            return true;
        }
        else{
            return false;
        }

    }

    /**
     * This method is called when a new action arrives from a player
     *
     * @return true if the action was taken or false if the action was invalid/illegal.
     */
    @Override
    protected boolean makeMove(GameAction action) {
        if(action instanceof PigRollAction ) {

            if(action.getPlayer().equals(players[official.GetPlayerTurnId()])){
                int hold = (int) Math.random() *5 +1;
                official.SetDieValue(hold);
                if(hold !=1){
                    official.SetRunning(hold);
                }
                else{
                    official.SetRunning(0);
                }

                if(super.players.length ==2){
                    official.SetPlayerTurnId(0);
                }
                return true;
            }
        }


        if(action instanceof PigHoldAction ) {
            if(action.getPlayer().equals(players[official.GetPlayerTurnId()])){
                if(action.getPlayer().equals(players[0])){
                     int store = official.GetPlayer0SCore();
                     store += official.GetRunning();
                    official.SetRunning(0);

                }
                else if(action.getPlayer().equals(players[1])){
                    int store = official.GetPlayer1Score();
                    store += official.GetRunning();
                    official.SetRunning(0);

                }
                if(super.players.length ==2){
                    official.SetPlayerTurnId(0);
                }

            return true;
            }
        }
        return false;
    }//makeMove

    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        PigGameState copy = new PigGameState();
        copy = new  PigGameState(copy);
        p.sendInfo(copy);
    }//sendUpdatedSate

    /**
     * Check if the game is over
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
     */
    @Override
    protected String checkIfGameOver() {
        if(official.GetPlayer0SCore() >= 50){
            return "Congratulations " + playerNames[0] + "your score is " + official.GetPlayer0SCore();
        }
        else if(official.GetPlayer1Score() >= 50){
            return "Congratulations " + playerNames[1] + "your score is " + official.GetPlayer1Score();
        }
        return null;
    }

}// class PigLocalGame
