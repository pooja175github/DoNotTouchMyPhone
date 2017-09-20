package pragma.com.donottouchmyphone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DELL on 14-07-2016.
 */
public class MainActivity  extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    TextView title,tv,tv1,tv2;
    LinearLayout layout;

    @Override
    public final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //refer layout file code below
        //get the sensor service
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get the accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //get layout
        layout = (LinearLayout) findViewById(R.id.Linear);
        //get textviews
        title=(TextView)findViewById(R.id.name);
        tv=(TextView)findViewById(R.id.xval);
        tv1=(TextView)findViewById(R.id.yval);
        tv2=(TextView)findViewById(R.id.zval);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Many sensors return 3 values, one for each axis.
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        Log.e("x",""+x);
        Log.e("y",""+y);
        Log.e("z",""+z);
        //display values using TextView
        title.setText(R.string.app_name);
        tv.setText("X axis" +"\t\t"+x);
        tv1.setText("Y axis" + "\t\t" +y);
        tv2.setText("Z axis" +"\t\t" +z);


                if(( event.values[0]< -0.300 || event.values[0]>1.200) || (event.values[1] > -1.200 && event.values[1] > 4.800) && (event.values[2] < 10.200 ))
        //if (event.values[1] > -1.200 && event.values[1] > 4.800)
       // if (event.values[2] < 10.200 )

//
//                (event.values[1] < low_valueY && event.values[1] > high_valueY )  &&
//                        (event.values[2] < low_valueZ && event.values[2] > high_valueZ  ))
        {
            Toast.makeText(getApplicationContext(),"don't touch my phone",Toast.LENGTH_SHORT).show();
        }
        else {
//                    Toast.makeText(getApplicationContext(),"music played",Toast.LENGTH_SHORT).show();
                }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}