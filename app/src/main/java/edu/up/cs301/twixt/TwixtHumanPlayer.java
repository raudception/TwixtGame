package edu.up.cs301.twixt;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.MessageBox;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A GUI for a human to play Twixt. This default version displays the GUI but is incomplete
 *
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl, Taylor Odem
 * @version November 2017
 */
public class TwixtHumanPlayer extends GameHumanPlayer implements OnClickListener, Animator  {

    /* instance variables */
    private int actionId; //tells which action is selected
    private int humanPlayer = 0; //assigns a value to the human player
    protected TwixtGameState state; //stores the passed in game state
    private AnimationSurface surface; //the current animation surface
    private int printOffset = 50; //the value between dots
    Peg previousPlacedPeg = null; //previously placed Peg
    Peg previousPeg = null; //previously selected Peg
    private boolean flashBoolean = false; //boolean telling whether a flash needs to occur
    private boolean drawAvailable = false; //tells whether a draw is available
    private boolean offerDraw = false; //tells whether a draw has been offered
    private boolean offerDrawResolved = false; //tells whether offerDraw has been resolved
    private boolean piRuleOffered = false; //tells whether the pi rule is applicable
    private boolean piRuleResolved = false; //tells whether the pi rule has been resolved
    private boolean placePegAvailable = true; //tells whether a player may still place a Peg

    // These variables will reference widgets that will be modified during play
    private Button buttonPP; //placePeg
    private Button buttonRP; //removePeg
    private Button buttonPL; //placeLink
    private Button buttonRL; //removeLink
    private Button buttonOD; //offerDraw
    private Button buttonET; //endTurn
    private TextView turn; //displays whose turn it is



    // the android activity that we are running
    private GameMainActivity myActivity;

    /**
     * constructor does nothing extra
     */
    public TwixtHumanPlayer(String name) {
        super(name);
    }

    /**
     * Returns the GUI's top view object
     *
     * @return
     * 		the top object in the GUI's view heirarchy
     */
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        if(!(info instanceof TwixtGameState)){return;}
        state = null;

        //ensures info is a TwixtGameState object
        if(!(info instanceof TwixtGameState)){
            return;
        }
        this.state = (TwixtGameState) info;

        //offer draw button becomes available after 20 turns
        if(state.getTotalturns() > 20){
            buttonOD.setTextColor(Color.WHITE);
            drawAvailable = true;
        }

        //offerDraw is set to true when other player makes an offerDraw action
        if( this.playerNum == 0 ){
            offerDraw = state.getOfferDraw0();
        }
        else if( this.playerNum == 1 ){
            offerDraw = state.getOfferDraw1();
        }

        //creates a dialog box and changes buttons when a draw is offered, but not resolved
        if(offerDraw && !offerDrawResolved){

            MessageBox.popUpMessage("A draw has been offered!",myActivity);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonPP.setText("Accept");
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonRP.setText("Reject");
            buttonPL.setBackgroundColor(Color.GRAY);
            buttonPL.setTextColor(Color.GRAY);
            buttonRL.setBackgroundColor(Color.GRAY);
            buttonRL.setTextColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
            buttonOD.setTextColor(Color.GRAY);
            buttonET.setBackgroundColor(Color.GRAY);
            buttonET.setTextColor(Color.GRAY);
            state.setOfferDraw0(false);
        }

        //sets turn text view based on whose turn it is
        if(state.getTurn() == humanPlayer){
            turn.setText("Green's Turn");
        }
        else{
            turn.setText("Red's Turn");
        }

