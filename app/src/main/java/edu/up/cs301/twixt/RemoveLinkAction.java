package edu.up.cs301.twixt;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Kollin on 11/8/2017.
 */

public class RemoveLinkAction extends GameAction implements Serializable {
    private Peg HoldPeg1;
    private Peg HoldPeg2;

    public RemoveLinkAction (GamePlayer player, Peg newPeg1, Peg newPeg2){
        super(player);
        HoldPeg1 = newPeg1;
        HoldPeg2 = newPeg2;
    }

    public Peg getHoldPeg1() {
        return HoldPeg1;
    }
    public Peg getHoldPeg2() {
        return HoldPeg2;
    }

}
