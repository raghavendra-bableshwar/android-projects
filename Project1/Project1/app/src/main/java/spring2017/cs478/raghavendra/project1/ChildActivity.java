package spring2017.cs478.raghavendra.project1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
    }

    public void okButtonListener(View view){
        EditText editText = (EditText) findViewById(R.id.editText);

        // regex1 is a regular expression which exactly matches the number of form
        // xxx-yyyy
        final String regex1 = "\\d\\d\\d\\-\\d\\d\\d\\d";
        // regex2 is a regular expression which exactly matches the number of form
        // (xxx)yyy-zzzz or (xxx) yyy-zzzz
        final String regex2 = "\\(\\d\\d\\d\\)\\ ?\\d\\d\\d\\-\\d\\d\\d\\d";

        // Following is the explanation of logic to match the phone numbers:
        // Lets say we have a text in the textbox something like, Raghavendra111-1234Bableshwar
        // Here regex1 matches the number in the text and hence the number is displayed
        // on dialer. A tricky example may be when the text have two phone numbers.
        // For ex: Raghavendra(111)111-1111Bableshwar111-1111. In this case, both regex1
        // as well as regex2 will match, but the start index of the number matched by regex2
        // will be less than the start index of the number matched by regex1 in the text.
        // So, the number matched by regex2 will be displayed on the dialer

        String text = editText.getText().toString();

        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);

        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);

        //set the start indices of both regexes to end of text
        int start1 = text.length() + 1;
        int start2 = text.length() + 1;

        boolean numFound = false;
        Matcher matcher = null;

        //Get the start index if regex1 matches
        if(matcher1.find()){
            start1 = matcher1.start();
            numFound = true;
        }
        //Get the start index if regex2 matches
        if(matcher2.find()){
            start2 = matcher2.start();
            numFound = true;
        }

        //Get the first number which is matched
        if(start1 < start2){
            matcher = matcher1;
        }
        else{
            matcher = matcher2;
        }

        //Display the number if matched
        if(numFound){
            //Toast.makeText(getApplicationContext(), matcher.group(0), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+ matcher.group(0)));
            startActivity(intent);
            setResult(RESULT_OK);
            finish();
        }
    }
}
