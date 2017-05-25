package spring2017.cs478.raghavendra.project4;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

/*
* References: Examples shown during the class by Prof in blackboard
* Android documentation: https://developer.android.com/reference/classes.html
* */

/*
* Implementation constraints are satisfied at the following places:
*
* 1. Under classes Player1 and Player2, Looper is prepared and looped over
*    and handlers are created in MainActivity (t1Handler, t2Handler)
* 2. Messages in the job queue of the worker threads are included in the
*    main thread handler's handleMessage method. Runnables are included
*    in each Player class' handleMessage method under switch case CONTINUE
* 3. The two threads uses different strategies in order to make their next
*    moves. Please see class header documentation of each Player class to
*    know the strategies
* 4. Thread.sleep are used at appropriate places in order to make the game
*    visible to the user.
* */

/*
* The Board:
* The board is nothing but nine buttons arranged in a grid manner, with
* lines drawn over it. Please see empty.jpg under drawable folder to see
* the pattern of lines
* */

public class MainActivity extends AppCompatActivity {

    //Player 1 & 2 job queue handlers
    private Handler t1Handler;
    private Handler t2Handler;
    Thread t1;
    Thread t2;
    public static final String TAG = "MainActivity";

    /*
    * Some of the status indicating constants
    * */
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int GAME_OVER = 0;
    public static final int RESTART = 4;
    public static final int PLAYER_1_WINS = 5;
    public static final int PLAYER_2_WINS = 6;
    public static final int CONTINUE = 3;

    //Used to see which all blocks are occupied
    public ArrayList<Integer> mOccupiedBlocks = new ArrayList<>();
    //Used to store the board like appearance
    public ArrayList<ImageButton> mImgButtons = new ArrayList<>();
    //Used to store the blocks occupied by player1
    public ArrayList<Integer> mPlayer1Occupancy = new ArrayList<>();
    //Used to store the blocks occupied by player2
    public ArrayList<Integer> mPlayer2Occupancy = new ArrayList<>();
    //Used to store rows and column numbers of the board to check
    //the winner
    public ArrayList<Integer> row1 = new ArrayList<>();
    public ArrayList<Integer> row2 = new ArrayList<>();
    public ArrayList<Integer> row3 = new ArrayList<>();
    public ArrayList<Integer> col1 = new ArrayList<>();
    public ArrayList<Integer> col2 = new ArrayList<>();
    public ArrayList<Integer> col3 = new ArrayList<>();
    //Displays status of the game
    public TextView mTextView;

    /**
     * This method displays the player's move to the user
     * */
    private int makeMoveAndCheckGameStatus(int from, int player, int to){
        //Get the corresponding image resource
        int imgRes = (player == PLAYER_1)? R.drawable.dark_blue_resized : R.drawable.burgundy_resized;
        int gameStatus = CONTINUE;
        if(from != -1) {
            //if all three pieces are out, then place empty slot in the
            //place from which the a piece has to be shifted
            mImgButtons.get(from).setImageResource(R.drawable.empty);
        }
        //Check game status if any one of the player's all pieces are out
        if(mPlayer2Occupancy.size() >= 3 || mPlayer1Occupancy.size() >= 3){
            gameStatus = checkGameStatus();
        }
        mImgButtons.get(to).setImageResource(imgRes);

        Log.i(TAG,"Game Status:"+Integer.toString(gameStatus));
        return gameStatus;
    }

    /*
    * Check if all of the player's pieces are aligned
    * in a row or a column
    * */
    private int checkGameStatus(){
        if(mPlayer1Occupancy.containsAll(row1) ||
                mPlayer1Occupancy.containsAll(row2) ||
                mPlayer1Occupancy.containsAll(row3) ||
                mPlayer1Occupancy.containsAll(col1) ||
                mPlayer1Occupancy.containsAll(col2) ||
                mPlayer1Occupancy.containsAll(col3)){
            return PLAYER_1_WINS;
        }
        else if(mPlayer2Occupancy.containsAll(row1) ||
                mPlayer2Occupancy.containsAll(row2) ||
                mPlayer2Occupancy.containsAll(row3) ||
                mPlayer2Occupancy.containsAll(col1) ||
                mPlayer2Occupancy.containsAll(col2) ||
                mPlayer2Occupancy.containsAll(col3)){
            return PLAYER_2_WINS;
        }
        return CONTINUE;
    }

