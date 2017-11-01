package edu.up.cs301.pig;

import edu.up.cs301.game.Game;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * An AI for Pig
 *
 * @author Andrew M. Nuxoll
 * @version August 2015
 */
public class PigComputerPlayer extends GameComputerPlayer {

    /**
     * ctor does nothing extra
     */
    public PigComputerPlayer(String name) {
        super(name);
    }

    /**
     * callback method--game's state has changed
     *
     * @param info
     * 		the information (presumably containing the game's state)
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        if(info instanceof PigGameState) {

            if (((PigGameState) info).GetPlayerTurnId() == 1) { //assuming the computer player is always player 2
                if (Math.random() > .5) {
                    PigHoldAction newaction = new PigHoldAction(this);
                    game.sendAction(newaction);
                } else {
                    PigRollAction newaction = new PigRollAction(this);
                    game.sendAction(newaction);
                }
            }
        }
    }//receiveInfo

}
