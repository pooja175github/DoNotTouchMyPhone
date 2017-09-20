package pragma.com.donottouchmyphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.MessageDigest;

public class Enter_pin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button Button_enter_pin_save;
    String PIN_NUMBER = "PinNumber", pin,item,SECURITY_QUESTION="Question",ans,SECURITY_ANSWER="Answer";
    SharedPreferences sp_pin,sp_ans,sp_ques;
    SharedPreferences.Editor editor_pin,editor_ans,editor_ques;
    EditText EditText_enter_pin,EditText_ans;
Spinner spinner;
   CheckBox CheckBox_ShowPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        //Toolbar for actionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Button_enter_pin_save = (Button) findViewById(R.id.Button_enter_pin_save);
        EditText_enter_pin = (EditText) findViewById(R.id.EditText_enter_pin);
        EditText_ans = (EditText) findViewById(R.id.EditText_ans);


         spinner = (Spinner) findViewById(R.id.spinner);
        CheckBox_ShowPwd= (CheckBox) findViewById(R.id.CheckBox_ShowPwd);
        sp_pin = getSharedPreferences(PIN_NUMBER, Context.MODE_PRIVATE);
        sp_ques=getSharedPreferences(SECURITY_QUESTION,Context.MODE_PRIVATE);
        sp_ans=getSharedPreferences(SECURITY_ANSWER,Context.MODE_PRIVATE);

// Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.security_question, R.layout.simple_list_item_1);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_list_item_2);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        //Toast.makeText(Enter_pin.this, spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
        Log.e("spinner->",""+spinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});

        CheckBox_ShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    EditText_enter_pin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    EditText_enter_pin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        Button_enter_pin_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pin = EditText_enter_pin.getText().toString();
                ans=EditText_ans.getText().toString();
                Log.e("pin",pin);



                if (!(pin.length() == 4) || pin.equals("") ) {
                    showError_pin();
                }else  if(EditText_ans.getText().toString().equals("")){
showError_ans();
                }

                else {
                editor_pin = sp_pin.edit();
                editor_pin.putString("pin", "" + pin);
                    editor_ans=sp_ans.edit();
                    editor_ans.putString("ans",ans);
                    //Toast.makeText(getApplication(),""+ans,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplication(),""+sp_ques,Toast.LENGTH_SHORT).show();
                    editor_ques=sp_ques.edit();
                    editor_ques.putString("ques",item);
                    editor_ques.commit();
                    editor_pin.commit();
                    editor_ans.commit();
                Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),""+item,Toast.LENGTH_SHORT).show();
                    Log.e("spinner::::",""+item);
                    Intent i1=new Intent(Enter_pin.this,Active.class);
                    startActivity(i1);
            }
            }
        });
    }
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private void showError_pin() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        EditText_enter_pin.startAnimation(shake);
        EditText_enter_pin.setError("Not a valid PIN");
    }
    private void showError_ans() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        EditText_ans.startAnimation(shake);
        EditText_ans.setError("Mandatory field");
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