    /*
    * Handler for main thread
    * */
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case PLAYER_1:
                    //Case when player 1 sends message
                    Log.i(TAG, "Inside Main Handle message player 1 case:"+Integer.toString(msg.arg2));
                    int gameStatus = makeMoveAndCheckGameStatus(msg.arg1, PLAYER_1, msg.arg2);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if any player wins, send the game status to both thread handlers
                    if(gameStatus == PLAYER_1_WINS || gameStatus == PLAYER_2_WINS) {
                        t1Handler.sendMessage(t1Handler.obtainMessage(gameStatus));
                        displayGameStatus(gameStatus);
                    }
                    //Else just send to thread 2 that its their turn
                    t2Handler.sendMessage(t2Handler.obtainMessage(gameStatus));
                    break;
                case PLAYER_2:
                    //Case when player 2 sends message
                    Log.i(TAG, "Inside Main Handle message player 2 case");
                    gameStatus = makeMoveAndCheckGameStatus(msg.arg1, PLAYER_2, msg.arg2);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if any player wins, send the game status to both thread handlers
                    if(gameStatus == PLAYER_1_WINS || gameStatus == PLAYER_2_WINS) {
                        t2Handler.sendMessage(t2Handler.obtainMessage(gameStatus));
                        displayGameStatus(gameStatus);
                    }
                    //Else just send to thread 1 that its their turn
                    t1Handler.sendMessage(t1Handler.obtainMessage(gameStatus));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Initialize threads
            t1 = new Thread(new Player1());
            t2 = new Thread(new Player2());

            //Initialize the board design
            mImgButtons.add((ImageButton) findViewById(R.id.button1));
            mImgButtons.add((ImageButton) findViewById(R.id.button2));
            mImgButtons.add((ImageButton) findViewById(R.id.button3));
            mImgButtons.add((ImageButton) findViewById(R.id.button4));
            mImgButtons.add((ImageButton) findViewById(R.id.button5));
            mImgButtons.add((ImageButton) findViewById(R.id.button6));
            mImgButtons.add((ImageButton) findViewById(R.id.button7));
            mImgButtons.add((ImageButton) findViewById(R.id.button8));
            mImgButtons.add((ImageButton) findViewById(R.id.button9));

            //Initialize the rows and columns
            //row[] represents the position of each block in the board along the row
            //col[] represents the position of each block in the board along the column
            row1.add(0);
            row1.add(1);
            row1.add(2);
            row2.add(3);
            row2.add(4);
            row2.add(5);
            row3.add(6);
            row3.add(7);
            row3.add(8);
            col1.add(0);
            col1.add(3);
            col1.add(6);
            col2.add(1);
            col2.add(4);
            col2.add(7);
            col3.add(2);
            col3.add(5);
            col3.add(8);

            //Initialize the textview to display game status
            mTextView = (TextView) findViewById(R.id.gameStatus);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Depending upon the game status, displays game status below the board
     **/
    public void displayGameStatus(int status){
        String gameStatus = (status == PLAYER_1_WINS)? "PLAYER 1 WINS!" : "PLAYER 2 WINS!";
        mTextView.setText(gameStatus);
    }

    /*
    * Onclick handler for start button
    * */
    public void onStartButtonClicked(View v){
        Log.i(TAG, "Inside onclick");

        //If start was clicked for the first time
        if(t1.getState() == Thread.State.NEW && t2.getState() == Thread.State.NEW) {
            t1.start();
            t2.start();
        }
        else{
            //On subsequent start button clicks
            resetData();
            Log.i(TAG, "Creating new thread");
            t1 = new Thread(new Player1());
            t2 = new Thread(new Player2());
            if(!t1.isAlive() && !t2.isAlive()) {
                Log.i(TAG, "Threads are not alive ");
                t1.start();
                t2.start();
            }
        }
    }

