package edu.up.cs301.twixt;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * this is the primary activity for Twixt
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class TwixtMainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 2278;

    /**
     * Create the default configuration for this game:
     * - one human player vs. one computer player
     * - minimum of 2 players
     *
     * @return
     * 		the new configuration object, representing the default configuration
     */
    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // Twixt has two player types:  human and computer
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new TwixtHumanPlayer(name);
            }});
        playerTypes.add(new GamePlayerType(" Easy Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new TwixtDumbPlayer(name);
            }});
        playerTypes.add(new GamePlayerType(" Hard Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new TwixtSmartPlayer(name);
            }});

        // Create a game configuration class for Twixt:
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Twixt", PORT_NUMBER);
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer", 1); // player 2: a computer player
        defaultConfig.setRemoteData("Remote Human Player", "", 0);

        return defaultConfig;
    }//createDefaultConfig

    /**
     * create a local game
     *
     * @return
     * 		the local game, a Twixt game
     */
    @Override
    public LocalGame createLocalGame() {
        return new TwixtLocalGame();
    }

}
