package spring2017.cs478.raghavendra.a1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //public final String permString = "edu.uic.cs478.project3";
    public final int NBA_PERMISSION_REQUEST = 0;
    public final int MLB_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    * First button onClick handler
    * */
    public void onFirstButtonClick(View view){
        //Check if the activity has permission to send broadcast
        int checkSelfPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),Manifest.permission.project3);
        if(checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            //Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.project3},NBA_PERMISSION_REQUEST);

        }
        else{
            //Send ordered broadcast with message NBA
            final String intentString = "NBA";
            final Intent intent = new Intent(intentString);
            //Flag to make the intent reach the stopped activities too
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendOrderedBroadcast(intent, null);
        }

    }

    /*
    * Second button onClick handler
    * */
    public void onSecondButtonClick(View view){
        //Check if the activity has permission to send broadcast
        int checkSelfPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),Manifest.permission.project3);
        if(checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            //Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.project3},MLB_PERMISSION_REQUEST);

        }
        else{
            //Send ordered broadcast with message MLB
            final String intentString = "MLB";
            final Intent intent = new Intent(intentString);
            //Flag to make the intent reach the stopped activities too
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendOrderedBroadcast(intent, null);
        }

    }

    /*
    * Callback to handle permission result
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantReults){
        switch (requestCode){
            case NBA_PERMISSION_REQUEST: {
                if(grantReults.length > 0 &&
                        grantReults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted send boradcast with NBA message
                    final String intentString = "NBA";
                    final Intent intent = new Intent(intentString);
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendOrderedBroadcast(intent, null);

                }
                else{
                    //Don't send boradcast if permission is not granted
                    Toast.makeText(getApplicationContext(), "Could not send NBA broadcast!", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case MLB_PERMISSION_REQUEST: {
                if(grantReults.length > 0 &&
                        grantReults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If permission granted send boradcast with MLB message
                    final String intentString = "MLB";
                    final Intent intent = new Intent(intentString);
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendOrderedBroadcast(intent, null);
                }
                else{
                    //Don't send boradcast if permission is not granted
                    Toast.makeText(getApplicationContext(), "Could not send MLB broadcast!", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }
}
