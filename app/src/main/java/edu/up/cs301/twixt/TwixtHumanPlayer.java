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
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class TwixtHumanPlayer extends GameHumanPlayer implements OnClickListener, Animator  {

	/* instance variables */
    private int actionId;
    private int humanPlayer = 0;
    private GameAction action = null;
    protected TwixtGameState state;
    private int backgroundColor;
    private AnimationSurface surface;
    private int printOffset = 50;
    private TextView turn;
    Peg previousPeg = null;
    private boolean flashBoolean = false;
    private int turnCount;
    private boolean drawAvailable = false;
    private boolean offerDraw = false;
    // These variables will reference widgets that will be modified during play
    private Button buttonPP;
    private Button buttonRP;
    private Button buttonPL;
    private Button buttonRL;
    private Button buttonOD;
    private Button buttonET;



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

        //this method will need to paint objects, and update the states of buttons
        if(!(info instanceof TwixtGameState)){
            return;
        }
        this.state = (TwixtGameState) info;

        if(state.getTurn() == 0){
            turn.setText("Your Turn");
        }
        if(state.getTotalturns() > 30){
            buttonOD.setTextColor(Color.WHITE);
            drawAvailable = true;
        }
        offerDraw = state.getOfferDraw0();
        if(offerDraw){

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
        if(state.getTurn() == humanPlayer){
            turn.setText("Green's Turn");
        }
        else{
            turn.setText("Red's Turn");
        }



    }//receiveInfo

    /**
     * this method gets called when the user clicks a button, creating an action to send to the game state
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {


        if(button.getId() == R.id.PlacePegButton){
            if(offerDraw){
                MessageBox.popUpMessage("It is a draw!",myActivity);
            }
            else{
                actionId =1;
                button.setBackgroundColor(Color.GREEN);
                buttonRP.setBackgroundColor(Color.GRAY);
                buttonPL.setBackgroundColor(Color.GRAY);
                buttonRL.setBackgroundColor(Color.GRAY);
                buttonOD.setBackgroundColor(Color.GRAY);
            }



        }
        else if(button.getId() == R.id.RemovePegButton){
            if(offerDraw){
                buttonPL.setTextColor(Color.WHITE);
                buttonRL.setTextColor(Color.WHITE);
                buttonET.setTextColor(Color.WHITE);
                buttonOD.setTextColor(Color.WHITE);
                buttonET.setBackgroundColor(Color.RED);
                offerDraw = false;
                game.sendAction( new EndTurnAction(this));
                buttonPP.setText("Place Peg");
                buttonRP.setText("Remove Peg");

            }
            else{
                actionId =2;
                button.setBackgroundColor(Color.GREEN);
                buttonPP.setBackgroundColor(Color.GRAY);
                buttonOD.setBackgroundColor(Color.GRAY);
                buttonPL.setBackgroundColor(Color.GRAY);
                buttonRL.setBackgroundColor(Color.GRAY);
            }

        }
        else if(button.getId() == R.id.PlaceLinkButton){
            actionId =3;
            button.setBackgroundColor(Color.GREEN);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonRL.setBackgroundColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
        }
        else if(button.getId() == R.id.RemoveLinkButton){
            actionId =4;
            button.setBackgroundColor(Color.GREEN);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonPL.setBackgroundColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
        }
        else if(button.getId() == R.id.OfferDrawButton){
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
        else if(button.getId() == R.id.EndTurnButton){
            //turn.setText("Opponent's Turn");
            buttonPP.setTextColor(Color.WHITE);
            buttonPP.setBackgroundColor(Color.GRAY);
            buttonRP.setBackgroundColor(Color.GRAY);
            buttonOD.setBackgroundColor(Color.GRAY);
            buttonPL.setBackgroundColor(Color.GRAY);
            buttonRL.setBackgroundColor(Color.GRAY);
            game.sendAction( new EndTurnAction(this));


        }
        else{
            return;
        }
    }// onClick

    public void onTouch(MotionEvent e){
        int x = (int)e.getX()/printOffset;
        int y = (int)e.getY()/printOffset;

        Peg[][] array = state.stateToArray();
        Peg selectedPeg;
        if(array[x][y] != null){
            selectedPeg = array[x][y];
        }
        else{
            selectedPeg = new Peg(x,y,state.getTurn());
        }

        if(actionId == 1){ //Place Peg

            /*
                Checks if the desired peg location already has a peg
                If so, flash.
                If not, send action.
             */
            if(state.getTurn() == 0){
                if(selectedPeg == array[x][y] || x==23 || x==0){
                    flashBoolean = true;
                }
                else{
                    actionId =0;
                    buttonPP.setBackgroundColor(Color.GRAY);
                    buttonPP.setTextColor(Color.BLACK);
                    game.sendAction(new PlacePegAction(this,selectedPeg));
                }
            }
            else{
                if(selectedPeg == array[x][y] || y==23 || y==0){
                    flashBoolean = true;
                }
                else{
                    actionId =0;
                    buttonPP.setBackgroundColor(Color.GRAY);
                    buttonPP.setTextColor(Color.BLACK);
                    game.sendAction(new PlacePegAction(this,selectedPeg));
                }
            }


        }
        else if(actionId == 2){ //Remove Peg
            /*
                checks if peg to be removed belongs to player
             */
            if(selectedPeg.getPegTeam() != state.getTurn()){
                flashBoolean = true;
            }
            else{
                game.sendAction( new RemovePegAction(this,selectedPeg));
                actionId =0;
                buttonRP.setBackgroundColor(Color.GRAY);
            }


        }
        else if(actionId == 3){ //placeLinkAction
            if(previousPeg == null && array[x][y] != null){
                previousPeg = selectedPeg;
            }
            else if(array[x][y] == null){
                flashBoolean = true;
            }
            else if(previousPeg.getxPos() == selectedPeg.getxPos() && previousPeg.getyPos() == selectedPeg.getyPos()){
                //flashBoolean = true;
            }
            else{

                game.sendAction( new PlaceLinkAction(this,selectedPeg,previousPeg));
                previousPeg = null;
                actionId = 0;
                buttonPL.setBackgroundColor(Color.GRAY);

            }

        }
        else if(actionId == 4) { //removeLinkAction
            if(previousPeg == null){
                previousPeg = selectedPeg;
            }
            else if(previousPeg.getxPos() == selectedPeg.getxPos() && previousPeg.getyPos() == selectedPeg.getyPos()){
                //flashBoolean = true;
            }
            else{

                game.sendAction( new RemoveLinkAction(this,selectedPeg,previousPeg));
                previousPeg = null;
                actionId = 0;
                buttonRL.setBackgroundColor(Color.GRAY);


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

        surface = (AnimationSurface) myActivity
                .findViewById(R.id.animation_surface);
        surface.setAnimator(this);

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





        //initialize widgets, buttons etc. here
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
        return backgroundColor;
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

        if(flashBoolean){
            flashColor.setColor(Color.RED);
            flashBoolean = false;
        }
        else{
            flashColor.setColor(Color.BLACK);
        }

        g.drawRect(0,0,1200,1200,flashColor);



        if (state == null){
            return;
        }
        //Log.i("tick","running");
        Peg[][] array = state.stateToArray();

        for(int i=0; i<24; i++){
            for(int j=0; j<24;j++){
                int radius = 5;
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);

                if(array[i][j] != null){

                    int pegTeam = array[i][j].getPegTeam();
                    radius = 12;

                    if(pegTeam == humanPlayer) {
                        paint.setColor(Color.GREEN);
                    }
                    else{
                        paint.setColor(Color.RED);
                    }

                }
                else if((i==0 && j==0)||(i==0 && j==23)||(i==23 && j==0)||(i==23 && j==23)){
                    paint.setColor(Color.BLACK);
                }
                g.drawCircle(i*printOffset+15, j*printOffset+15, radius, paint);

                if(array[i][j] != null){
                    if(array[i][j].getLinkedPegs() != null) {
                        for (Peg p:array[i][j].getLinkedPegs()) {
                            if(p.getLinkedPegs().contains(array[i][j])){
                                g.drawLine(i * printOffset + 15, j * printOffset + 15, (p.getxPos()) * printOffset + 15, (p.getyPos()) * printOffset + 15, paint);
                            }

                        }
                    }
                }


            }
        }
        Paint red = new Paint();
        red.setColor(Color.RED);

        Paint blue = new Paint();
        blue.setColor(Color.GREEN);

        g.drawLine(60,40,1120,40,blue);
        g.drawLine(60,1140,1120,1140,blue);
        g.drawLine(40,60,40,1120,red);
        g.drawLine(1140,60,1140,1120,red);


    }







}// class TwixtHumanPlayer
