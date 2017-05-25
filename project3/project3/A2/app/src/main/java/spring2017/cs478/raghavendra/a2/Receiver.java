package spring2017.cs478.raghavendra.a2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Raghavendra on 3/20/2017.
 */

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Receiver A2: Received intent with message "+intent.getAction(),Toast.LENGTH_LONG).show();
    }
}
