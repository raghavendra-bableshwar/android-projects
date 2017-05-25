package spring2017.cs478.raghavendra.project2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;

//Reference : https://developer.android.com/guide/topics/ui/layout/gridview.html
//Cover images:
//http://www.belgian-beatles-site.com/music/detailpage/1346?sort=title
//https://www.youtube.com/watch?v=vlE6xefXuMc
//https://www.youtube.com/watch?v=LkxSU__Bi3w


public class GridViewActivity extends AppCompatActivity {
    // Members to store the songs information
    String[] mSongsURLList = null;
    ArrayList<Integer> mSelectedSongs = null;
    String[] mSongWikiURLList = null;
    String[] mArtistWikiURLList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        GridView gridView = (GridView)findViewById(R.id.gridview);

        //Get the songsURL list
        mSongsURLList = getResources().getStringArray(R.array.songURLList);
        //Get the songsURL list
        mSelectedSongs = getIntent().getIntegerArrayListExtra("SELECTED_SONGS");
        //Get the songs wiki URL list
        mSongWikiURLList = getResources().getStringArray(R.array.songWikiURLList);
        //Get the artist wiki URL list
        mArtistWikiURLList = getResources().getStringArray(R.array.artistWikiURLList);

        gridView.setAdapter(new ImageAdapter(this,mSelectedSongs,
                getResources().obtainTypedArray(R.array.coverImageIds)));

        registerForContextMenu(gridView);

        // onClickListener for gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSongsURLList[mSelectedSongs.get(i)]));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
            }
        });

    }

    /*
    * Provide actions to what all should happen once the context menu created
    * */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(menu,view,contextMenuInfo);
        menu.setHeaderTitle("Choose an action");
        //Create submenu with item ids
        menu.addSubMenu(view.getId(),0,Menu.NONE, "Play");
        //Create submenu with item ids
        menu.add(view.getId(),1,Menu.NONE,"Song Wiki");
        //Create submenu with item ids
        menu.add(view.getId(),2,Menu.NONE,"Artist Wiki");
    }

     /*So the logic for actions in context menu goes like this:
     From the MainActivity a list indices of selectedSongs
     will be passed to the GridViewActivity via intent. Other lists
     are created to store songsURL, songWikiURL and artistWikiURL.
     Note that ordering in all the list is same as the order of songs
     appearing in the listview. So while performing any action like play
     song or displaying wiki page, I'll get the index of the selected song
     and get the URL present in that position of the lists which stores
     the information.*/

    /*
    * Method called on play context menu selection
    * */
    void onPlaySelected(int gridViewItem){

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mSongsURLList[mSelectedSongs.get(gridViewItem)]));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }

    /*
    * Method called on Song Wiki option selection
    * */
    public void onSongWikiSelected(int gridViewItem){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mSongWikiURLList[mSelectedSongs.get(gridViewItem)]));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }

    /*
    * Method called on Artist Wiki option selection
    * */
    public void onArtistWikiSelected(int gridViewItem){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mArtistWikiURLList[mSelectedSongs.get(gridViewItem)]));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }

    /*
    * Method called on any option selected in the context menu
    * */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case 0: onPlaySelected(info.position);
                break;
            case 1: onSongWikiSelected(info.position);
                break;
            case 2: onArtistWikiSelected(info.position);
                break;
            default:
                return false;
        }
        return true;
    }
}