    /*
    * Resets all the data structures and clears the message queues of the threads
    * and board is emptied
    * */
    public void resetData(){
        Log.i(TAG, "Inside Reset data");

        //Clears all the messages from main thread
        mHandler.removeCallbacksAndMessages(null);
        if(t1.isAlive() && t2.isAlive()){
            t1Handler.sendMessage(t1Handler.obtainMessage(RESTART));
            t2Handler.sendMessage(t2Handler.obtainMessage(RESTART));
        }
        //Clears all the messages and runnables in the thread handlers
        t1Handler.removeCallbacksAndMessages(null);
        t2Handler.removeCallbacksAndMessages(null);
        t1Handler.getLooper().quit();
        t2Handler.getLooper().quit();
        try {
            Log.i(TAG, "Joining thread 1");
            t1.join();
            Log.i(TAG, "Joining thread 2");
            t2.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        mOccupiedBlocks.clear();
        mPlayer1Occupancy.clear();
        mPlayer2Occupancy.clear();
        //Reset the display board
        for (int i = 0; i < mImgButtons.size(); i++) {
            mImgButtons.get(i).setImageResource(R.drawable.empty);
        }
        mTextView.setText("");

    }

    /*
    * Thread for player1
    * Strategy: Just chooses are random number for the next move
    * */
    public class Player1 implements Runnable{
        //Used to indicate how many pieces are out on display
        int numMenOut = 0;
        //Used to know which piece should be shifted from what position
        int from = -1;
        @Override
        public void run(){
            if(Looper.myLooper() == null)
                Looper.prepare();
            Log.i(TAG, "Inside player1 run");
            //Assigning a handler
            t1Handler = new Handler(){
                public void handleMessage(Message msg){
                    Log.i(TAG,"Inside handleMessage of Player1");
                    //If three pieces are out, then randomly shift a piece
                    from = (numMenOut >= 3)? mPlayer1Occupancy.remove(new Random().nextInt(3)) : -1;
                    switch (msg.what){
                        case CONTINUE:
                            //If the message received was to continue playing
                            Log.i(TAG,"Sending message that player1 turn over");
                            // TODO: 4/8/2017 : Should make move and then send message
                            //Posting a runnable to job queue
                            t1Handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.sendMessage(mHandler.obtainMessage(MainActivity.PLAYER_1,
                                            from, makeMove()));
                                }
                            });
                            break;
                        case PLAYER_1_WINS:
                        case PLAYER_2_WINS:
                        case GAME_OVER:
                        case RESTART:
                            //// TODO: 4/8/2017 :  Should check what to do
                            //Case where we need to end the current game
                            Looper.myLooper().quit();
                            numMenOut = 0;
                            from = -1;
                            t1Handler.removeCallbacksAndMessages(null);
                            return;
                    }

                }
            };
            //Posts a runnable to thread's queue, which inturn sends message to main thread handler
            t1Handler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendMessage(mHandler.obtainMessage(MainActivity.PLAYER_1, -1, makeMove()));
                }
            });
            Looper.loop();
        }

        /*
        * Returns a random number to make the next move
        * */
        public int makeMove(){
            int nextMove = -1;
            Random random = new Random();
            //Generate new number until one of the unoccupied
            //number is generated
            do {
                nextMove = random.nextInt(9);
            }while (mOccupiedBlocks.contains(nextMove));
            //Add the next move as occupied block
            mOccupiedBlocks.add(nextMove);
            //Add the next move as player 1 occupied block
            mPlayer1Occupancy.add(nextMove);
            if(numMenOut < 3){
                numMenOut++;
            }
            else{
                //Remove the entry from occupied blocks list from which
                //a piece will be shifted
                mOccupiedBlocks.remove(new Integer(from));
            }
            return nextMove;
        }
    }


    /*
    * Thread for player2
    * Strategy: Two cases
    *   1. When no pieces are out: Choose a random number to start with
    *   2. When there is atleast one piece out: Choose the last entry from
    *   mPlayer2Occupancy and place the new piece in the next free block
    *   available
    * Choosing the piece to be shifted is done by choosing randomly a piece
    * from mPlayer2Occupancy
    * */
    public class Player2 implements Runnable{
        //Used to indicate how many pieces are out on display
        int numMenOut = 0;
        //Used to know which piece should be shifted from what position
        int from = -1;
        @Override
        public void run(){
            if(Looper.myLooper() == null)
                Looper.prepare();
            Log.i(TAG, "Inside player2 run");
            //Assigning a handler
            t2Handler = new Handler(){
                public void handleMessage(Message msg){
                    Log.i(TAG,"Inside handleMessage of Player2");
                    //If three pieces are out, then randomly shift a piece
                    from = (numMenOut >= 3)? mPlayer2Occupancy.remove(new Random().nextInt(3)) : -1;
                    switch (msg.what) {
                        case CONTINUE:
                            //If the message received was to continue playing
                            Log.i(TAG,"Sending message that player2 turn over");
                            // TODO: 4/8/2017: Should make move and then send message
                            //Posting a runnable to job queue
                            t2Handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.sendMessage(mHandler.obtainMessage(MainActivity.PLAYER_2,
                                            from, makeMove()));
                                }
                            });
                            break;
                        case PLAYER_1_WINS:
                        case PLAYER_2_WINS:
                        case GAME_OVER:
                        case RESTART:
                            //// TODO: 4/8/2017 : Should check what to do
                            //Case where we need to end the current game
                            Looper.myLooper().quit();
                            numMenOut = 0;
                            from = -1;
                            t2Handler.removeCallbacksAndMessages(null);
                            return;
                    }
                }
            };
            Looper.loop();
        }

        /*
        * Returns a number to make the next move
        * */
        public int makeMove(){
            int nextMove = -1;
            Random random = new Random();
            int i = 1;
            //Generate new number until one of the unoccupied
            //number is generated
            do {
                //Case 2 of the strategy mentioned in the above header
                if(mPlayer2Occupancy.size() != 0)
                    nextMove = (mPlayer2Occupancy.get(mPlayer2Occupancy.size()-1) + i++) % 9;
                else {
                    //Case 1 of the strategy mentioned in the above header
                    nextMove = random.nextInt(9);
                }
            }while (mOccupiedBlocks.contains(nextMove));
            //Add the next move as occupied block
            mOccupiedBlocks.add(nextMove);
            //Add the next move as player 2 occupied block
            mPlayer2Occupancy.add(nextMove);
            if(numMenOut < 3){
                numMenOut++;
            }
            else{
                //Remove the entry from occupied blocks list from which
                //a piece will be shifted
                mOccupiedBlocks.remove(new Integer(from));
            }
            return nextMove;
        }
    }
}
