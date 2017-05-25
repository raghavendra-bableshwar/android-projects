package spring2017.cs478.raghavendra.a3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

//Reference: Fragments section of Adam Porter's Coursera MOOC and example taught by
//professor in class

public class MLBActivity extends AppCompatActivity implements MLBTeamsFragment.ListSelectionListener {

    //Permission to be acquired
    private String mPermissionString = "edu.uic.cs478.project3";
    //Constant used in acquiring permission
    private final int MLB_PERMISSION_ID = 1;

    //Holds team names
    public static String[] mMLBTeams;
    //Holds team website URLs
    public static String[] mMLBTeamsUrls;

    //Teamlist fragment identifier
    public final String TEAMFRAGMENTTAG = "mlbteamfragment";
    //Team website fragment identifier
    public final String TEAMWEBSITETAG = "mlbteamwebsitetag";

    //Declare some class members used in future
    private android.app.FragmentManager mFragmentManager;
    private FrameLayout mTeamsFrameLayout, mTeamWebsiteFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    private final MLBWebsiteFragment mMLBWebsiteFragment = new MLBWebsiteFragment();

    public Configuration mConfig;
    private static final String TAG = "MLBActivity";
    android.app.FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlb);

        //Check permission before displaying teams
        int checkPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                "edu.uic.cs478.project3");
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            //request permission
            ActivityCompat.requestPermissions(this, new String[]{"edu.uic.cs478.project3"},
                    MLB_PERMISSION_ID);
        }
        else {
            //If permission is already granted display teams
            permissionGrantedActions();
        }
    }

    /*
    * Defines actions to be taken once permissions are granted
    * */
    void permissionGrantedActions(){
        //Get required information
        mMLBTeams = getResources().getStringArray(R.array.mlbteams);
        mMLBTeamsUrls = getResources().getStringArray(R.array.mlbteamswebsites);
        mConfig = getResources().getConfiguration();

        // Get references to the TeamFragment and to the TeamWebsiteFragment
        mTeamsFrameLayout = (FrameLayout) findViewById(R.id.mlb_team_fragment_container);
        mTeamWebsiteFrameLayout = (FrameLayout) findViewById(R.id.mlb_websites_fragment_container);

        // Get a reference to the FragmentManager
        mFragmentManager = getFragmentManager();

        mFragmentManager.addOnBackStackChangedListener(new android.app.FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                displayTeams();
            }
        });

        //The following logic is to retain the fragment instance even during configuration change
        //If the team fragment and website fragment are already added, then
        //a reference is got to the already added fragments and are displayed
        //Else a new fragment is created and is added to display
        if(mFragmentManager.findFragmentByTag(TEAMFRAGMENTTAG) != null){
            mFragmentTransaction = mFragmentManager
                    .beginTransaction();
            if(mFragmentManager.findFragmentByTag(TEAMWEBSITETAG) != null){
                mFragmentTransaction.replace(R.id.mlb_websites_fragment_container,
                        mFragmentManager.findFragmentByTag(TEAMWEBSITETAG));
                Log.i(TAG,"Team website Found");
            }
            else {
                mFragmentTransaction.replace(R.id.mlb_team_fragment_container,
                        mFragmentManager.findFragmentByTag(TEAMFRAGMENTTAG));
            }
            mFragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        else{
            // Start a new FragmentTransaction
            mFragmentTransaction = mFragmentManager
                    .beginTransaction();
            mFragmentTransaction.replace(R.id.mlb_team_fragment_container,
                    new MLBTeamsFragment(), TEAMFRAGMENTTAG);
            // Commit the FragmentTransaction
            mFragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }


    }

    /*
    * Inflate the Menu on the title bar
    * */
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    /*
    * Method to handle the menu item selection
    * */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.NBA:
                Intent intent = new Intent(this, NBAActivity.class);
                //The flags below ensure to send the intent to already
                //created activity (if created) else, start a new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.MLB:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * Display based on screen configuration
    * */
    private void displayTeams(){
        //The below if condition checks if the website fragment is added
        //If not, make the team fragment to occupy the whole screen
        //See else part for other description
        if(!mMLBWebsiteFragment.isAdded()){
            mTeamsFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mTeamWebsiteFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT));
            Log.i(TAG,"displayTeams(): website not added ");
        }
        else{
            //Check the orientation, if portrait display website fragment full screen
            //if landscape, display website fragment 2/3 of screen and team fragment
            //1/3 of the screen
            Log.i(TAG,"displayTeams(): website added ");
            if(getResources().getConfiguration().orientation == mConfig.ORIENTATION_PORTRAIT ){
                mTeamsFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        0, MATCH_PARENT));
                mTeamWebsiteFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
            }
            else {
                mTeamsFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));
                mTeamWebsiteFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
        }
    }

    /*
    * Callback to handle permission request
    * */
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantReults){
        switch (requestCode){
            case MLB_PERMISSION_ID:{
                if(grantReults.length > 0 && grantReults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGrantedActions();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Cannot display teams!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
    * Callback to handle Listitem selection
    * */
    @Override
    public void onListSelection(int index) {
        // If the Teamwebsiteview has not been added, add it now
        if (!mMLBWebsiteFragment.isAdded()) {

            // Start a new FragmentTransaction
            android.app.FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the Teamwebsiteview to the layout
            fragmentTransaction.replace(R.id.mlb_websites_fragment_container,
                    mMLBWebsiteFragment, TEAMWEBSITETAG);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(TEAMWEBSITETAG);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        if (mMLBWebsiteFragment.getShownIndex() != index) {

            // Tell the Teamwebsiteview to show the website at position index
            mMLBWebsiteFragment.showQuoteAtIndex(index);

        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    //if user clicks back, pop the top most Fragment
    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }
    //Reset layout everytime configuration is changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG,"onConfigutation changed");
        super.onConfigurationChanged(newConfig);
        displayTeams();
    }
}
