package edu.up.cs301.twixt;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Kollin on 11/8/2017.
 */

public class PlacePegAction extends GameAction implements Serializable {
    private Peg HoldPeg;

    public PlacePegAction (GamePlayer player, Peg newPeg){
        super(player);
        HoldPeg = newPeg;
    }

    public Peg getHoldPeg() {
        return HoldPeg;
    }

}
