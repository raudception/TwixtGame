package twixt;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Kollin on 11/8/2017.
 */

public class SwitchSidesAction extends GameAction {
    public SwitchSidesAction (GamePlayer player, Peg newPeg){
        super(player);
    }
}
