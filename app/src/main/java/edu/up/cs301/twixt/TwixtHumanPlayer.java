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
    private GameAction action = null;
    protected TwixtGameState state;
    private int backgroundColor;
    private AnimationSurface surface;
    private int printOffset = 50;
    private TextView turn;
    Peg previousPeg = null;
    private boolean flashBoolean = false;
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
        if(!(info instanceof TwixtGameState)){return;}
        //this method will need to paint objects, and update the states of buttons
        if(!(info instanceof TwixtGameState)){
            return;
        }
        this.state = (TwixtGameState) info;

        Log.i("Human Player","receiveInfo");
        if(state.getTurn() == 0){
            turn.setText("Your Turn");
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
            actionId =1;
            //button.set
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
            turn.setText("Opponent's Turn");
            game.sendAction( new EndTurnAction(this));

        }
        else{
            return;
        }
    }// onClick
    public void onTouch(MotionEvent e){
        int x = (int)e.getX()/printOffset;
        int y = (int)e.getY()/printOffset;
        //Log.i("onTouch", "In On Touch: " + x + " " + y);

        Peg[][] array = state.stateToArray();
        Peg selectedPeg;
        if(array[x][y] != null){
            selectedPeg = array[x][y];
        }
        else{
            selectedPeg = new Peg(x,y,0);
        }



        if(actionId == 1){ //Place Peg
            //Log.i("peg","placed");
            game.sendAction(new PlacePegAction(this,selectedPeg));
            actionId =0;


        }
        else if(actionId == 2){ //Remove Peg

            if(selectedPeg.getPegTeam() != 0){
                flashBoolean = true;
            }
            else{
                game.sendAction( new RemovePegAction(this,selectedPeg));
                actionId =0;
            }


        }
        else if(actionId == 3){ //placeLinkAction
            if(previousPeg == null && array[x][y] != null){
                previousPeg = selectedPeg;
            }
            else if((previousPeg.getxPos() == selectedPeg.getxPos() && previousPeg.getyPos() == selectedPeg.getyPos()) || array[x][y] == null){
                //flashBoolean = true;
            }
            else{

                game.sendAction( new PlaceLinkAction(this,selectedPeg,previousPeg));
                previousPeg = null;
                actionId = 0;

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

        Button placePegButton = (Button) activity.findViewById(R.id.PlacePegButton);
        placePegButton.setOnClickListener(this);

        Button removePegButton = (Button) activity.findViewById(R.id.RemovePegButton);
        removePegButton.setOnClickListener(this);

        Button placeLinkButton = (Button) activity.findViewById(R.id.PlaceLinkButton);
        placeLinkButton.setOnClickListener(this);

        Button removeLinkButton = (Button) activity.findViewById(R.id.RemoveLinkButton);
        removeLinkButton.setOnClickListener(this);

        Button offerDrawButton = (Button) activity.findViewById(R.id.OfferDrawButton);
        offerDrawButton.setOnClickListener(this);

        Button endTurnButton = (Button) activity.findViewById(R.id.EndTurnButton);
        endTurnButton.setOnClickListener(this);

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
                    //Log.i("peg is","not null");

                    int pegTeam = array[i][j].getPegTeam();
                    radius = 12;

                    if(pegTeam == 0) {
                       // Log.i("team is","human");
                        paint.setColor(Color.GREEN);
                    }
                    else if(pegTeam == 1){
                        //Log.i("team is","computer");
                        paint.setColor(Color.RED);
                    }




                }
                else if((i==0 && j==0)||(i==0 && j==23)||(i==23 && j==0)||(i==23 && j==23)){
                    paint.setColor(Color.BLACK);
                }
                g.drawCircle(i*printOffset+15, j*printOffset+15, radius, paint);

                if(array[i][j] != null){
                    ArrayList<Peg> linkedPegs;
                    linkedPegs = array[i][j].getLinkedPegs();
                    if(linkedPegs != null) {
                        for (Peg p: linkedPegs) {
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
