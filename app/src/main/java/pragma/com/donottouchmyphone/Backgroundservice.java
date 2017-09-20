package pragma.com.donottouchmyphone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import java.util.Timer;
import java.util.TimerTask;

public class Backgroundservice extends Service implements SensorEventListener {


    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public String AppMode = "";
    TimerTask timerTask2;
    Timer timer = new Timer();
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
  public static MediaPlayer mediaPlayer;
    public static  Vibrator v;

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(Backgroundservice.this, R.raw.alert);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100, 100);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //get the sensor service
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get the accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains("AppStatus")) {
            AppMode = sharedpreferences.getString("AppStatus", "NO");
        }
        timerTask2 = new TimerTask() {

            @Override
            public void run() {
                //Log.e("run()","Backgroundservice");

            }
        };
        timer.scheduleAtFixedRate(timerTask2, 10000, 10000);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

// Many sensors return 3 values, one for each axis.
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        //if ((event.values[0] < -0.300 || event.values[0] > 1.200) || (event.values[1] > -1.200 && event.values[1] > 4.800) && (event.values[2] < 10.200)) {
            if ((event.values[0] < -0.300 || event.values[0] > 1.200) || (event.values[1] > -1.550 && event.values[1] > 4.800) && (event.values[2] < 10.200)) {
            //Toast.makeText(getApplicationContext(),"don't touch my phone",Toast.LENGTH_SHORT).show();
            startVibrate();

            playSound();
        }
    }


    private void playSound() {


        mediaPlayer.setLooping(true);
        mediaPlayer.start();


    }

    //vibration
    public void startVibrate() {

        int dot = 200;      // Length of a Morse Code "dot" in milliseconds
        int dash = 500;     // Length of a Morse Code "dash" in milliseconds
        int short_gap = 200;    // Length of Gap Between dots/dashes
        int medium_gap = 500;   // Length of Gap Between Letters
        int long_gap = 1000;    // Length of Gap Between Words
        long[] pattern = {
                0,  // Start immediately
                dot, short_gap, dot, short_gap, dot,    // s
                medium_gap,
                dash, short_gap, dash, short_gap, dash, // o
                medium_gap,
                dot, short_gap, dot, short_gap, dot,    // s
                long_gap
        };

// Only perform this pattern one time (-1 means "do not repeat")
        v.vibrate(pattern, -1);
    }
//    private Vibrator getVibrator() {
//        if (v != null) {
//            Vibrator vibrator = mLastInputDevice.getVibrator();
//            if (vibrator.hasVibrator()) {
//                return vibrator;
//            }
//        }
//        return (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
//    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Log.e("onAccuracyChanged()","Backgroundservice");
    }

    @Override
    public void onDestroy() {
        //Log.e("onDestroy()","Backgroundservice");
        mSensorManager.unregisterListener(this);
        mediaPlayer.stop();
        v.cancel();
        super.onDestroy();
    }
}