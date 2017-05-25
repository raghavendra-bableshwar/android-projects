package spring2017.cs478.raghavendra.a3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Raghavendra on 3/24/2017.
 */

public class NBAWebsiteFragment extends android.app.Fragment {

    private static final String TAG = "NBAWebsiteFragment";
    private WebView mWebsiteView = null;
    private int mCurrIdx = -1;
    private int mTeamListLen;

    ////
    // Show the team website at position newIndex
    void showQuoteAtIndex(int newIndex) {
        if (newIndex < 0 || newIndex >= mTeamListLen)
            return;
        mCurrIdx = newIndex;
        mWebsiteView.getSettings().setJavaScriptEnabled(true);
        mWebsiteView.loadUrl(NBAActivity.mNbaTeamsUrls[mCurrIdx]);
        Log.i(TAG,NBAActivity.mNbaTeamsUrls[mCurrIdx]);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    // Called to create the content view for this Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreateView()");

        return inflater.inflate(R.layout.team_website_view,
                container, false);
    }
    ////

    // Set up some information about the Webview
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        mWebsiteView = (WebView) getActivity().findViewById(R.id.team_website_view);
        mTeamListLen = NBAActivity.mNbaTeamsUrls.length;
    }

    int getShownIndex() {
        return mCurrIdx;
    }

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }
}
