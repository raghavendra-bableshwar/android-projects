package spring2017.cs478.raghavendra.a3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Raghavendra on 3/20/2017.
 */

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("NBA")) {
            //open NBA activity
            Intent nbaIntent = new Intent(context.getApplicationContext(),NBAActivity.class);
            nbaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(nbaIntent);
        } else if (intent.getAction().equalsIgnoreCase("MLB")) {
            //open MLB activity
            Intent mlbIntent = new Intent(context.getApplicationContext(),MLBActivity.class);
            mlbIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mlbIntent);
        }
    }


}