        /*
            After the first turn, the second player will be offered to switch sides.
            The loop searches through the board to find the first Peg placed and switches the value of that Peg to the second player's color
            The first player then gets to make a move again
          */
        if(state.getTotalturns() == 1 && this.playerNum != 0 && piRuleResolved == false) {
            Peg[][] array = state.getBoard();
            for (int i = 0; i < 24; i++) {
                for (int j = 0; j < 24; j++) {
                    if(array[i][j] != null) {
                        if (array[i][j].getyPos() != 0 && array[i][j].getyPos() != 23 && array[i][j].getxPos() != 0 && array[i][j].getxPos() != 23) {
                            MessageBox.popUpMessage("Would you like to switch side?", myActivity);
                            buttonPP.setBackgroundColor(Color.GRAY);
                            buttonPP.setText("Accept");
                            buttonRP.setBackgroundColor(Color.GRAY);
                            buttonRP.setText("Reject");
                            buttonPL.setBackgroundColor(Color.GRAY);
                            buttonPL.setTextColor(Color.GRAY);
                            buttonRL.setBackgroundColor(Color.GRAY);
                            buttonRL.setTextColor(Color.GRAY);
                            buttonOD.setBackgroundColor(Color.GRAY);
                            buttonOD.setTextColor(Color.GRAY);
                            buttonET.setBackgroundColor(Color.GRAY);
                            buttonET.setTextColor(Color.GRAY);
                            piRuleOffered = true;
                            piRuleResolved = true;
                            break;
                        }
                        else{piRuleResolved = true;}
                    }
                }
            }
        }





    }//receiveInfo

    /**
     * this method gets called when the user clicks a button, creating an action to send to the game state
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {

        /*
            if statements check which button has been pressed
            turns the button selected green, other buttons return to gray
         */
        if(button.getId() == R.id.PlacePegButton){

            //if the other player has offered a draw, pressing this button will accept the draw
            if(offerDraw && !offerDrawResolved){
                MessageBox.popUpMessage("It is a draw!",myActivity);
                offerDraw = false;
                offerDrawResolved = true;
                myActivity.setGameOver(true);
            }
            //if the pi rule is applicable, pressing this button will accept the switch
            else if(piRuleOffered){
                MessageBox.popUpMessage("Side's switched!",myActivity);
                game.sendAction( new PiRuleAction(this));
                game.sendAction( new EndTurnAction(this));
                buttonPL.setTextColor(Color.WHITE);
                buttonRL.setTextColor(Color.WHITE);
                buttonET.setTextColor(Color.WHITE);
                buttonOD.setTextColor(Color.WHITE);
                buttonET.setBackgroundColor(Color.RED);
                piRuleOffered = false;
                buttonPP.setText("Place Peg");
                buttonRP.setText("Remove Peg");
            }
            //sets actionId to 1
            else{

                if(placePegAvailable) {
                    actionId = 1;
                    button.setBackgroundColor(Color.GREEN);
                    buttonRP.setBackgroundColor(Color.GRAY);
                    buttonPL.setBackgroundColor(Color.GRAY);
                    buttonRL.setBackgroundColor(Color.GRAY);
                    buttonOD.setBackgroundColor(Color.GRAY);
                }
                else{
                    flashBoolean = true;
                }
            }



        }
        else if(button.getId() == R.id.RemovePegButton){

            //if other player offers a draw, this button will reject the draw
            if(offerDraw && !offerDrawResolved){
                buttonPL.setTextColor(Color.WHITE);
                buttonRL.setTextColor(Color.WHITE);
                buttonET.setTextColor(Color.WHITE);
                buttonOD.setTextColor(Color.WHITE);
                buttonET.setBackgroundColor(Color.RED);
                offerDraw = false;
                offerDrawResolved = true;
                game.sendAction( new EndTurnAction(this));
                buttonPP.setText("Place Peg");
                buttonRP.setText("Remove Peg");

            }
            //if pi rule is applicable, this button will reject the draw
            else if(piRuleOffered){
                buttonPL.setTextColor(Color.WHITE);
                buttonRL.setTextColor(Color.WHITE);
                buttonET.setTextColor(Color.WHITE);
                buttonOD.setTextColor(Color.WHITE);
                buttonET.setBackgroundColor(Color.RED);
                piRuleOffered = false;
                buttonPP.setText("Place Peg");
                buttonRP.setText("Remove Peg");
            }
            //sets actionId to 2
            else{
                actionId =2;
                button.setBackgroundColor(Color.GREEN);
                buttonPP.setBackgroundColor(Color.GRAY);
                buttonOD.setBackgroundColor(Color.GRAY);
                buttonPL.setBackgroundColor(Color.GRAY);
                buttonRL.setBackgroundColor(Color.GRAY);
            }

        }
        //sets actionId to 3
        else if(button.getId() == R.id.PlaceLinkButton){
            actionId =3;
            button.setBackgroundColor(Color.GREEN);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonRL.setBackgroundColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
        }
        //sets actionId to 4
        else if(button.getId() == R.id.RemoveLinkButton){
            actionId =4;
            button.setBackgroundColor(Color.GREEN);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonPL.setBackgroundColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
        }
        else if(button.getId() == R.id.OfferDrawButton){

            //sends an offerDraw action if available
            if(drawAvailable){
                game.sendAction( new OfferDrawAction(this));
                button.setBackgroundColor(Color.GREEN);
                buttonPP.setBackgroundColor(Color.GRAY);
                buttonRP.setBackgroundColor(Color.GRAY);
                buttonPL.setBackgroundColor(Color.GRAY);
                buttonRL.setBackgroundColor(Color.GRAY);

            }
            else{
                flashBoolean = true;
            }


        }
        //ends the current player's turn
        else if(button.getId() == R.id.EndTurnButton){
            previousPeg = null;
            buttonPP.setTextColor(Color.WHITE);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
            buttonPL.setBackgroundColor(Color.GRAY);
            buttonRL.setBackgroundColor(Color.GRAY);
            game.sendAction( new EndTurnAction(this));
            placePegAvailable = true;


        }
        else{
            return;
        }
    }// onClick

    public void onTouch(MotionEvent e){
        int x = (int)e.getX()/printOffset; //x value for the area on the board that is touched
        int y = (int)e.getY()/printOffset; //y value for the area on the board that is touched

        //loop checks to make sure the touch is not out of bounds
        if((x>-1 && x<24) && (y<24 && y>-1)) {
            Peg[][] array = state.stateToArray(); //creates an array from the passed in game state
            Peg selectedPeg = null;

            //if touched area of the board contains a Peg, select that Peg
            if (array[x][y] != null) {
                selectedPeg = array[x][y];
            }

            if (actionId == 1) { //Place Peg

            /*
                Checks if the desired peg location already has a peg
                If so, flash.
                If not, send action.
             */
                selectedPeg = new Peg(x, y, state.getTurn()); //creates new Peg in touched location

                //if statements make sure Peg isn't placed in opponent's end row
                if (state.getTurn() == 0) {
                    if (selectedPeg == array[x][y] || x == 23 || x == 0) {
                        flashBoolean = true;
                    } else {
                        actionId = 0;
                        buttonPP.setBackgroundColor(Color.GRAY);
                        buttonPP.setTextColor(Color.BLACK); //setting text to black to show another Peg cannot be placed
                        game.sendAction(new PlacePegAction(this, selectedPeg)); //sends action to local game
                        previousPlacedPeg = selectedPeg;
                        placePegAvailable = false;
                    }
                } else {
                    if (selectedPeg == array[x][y] || y == 23 || y == 0) {
                        flashBoolean = true;
                    } else {
                        actionId = 0;
                        buttonPP.setBackgroundColor(Color.GRAY);
                        buttonPP.setTextColor(Color.BLACK);
                        game.sendAction(new PlacePegAction(this, selectedPeg));
                        previousPlacedPeg = selectedPeg;
                        placePegAvailable = false;
                    }
                }


            } else if (actionId == 2) { //Remove Peg

                //checks if peg to be removed belongs to player
                if(selectedPeg != null) {
                    if (selectedPeg.getPegTeam() != state.getTurn()) {
                        flashBoolean = true;
                    } else {
                        game.sendAction(new RemovePegAction(this, selectedPeg));
                        actionId = 0;
                        buttonRP.setBackgroundColor(Color.GRAY);

                        //checks if peg removed is the Peg just placed
                        if (selectedPeg.getxPos() == previousPlacedPeg.getxPos() && selectedPeg.getyPos() == previousPlacedPeg.getyPos()) {
                            buttonPP.setTextColor(Color.WHITE); //turns text in place peg button back to white
                            placePegAvailable = true;
                        }
                    }


                }
            } else if (actionId == 3) { //placeLinkAction

                //assigns previousPeg the Peg selected
                if (previousPeg == null && array[x][y] != null) {
                    previousPeg = selectedPeg;
                }
                //checks if place selected is empty
                else if (array[x][y] == null) {
                    flashBoolean = true;
                }
                //checks if Peg selected is the player's team
                else if(array[x][y].getPegTeam() != this.playerNum){
                    flashBoolean = true;
                }
                //the selected Peg will be linked with the Peg assigned to previousPeg
                else if(selectedPeg.getxPos() != previousPeg.getxPos() && selectedPeg.getyPos() != previousPeg.getyPos()){
                    game.sendAction(new PlaceLinkAction(this, selectedPeg, previousPeg));
                    previousPeg = null;
                    actionId = 0;
                    buttonPL.setBackgroundColor(Color.GRAY);

                }

            } else if (actionId == 4) { //removeLinkAction

                //assigns previousPeg the Peg selected
                if (previousPeg == null && selectedPeg != null ) {
                    previousPeg = selectedPeg;
                }
                //checks if place selected is empty
                else if (array[x][y] == null) {
                    flashBoolean = true;
                }
                //checks if Peg selected is the player's team
                else if(array[x][y].getPegTeam() != this.playerNum){
                    flashBoolean = true;
                }

                //link between the selected Peg and the previousPeg will be removed
                else if(selectedPeg.getxPos() != previousPeg.getxPos() && selectedPeg.getyPos() != previousPeg.getyPos()){

                    game.sendAction(new RemoveLinkAction(this, selectedPeg, previousPeg));
                    previousPeg = null;
                    actionId = 0;
                    buttonRL.setBackgroundColor(Color.GRAY);


                }


            }
        }
    }


    /**
     * callback method--our game has been chosen/rechosen to be the GUI,
     * called from the GUI thread
     *
     * @param activity
     * 		the activity under which we are running
     */
    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;
        activity.setContentView(R.layout.twixt_layout);

        //assigns the surface
        surface = (AnimationSurface) myActivity
                .findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        //creates widgets
        buttonPP = (Button) activity.findViewById(R.id.PlacePegButton);
        buttonPP.setOnClickListener(this);

        buttonRP = (Button) activity.findViewById(R.id.RemovePegButton);
        buttonRP.setOnClickListener(this);

        buttonPL = (Button) activity.findViewById(R.id.PlaceLinkButton);
        buttonPL.setOnClickListener(this);

        buttonRL = (Button) activity.findViewById(R.id.RemoveLinkButton);
        buttonRL.setOnClickListener(this);

        buttonOD = (Button) activity.findViewById(R.id.OfferDrawButton);
        buttonOD.setOnClickListener(this);

        buttonET = (Button) activity.findViewById(R.id.EndTurnButton);
        buttonET.setOnClickListener(this);

        turn = (TextView) activity.findViewById(R.id.turnDisplay);
    }//setAsGui

    public int interval() {
        // 1/20 of a second
        return 50;
    }

    /**
     * @return
     * 		the background color
     */
    public int backgroundColor() {
        return 0;
    }

    /**
     * @return
     * 		whether the animation should be paused
     */
    public boolean doPause() {
        return false;
    }

    /**
     * @return
     * 		whether the animation should be terminated
     */
    public boolean doQuit() {
        return false;
    }

    public void tick(Canvas g){
        Paint flashColor = new Paint();

        //flashes the screen red for one tick
        if(flashBoolean){
            flashColor.setColor(Color.RED);
            flashBoolean = false;
        }
        else{
            flashColor.setColor(Color.BLACK);
        }

        g.drawRect(0,0,1200,1200,flashColor);


        //null check on state
        if (state == null){
            return;
        }

        Peg[][] array = state.stateToArray();

        //loop paints small white dot at each index in the 2-dimensional board array
        for(int i=0; i<24; i++){
            for(int j=0; j<24;j++){
                int radius = 5;
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);

                //paints a Peg at the array index
                if(array[i][j] != null){

                    int pegTeam = array[i][j].getPegTeam();
                    radius = 12;

                    if(pegTeam == humanPlayer) {
                        paint.setColor(Color.GREEN); //Pegs paint green for player 1
                    }
                    else{
                        paint.setColor(Color.RED); //{egs paint red for player 2
                    }

                }
                //corner places paint black
                else if((i==0 && j==0)||(i==0 && j==23)||(i==23 && j==0)||(i==23 && j==23)){
                    paint.setColor(Color.BLACK);
                }

                //paints dot
                g.drawCircle(i*printOffset+15, j*printOffset+15, radius, paint);

                //draws line between linked Pegs
                if(array[i][j] != null){
                    if(array[i][j].getLinkedPegs() != null) {
                        for (Peg p:array[i][j].getLinkedPegs()) {
                            if(array[p.getxPos()][p.getyPos()] != null) { //check to see if the peg to link to exists

                                g.drawLine(i * printOffset + 15, j * printOffset + 15, (p.getxPos()) * printOffset + 15, (p.getyPos()) * printOffset + 15, paint);

                            }
                        }
                    }
                }


            }
        }

        //paints lines to indicate end rows
        Paint red = new Paint();
        red.setColor(Color.RED);

        Paint green = new Paint();
        green.setColor(Color.GREEN);

        g.drawLine(60,40,1120,40,green);
        g.drawLine(60,1140,1120,1140,green);
        g.drawLine(40,60,40,1120,red);
        g.drawLine(1140,60,1140,1120,red);

    }//tick

}// class TwixtHumanPlayer
