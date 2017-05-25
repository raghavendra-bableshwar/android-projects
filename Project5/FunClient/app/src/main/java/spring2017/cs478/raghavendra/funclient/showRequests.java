package spring2017.cs478.raghavendra.funclient;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class showRequests extends AppCompatActivity {

    public static final String TAG = "showRequests";
    ArrayList<String> mAllRequestList;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);
        mAllRequestList = getIntent().getStringArrayListExtra("ALL_REQUESTS");
        //Set the array adapter to the list view
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item, mAllRequestList);
        listView = (ListView) findViewById(R.id.reqList);
        listView.setAdapter(adapter);
    }

    /*
    * on OK button clicked
    * */
    public void onOkClicked(View view){
        Log.i(TAG, "Inside onOkClicked");
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /*
    * on CLEAR button clicked
    * */
    public void onClearClicked(View view){
        Log.i(TAG, "Inside onClearClicked");
        setResult(Activity.RESULT_OK);
        finish();
    }
}
