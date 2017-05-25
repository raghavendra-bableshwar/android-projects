package spring2017.cs478.raghavendra.project2;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Raghavendra on 2/19/2017.
 * Reference: https://developer.android.com/guide/topics/ui/layout/gridview.html
 *
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mSelectedSongs = null;
    private TypedArray mCoverImgIds = null;

    //Constructor
    public ImageAdapter(Context c,
                        ArrayList<Integer> selectedSongs,
                        TypedArray coverImgIds) {
        mContext = c;
        mSelectedSongs = selectedSongs;
        mCoverImgIds = coverImgIds;
    }

    public int getCount() {
        return mSelectedSongs.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // So the logic for displaying thumbnails goes like this:
        // From the GridViewActivity a list indices of selectedSongs (which in
        // turn got from mainactivity) along with a
        // list thumbnail resources will be passed to the constructor. Note that
        // ordering in thumbnails list is same as the order of songs appearing in the listview.
        // While setting the thumbnail, I'll get the index of selected song, and
        // get the resource at the selected song index from list of thumbnails to
        // set the image
        imageView.setImageResource(mCoverImgIds.getResourceId(mSelectedSongs.get(position),-1));

        return imageView;
    }

}
