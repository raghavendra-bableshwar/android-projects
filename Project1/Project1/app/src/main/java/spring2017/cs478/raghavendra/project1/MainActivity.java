package spring2017.cs478.raghavendra.project1;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final int DISPLAY_PHONE_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void topButtonListener(View view){
        Intent intent = new Intent(this, ChildActivity.class);
        startActivityForResult(intent, DISPLAY_PHONE_NUM);
    }

    /*
    * This method gets the result of the child activity after it exits
    *
    * */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // Check which request we're responding to
        if (requestCode == DISPLAY_PHONE_NUM) {
            // If there was a phone number and the child activity opened a dialer
            // then the textview is updated with text SUCCESS:Phone number found!
            if (resultCode == RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "SUCCESS:Phone number found!", Toast.LENGTH_LONG).show();
                TextView textView = (TextView)findViewById(R.id.childReturnValDisplay);
                textView.setText("SUCCESS:Phone number found!");
            }
            else{
                // If there wasn't a phone number and the child activity didn't open a dialer
                // then the textview is updated with text FAIL:Phone number not found!
                TextView textView = (TextView)findViewById(R.id.childReturnValDisplay);
                textView.setText("FAIL:Phone number not found!");
            }
        }

    }

    public void bottomButtonListener(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/index.html"));
        startActivity(intent);
    }
}
