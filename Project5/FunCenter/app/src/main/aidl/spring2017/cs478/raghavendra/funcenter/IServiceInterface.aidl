// IServiceInterface.aidl
package spring2017.cs478.raghavendra.funcenter;

// Declare any non-default types here with import statements

interface IServiceInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void playMedia(int num);
     void stop(int num);
     void resume(int num);
     void pause();
     Bitmap sendPic(int num);

//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
}
