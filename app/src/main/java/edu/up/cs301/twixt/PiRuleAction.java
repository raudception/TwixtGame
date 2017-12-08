package edu.up.cs301.twixt;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by tdog9 on 12/4/2017.
 * Sends an PiRuleAction to the game state with the player that created it.
 */

public class PiRuleAction extends GameAction implements Serializable {

    public PiRuleAction (GamePlayer player){ super(player); }
}
