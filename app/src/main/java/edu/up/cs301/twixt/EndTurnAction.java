package edu.up.cs301.twixt;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Kollin on 11/8/2017.
 * Sends an EndTurnAction to the game state with the player that created it.
 */

public class EndTurnAction extends GameAction implements Serializable {

    public EndTurnAction (GamePlayer player) {
        super(player);
    }

}
