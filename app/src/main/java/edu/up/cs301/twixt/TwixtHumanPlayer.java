package edu.up.cs301.twixt;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * A GUI for a human to play Twixt. This default version displays the GUI but is incomplete
 *
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class TwixtHumanPlayer extends GameHumanPlayer implements Animator {

	/* instance variables */
   private int actionId;
    private GameAction action = null;
    protected TwixtGameState state;
    private int backgroundColor;
    private AnimationSurface surface;
    private int printOffset = 50;
    // These variables will reference widgets that will be modified during play



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
        //this method will need to paint objects, and update the states of buttons
        this.state = new TwixtGameState((TwixtGameState) info);
        Log.i("hello","world");


    }//receiveInfo

    /**
     * this method gets called when the user clicks a button, creating an action to send to the game state
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {


        if(button.getId() == R.id.PlacePegButton){
            actionId =1;
        }
        else if(button.getId() == R.id.RemovePegButton){
            actionId =2;
        }
        else if(button.getId() == R.id.PlaceLinkButton){
            actionId =3;
        }
        else if(button.getId() == R.id.RemoveLinkButton){
            actionId =4;
        }
        else if(button.getId() == R.id.OfferDrawButton){
            game.sendAction( new OfferDrawAction(this));
        }
        else if(button.getId() == R.id.EndTurnButton){
            game.sendAction( new EndTurnAction(this));
        }
    }// onClick
    public void onTouch(MotionEvent e){
        int x = (int)e.getX()/printOffset;
        int y = (int)e.getY()/printOffset;
        Log.i("onTouch", "In On Touch: " + x + " " + y);
        ArrayList<Peg> peg = new ArrayList<Peg>();
        Peg selectedPeg = new Peg(x,y,0,peg);
        if(actionId == 1){ //Pplace Peg
            game.sendAction(new PlacePegAction(this,selectedPeg));
            actionId =0;

        }
        else if(actionId == 2){ //Remove Peg
            game.sendAction( new RemovePegAction(this,selectedPeg));
            actionId =0;
        }
        else if(actionId == 3){ //placeLinkAction
            //game.sendAction( new PlaceLinkAction(this,selectedPeg));
            actionId =0;
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

        if (state == null){ return; }

        Peg[][] array = state.stateToArray();

        for(int i=0; i<24; i++){
            for(int j=0; j<24;j++){
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                if(array[i][j] != null){
                    int pegTeam = array[i][j].getPegTeam();

                    if(pegTeam == 0) {
                        paint.setColor(Color.BLUE);
                    }
                    else if(pegTeam == 1){
                        paint.setColor(Color.RED);
                    }

                }

                g.drawCircle(i*printOffset+5, j*printOffset+5, 5, paint);

            }
        }



    }





}// class TwixtHumanPlayer
