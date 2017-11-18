package pragma.com.donottouchmyphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Index extends AppCompatActivity {


    String PIN_NUMBER = "PinNumber",ans,SECURITY_QUESTION="Question",pin,SECURITY_ANSWER="Answer",value,notice,NOTIFICATION="notice";
    SharedPreferences sp_pin,sp_ques,sp_ans,sp_notice;
    Intent intent;

    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
intent=getIntent();
        notice=intent.getStringExtra("notification");

sp_pin=getSharedPreferences(PIN_NUMBER, Context.MODE_PRIVATE);
sp_ques=getSharedPreferences(SECURITY_QUESTION, Context.MODE_PRIVATE);
sp_ans=getSharedPreferences(SECURITY_ANSWER, Context.MODE_PRIVATE);
sp_notice=getSharedPreferences(NOTIFICATION, Context.MODE_PRIVATE);
        pin=sp_pin.getString("Enter suitable pin","no_value_found");
        value=sp_notice.getString("value","no_value_found");


        Log.e("Enter suitable pin",""+pin);
        //Log.e("test in index",""+Active.test);
        Log.e("notification",""+value);
//Boolean b=Backgroundservice.v.hasVibrator();







        //SPLASH SCREEN
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    // After 5 seconds redirect to another intent


                    if(pin.equals("no_value_found")) {
                        Log.e("pin", "if");
                        Intent intent1 = new Intent(Index.this, Enter_pin.class);
                        startActivity(intent1);
                    }else if (value.equals("true")){
                        Intent intent1=new Intent(Index.this,Deactivate.class);
                        startActivity(intent1);
                    }

                     else {
                        Log.e("pin","else");
                        Intent intent2=new Intent(Index.this,Active.class);
                        startActivity(intent2);
                    }


                    //Remove activity
                    finish();

                } catch (Exception e) {
e.printStackTrace();
                    Log.e("exception",""+e.toString());
                }
            }
        };

        // start thread
        background.start();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


}
