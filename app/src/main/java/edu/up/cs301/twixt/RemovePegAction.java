package edu.up.cs301.twixt;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Kollin on 11/8/2017.
 */

public class RemovePegAction extends GameAction {
    private Peg HoldPeg;

    public RemovePegAction (GamePlayer player, Peg newPeg){
        super(player);
        HoldPeg = newPeg;
    }

    public Peg getHoldPeg() {
        return HoldPeg;
    }

}
