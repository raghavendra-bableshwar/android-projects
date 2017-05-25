package spring2017.cs478.raghavendra.project2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

//References: http://stackoverflow.com/questions/3922406/using-hashmap-to-map-a-string-and-int

public class MainActivity extends AppCompatActivity {
    ListView listView = null;
    String[] songsList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get all the songs listed in the strings.xml file
        songsList =  getResources().getStringArray(R.array.songList);
        try {
            //Create an adapter to provide data to list view
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.listview_item, songsList);

            listView = (ListView) findViewById(R.id.songList);
            listView.setAdapter(adapter);
        }
        catch(Exception e){
            Log.d(this.toString(), e.getStackTrace().toString());
        }

    }

    /*
    * Inflate the Menu on the title bar
    * */
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    /*
    * Method to create playlist
    * */
    public void createPlayList(){
        int childCount = listView.getChildCount();
        ArrayList<Integer> selectedSongs = new ArrayList<Integer>();

        //Store all the indices of songs in listview which are selected
        //by user
        for(int i=0; i < childCount; i++){
            CheckBox cb = (CheckBox) listView.getChildAt(i);
            if(cb.isChecked()){
                selectedSongs.add(i);
            }
        }
        //If user has not selected any songs warn using a toast
        if(selectedSongs.isEmpty()){
            Toast.makeText(getApplicationContext(),"Select atleast one song", Toast.LENGTH_LONG).show();
        }
        else{
            //Create an implicit intent to play the song
            Intent intent = new Intent(getApplicationContext(),GridViewActivity.class);
            intent.putIntegerArrayListExtra("SELECTED_SONGS", selectedSongs);
            startActivity(intent);
        }
    }

    /*
    * De-select if any song selected
    * */
    public void clearSelection(){
        int childCount = listView.getChildCount();
        for(int i=0; i < childCount; i++){
            CheckBox cb = (CheckBox) listView.getChildAt(i);
            cb.setChecked(false);
        }
    }

    /*
    * Method to invert song selection
    * */
    public void invertSelection(){
        int childCount = listView.getChildCount();
        for(int i=0; i < childCount; i++){
            CheckBox cb = (CheckBox) listView.getChildAt(i);
            if(cb.isChecked())
                cb.setChecked(false);
            else
                cb.setChecked(true);
        }
    }

    /*
    * Method to select all songs
    * */
    public void selectAll(){
        int childCount = listView.getChildCount();
        for(int i=0; i < childCount; i++){
            CheckBox cb = (CheckBox) listView.getChildAt(i);
            cb.setChecked(true);
        }
    }

    /*
    * onClick method for button on Main activity
    * */
    public void onCreatePlayListClicked(View view){
        createPlayList();
    }

    /*
    * Method to handle the menu item selection
    * */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //Handle create playlist operation
            case R.id.createPlaylist:
                createPlayList();
                break;
            //Handle clear selection operation
            case R.id.clearSelections:
                clearSelection();
                break;
            //Handle invert selection operation
            case R.id.invertSelections:
                invertSelection();
                break;
            //Handle selectall operation
            case R.id.selectAll:
                selectAll();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
