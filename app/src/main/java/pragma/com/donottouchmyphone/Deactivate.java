package pragma.com.donottouchmyphone;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class Deactivate extends AppCompatActivity {
   ImageView deactivate;
    String PIN_NUMBER = "PinNumber",new_pin,sec_ans,SECURITY_QUESTION="Question",pin,SECURITY_ANSWER="Answer",NOTIFICATION="notice";
    public static String ques,ans;
    SharedPreferences sp_pin,sp_ans,sp_ques,sp_notice;
    SharedPreferences.Editor editor;
     EditText userInput, editText_sec_ans;
    TextView TextView_sec_qes;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View promptsView;
    CheckBox CheckBox_ShowPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate);
        deactivate = (ImageView) findViewById(R.id.deactivate);

        //Toolbar for actionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        sp_pin = getSharedPreferences(PIN_NUMBER, Context.MODE_PRIVATE);
        sp_ques = getSharedPreferences(SECURITY_QUESTION, Context.MODE_PRIVATE);
        sp_ans = getSharedPreferences(SECURITY_ANSWER, Context.MODE_PRIVATE);

        ans = sp_ans.getString("ans", "abc");
        pin = sp_pin.getString("pin", "abc");
        ques = sp_ques.getString("ques","abc");
        Log.e("question", ques);
        Log.e("PIN:", pin);
        Log.e("answer", ans);


        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Deactivate.this, Pin.class);
                startActivity(i1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(Deactivate.this);
        promptsView = li.inflate(R.layout.prompts, null);
        userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        TextView_sec_qes = (TextView) promptsView.findViewById(R.id.TextView_sec_qes);
        editText_sec_ans = (EditText) promptsView.findViewById(R.id.editText_sec_ans);
        CheckBox_ShowPwd= (CheckBox) promptsView.findViewById(R.id.CheckBox_ShowPwd);
        TextView_sec_qes.setText(ques);
         builder = new AlertDialog.Builder(Deactivate.this);
        builder.setView(promptsView);
        builder.setMessage("Forgot PIN ?");
        userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        TextView_sec_qes = (TextView) promptsView.findViewById(R.id.TextView_sec_qes);
        editText_sec_ans = (EditText) promptsView.findViewById(R.id.editText_sec_ans);
        TextView_sec_qes.setText(ques);
        CheckBox_ShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    userInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    userInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new_pin = userInput.getText().toString();
                sec_ans = editText_sec_ans.getText().toString();
//validation
                if (new_pin.equals("") || !ans.equals(sec_ans) || sp_ans.equals("")) {

                    //showError_pin();
                    Toast.makeText(getApplicationContext(),"Wrong answer entered",Toast.LENGTH_SHORT).show();
                }
                else {
                    editor=sp_pin.edit();
                    editor.putString("pin",new_pin);
//                    editor.putString("ans",ans);
                    editor.commit();
                    Log.e("new_pin",new_pin);
                    Toast.makeText(getApplicationContext(),"PIN value changed",Toast.LENGTH_SHORT).show();
                }
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                Log.e("cancel", "cancel");
            }
        });
        alertDialog = builder.create();
        alertDialog.show();




        return super.onOptionsItemSelected(item);
    }

    private void showError_pin() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        editText_sec_ans.startAnimation(shake);
        editText_sec_ans.setError("Not a valid PIN");
    }

@Override
public void onBackPressed() {
    // Toast.makeText(getApplicationContext(),"Back button press",Toast.LENGTH_SHORT).show();
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Do you want to Exit?");
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(DialogInterface dialog, int which) {
sp_notice=getSharedPreferences(NOTIFICATION,Context.MODE_PRIVATE);
            Log.e("notification in De",""+sp_notice.getString("value","no_value"));
            finishAffinity();
        }


    });

    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
        }
    });

    builder.create();
    builder.show();
}

}
