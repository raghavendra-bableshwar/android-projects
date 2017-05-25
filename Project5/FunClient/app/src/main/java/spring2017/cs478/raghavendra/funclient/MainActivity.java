package spring2017.cs478.raghavendra.funclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Map;

import spring2017.cs478.raghavendra.funcenter.IServiceInterface;


@SuppressWarnings("WrongConstant")
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    //Service connection object
    private ServiceConnection mServiceConnection  = null;
    //Service interface
    private IServiceInterface iServiceInterface;
    //To check if the client is bound to the service
    private boolean mIsBound;
    //To store the previous logs
    private SharedPreferences mPrefs;
    private static final int SHOW_REQUESTS_RESULT_CODE = 0;
    private static int count = 0;
    ArrayList<String> mPrevSessionOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating shared preference object
        mPrefs = getSharedPreferences("userChoices", MODE_APPEND|MODE_PRIVATE);

        //Create service connection
        mServiceConnection = new ServiceConnection() {
            //Implement what to happen when service is connected
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG,"Service connected");
                iServiceInterface = IServiceInterface.Stub.asInterface(service);
                mIsBound = true;
            }

            //Implement what to happen when service is disconnected
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG,"Service disconnected");
                iServiceInterface = null;
                mIsBound = false;
            }
        };

        //If the service is not bound bind it
        if(!mIsBound){
            boolean bindStatus = false;
            //Create intent to bind to the service
            Intent intent = new Intent(IServiceInterface.class.getName());
            //Resolve the info of service
            ResolveInfo resolveInfo = getPackageManager().resolveService(
                    intent, Context.BIND_AUTO_CREATE);
            if(resolveInfo == null){
                Log.i(TAG, "ResolveInfo is null");
            }
            intent.setComponent(new ComponentName(
                    resolveInfo.serviceInfo.packageName,
                    resolveInfo.serviceInfo.name));
            //bind to the service
            bindStatus = bindService(intent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);
            if(bindStatus){
                Log.i(TAG, "Bound to service");
            }
            else{
                Log.i(TAG, "Bind failed");
            }
        }

        mPrevSessionOps = getAllRequestsFromSharedPrefs();

        //start the activity to show the user request list
        Intent showRequestIntent = new Intent(this, showRequests.class);
        showRequestIntent.putStringArrayListExtra("ALL_REQUESTS", getAllRequestsFromSharedPrefs());
        startActivityForResult(showRequestIntent, SHOW_REQUESTS_RESULT_CODE);
    }

    /*
    * Returns the user previous request list from shared preferences
    * */
    public ArrayList<String> getAllRequestsFromSharedPrefs(){
        Map<String, ?> allRequestMap = mPrefs.getAll();
        ArrayList<String> allRequestList = new ArrayList<>();
        for (int i = 0; i < allRequestMap.size(); i++) {
            allRequestList.add(allRequestMap.get(Integer.toString(i+1)).toString());
        }
        return allRequestList;
    }

    /*
    * onStart method to connect to the service on start
    * */
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "Inside onStart");
        //If the service is not bound bind it
        if(!mIsBound){
            boolean bindStatus = false;
            //Create intent to bind to the service
            Intent intent = new Intent(IServiceInterface.class.getName());
            //Resolve the info of service
            ResolveInfo resolveInfo = getPackageManager().resolveService(intent, Context.BIND_AUTO_CREATE);
            if(resolveInfo == null){
                Log.i(TAG, "ResolveInfo is null");
            }
            intent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
            //bind to the service
            bindStatus = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            if(bindStatus){
                Log.i(TAG, "Bound to service");
            }
            else{
                Log.i(TAG, "Bind failed");
            }
        }
    }

    /*
    * Get the result from the other activity
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SHOW_REQUESTS_RESULT_CODE){
            if(resultCode == RESULT_OK)
            {
                //Clear the list and sharedPreference
                Log.i(TAG, "Inside RESULT_OK condition");
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.clear();
                editor.commit();
                mPrevSessionOps.clear();
            }
            if(resultCode == RESULT_CANCELED){
                //Don't do anything
                Log.i(TAG, "Inside RESULT_CANCELED condition");
            }
        }
    }

    /*
    * Connect to the service and play the clip
    * */
    void playClip(int num){
        //Add the request to shared preferences
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(Integer.toString(mPrefs.getAll().size() + 1), "PlayClip: "+ Integer.toString(num+1));
        editor.commit();
        try{
            if(mIsBound){
                //Play the requested song
                iServiceInterface.resume(num);
            }
            else{
                Log.i(TAG, "Started playing");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    * on PLAY1 button clicked
    * */
    public void onPlay1Clicked(View v){
        playClip(0);
    }

    /*
    * on PLAY2 button clicked
    * */
    public void onPlay2Clicked(View v){
        playClip(1);
    }

    /*
    * on PLAY3 button clicked
    * */
    public void onPlay3Clicked(View v){
        playClip(2);
    }

    /*
    * on PLAY4 button clicked
    * */
    public void onPlay4Clicked(View v){
        playClip(3);
    }

    /*
    * on PLAY5 button clicked
    * */
    public void onPlay5Clicked(View v){
        playClip(4);
    }

    /*
    * on PAUSE button clicked
    * */
    public void onPauseClicked(View view){
        try{
            if(mIsBound){
                iServiceInterface.pause();
            }
            else{
                Log.i(TAG, "Not bound to service");
            }
            Button pauseButton = (Button) findViewById(R.id.pause);
            String state = (pauseButton.getText().toString().equalsIgnoreCase("pause") == true) ? "RESUME":"PAUSE";
            pauseButton.setText(state);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    * on STOP button clicked
    * */
    public void onStopClicked(View view){
        try{
            if(mIsBound){
                iServiceInterface.stop(0);
            }
            else{
                Log.i(TAG, "Not bound to service");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    * Calls method is server to retrieve a pic
    * */
    public void requestPic(int num){
        //Save the request into SharedPreferences
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(Integer.toString(mPrefs.getAll().size() + 1), "RequestPic: "+ Integer.toString(num+1));
        editor.commit();
        Bitmap bitmap = null;
        if(mIsBound){
            try {
                //Call sendPic function of the service
                bitmap = iServiceInterface.sendPic(num);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(bitmap != null){
            // Display the bitmap
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
        else{
            Log.i(TAG, "Bitmap is null");
        }
    }

    //on clicking PIC1
    public void onPic1Clicked(View view){
        Log.i(TAG,"Inside onPic1Clicked");
        requestPic(0);
    }

    //on clicking PIC2
    public void onPic2Clicked(View view){
        Log.i(TAG,"Inside onPic2Clicked");
        requestPic(1);
    }

    //on clicking PIC3
    public void onPic3Clicked(View view){
        Log.i(TAG,"Inside onPic3Clicked");
        requestPic(2);
    }

    //on clicking PIC4
    public void onPic4Clicked(View view){
        Log.i(TAG,"Inside onPic4Clicked");
        requestPic(3);
    }

    //on clicking PIC5
    public void onPic5Clicked(View view){
        Log.i(TAG,"Inside onPic5Clicked");
        requestPic(4);
    }

    /*
    * When the activity goes to stopped state
    * */
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "Inside onStop");
    }

    /*
    * When the activity is in resumed state
    * */
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "Inside onResume");
        if(!mIsBound){
            boolean bindStatus = false;
            Intent intent = new Intent(IServiceInterface.class.getName());
            ResolveInfo resolveInfo = getPackageManager().resolveService(intent, Context.BIND_AUTO_CREATE);
            if(resolveInfo == null){
                Log.i(TAG, "ResolveInfo is null");
            }
            intent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
            bindStatus = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            if(bindStatus){
                Log.i(TAG, "Bound to service");
            }
            else{
                Log.i(TAG, "Bind failed");
            }
        }
    }

    /*
    * When the activity gets destroyed unbind the service
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Inside onDestroy");
        if(mIsBound) {
            unbindService(mServiceConnection);
            mIsBound = false;
        }
    }

    /*
    * When logs button clicked show the previous requests
    * */
    public void onLogsClicked(View view){
        Intent showRequestIntent = new Intent(this, showRequests.class);
        showRequestIntent.putStringArrayListExtra("ALL_REQUESTS", getAllRequestsFromSharedPrefs());
        startActivityForResult(showRequestIntent, SHOW_REQUESTS_RESULT_CODE);
    }

}
