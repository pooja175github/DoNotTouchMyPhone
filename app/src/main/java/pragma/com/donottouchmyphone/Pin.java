package pragma.com.donottouchmyphone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;


public class Pin extends AppCompatActivity {
    EditText EditText_pin;
    Button Button_pin_ok;
    String PIN_NUMBER = "PinNumber", key,PIN_VALUE_EDITTEXT="PinValue",NOTIFICATION="notice";
    public static String value="";
    SharedPreferences sp,sp_notice;
    NotificationManager nf;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        EditText_pin = (EditText) findViewById(R.id.EditText_pin);
        Button_pin_ok = (Button) findViewById(R.id.Button_pin_ok);
        sp = getSharedPreferences(PIN_NUMBER, Context.MODE_PRIVATE); // sharedpreference
sp_notice=getSharedPreferences(NOTIFICATION,Context.MODE_PRIVATE);

        Button_pin_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ns = Context.NOTIFICATION_SERVICE;
                nf = (NotificationManager) getApplicationContext().getSystemService(ns);
                value = EditText_pin.getText().toString();

                key = sp.getString("pin", "default_value");
                Log.e("newpin", key);
                Log.e("entered pin", value);
                if (value.equals(key)) {
                    // stop the service.

                    Intent intent = new Intent(Pin.this, Backgroundservice.class);
                    try {
                        Backgroundservice.v.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("vibration","wait");
                    }

                    nf.cancel(9999);
                   //put false value in shreprence
                            editor=sp_notice.edit();
                    editor.putString("value","false");
                    editor.commit();
                    Backgroundservice.mediaPlayer.stop();
                    stopService(intent);

                    //Backgroundservice.v.cancel();
                   AppExit();
                    //Toast.makeText(getApplication(),"vibration:"+Backgroundservice.v.hasVibrator(),Toast.LENGTH_SHORT).show();
                } else {
                    EditText_pin.setText("");
                    showError();
                }


            }
        });


    }
    public void AppExit()
    {
        finishAffinity();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
        startActivity(intent);


    }


    @Override
    protected void onPause() {
        Backgroundservice.v.cancel();
        super.onPause();
    }

    private void showError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        EditText_pin.startAnimation(shake);
        EditText_pin.setError("Not a valid PIN");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();}

}

