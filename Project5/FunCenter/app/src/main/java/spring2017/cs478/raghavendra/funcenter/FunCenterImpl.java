package spring2017.cs478.raghavendra.funcenter;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Raghavendra on 4/21/2017.
 */

public class FunCenterImpl extends Service {

    final static int[] mSongList = {R.raw.clip5, R.raw.clip2, R.raw.clip3, R.raw.clip4, R.raw.clip1};
    final static int[] mPicsList = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5};

    private static MediaPlayer mMediaPlayer = new MediaPlayer();
    public static final String TAG = "FunCenterImpl";

    private boolean mIsStarted = false;
    private int mCurrentSong = -1;

    private final IServiceInterface.Stub mBinder = new IServiceInterface.Stub(){

        /**
         * Play the media
         * */
        @Override
        public synchronized void playMedia(int num) throws RemoteException {
            try{
                AssetFileDescriptor descriptor = getApplicationContext().getResources().openRawResourceFd(mSongList[num]);
                mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                descriptor.close();
                mIsStarted = true;
                mCurrentSong = num;
                Log.i(TAG, "Playing:" + Integer.toString(num));
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        /*
        * Stop the media
        * */
        @Override
        public synchronized void stop(int num) throws RemoteException {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mIsStarted = false;
            mCurrentSong = -1;
            Log.i(TAG, "Stopping:" + Integer.toString(num));
        }

        /*
        * Resume the media
        * */
        @Override
        public synchronized void resume(int num) throws RemoteException {
            Log.i(TAG, "Resuming:"+Integer.toString(num));
            //When same button is pressed toggle play/pause
            if (mIsStarted) {
                if (mCurrentSong == num) {
                    if (!mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();
                    } else {
                        mMediaPlayer.pause();
                    }
                } else {

                    mMediaPlayer.reset();
                    playMedia(num);
                }
            } else {
                playMedia(num);
            }
        }

        /*
        * Pause the media player
        * */
        @Override
        public synchronized void pause() throws RemoteException {
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
            else{
                mMediaPlayer.start();
            }
        }

        /*
        * Send the requested pic to client
        * */
        @Override
        public synchronized Bitmap sendPic(int num) throws RemoteException {
            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), mPicsList[num]);
            return bitmap;
        }

    };

    /*
    * Method called when a client binds to the service
    * */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /*
    * Method to stop the service once all clients are unbound
    * */
    @Override
    public boolean onUnbind(Intent intent){

        Log.i(TAG,"Inside onUnbind");
        stopService(new Intent(this, this.getClass()));
        stopSelf();
        mMediaPlayer.stop();
        return false;
    }

    @Override
    public void onTaskRemoved(Intent intent){
        stopService(new Intent(this, this.getClass()));
        stopSelf();
        Log.i(TAG,"Inside onTaskRemoved");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent,flags,startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
