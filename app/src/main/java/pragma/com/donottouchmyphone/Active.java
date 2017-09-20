package pragma.com.donottouchmyphone;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.security.MessageDigest;


public class Active extends AppCompatActivity {

    LinearLayout main_linearlayout;
    ImageView activate;
   public static NotificationCompat.Builder nBuilder;
    public static PendingIntent test;
   SharedPreferences sp_notification;
    String NOTIFICATION="notice";
    SharedPreferences.Editor editor;
    private AdView mAdView;
    InterstitialAd iad ;
    Boolean isInternetPresent;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);


        //Add-mob
        mAdView = (AdView) findViewById(R.id.ad_view);
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        iad = new InterstitialAd(this);
        iad.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        iad.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

                if (!isInternetPresent) {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(Active.this, "No Internet Connection",
                            "You don't have internet connection.", false);

                }

            }
        });

        if(getString(R.string.adtype).equals("TEST")) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(deviceId)
                    .build();
            mAdView.loadAd(adRequest);
        }
        else if(getString(R.string.adtype).equals("ON"))
        {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
        }


        //Toolbar for actionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        activate = (ImageView) findViewById(R.id.activate);
        main_linearlayout = (LinearLayout) findViewById(R.id.main_linearlayout);
        activate.setVisibility(View.VISIBLE);
        sp_notification= getSharedPreferences(NOTIFICATION,Context.MODE_PRIVATE);

        Log.e("notification in active",""+sp_notification.getString("value","no_value"));
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_linearlayout.setBackgroundColor(Color.parseColor("#e53935"));

                if (!isMyServiceRunning(Backgroundservice.class)) {
                    startService(new Intent(Active.this, Backgroundservice.class));
                }
               else if (iad.isLoaded()) {
                    iad.show();
                }
                //Notification
                nBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(Active.this)
                                .setSmallIcon(R.drawable.ic_action_tick)
                                .setContentTitle("Do not touch my phone")
                                .setContentText("Your application is running...");

                Notification notification = nBuilder.build();
                notification.flags=Notification.FLAG_NO_CLEAR| Notification.FLAG_ONGOING_EVENT;

                notification.flags = Notification.FLAG_AUTO_CANCEL;
                Intent myIntent = new Intent(Active.this, Deactivate.class);
//                i=myIntent.putExtra("notification","notification");

                editor=sp_notification.edit();
                editor.putString("value","true");
                editor.commit();

                PendingIntent intent2 = PendingIntent.getActivity(Active.this, 1,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                nBuilder.setContentIntent(intent2);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                mNotificationManager.notify(9999, nBuilder.build());

                Intent i1 = new Intent(Active.this, Deactivate.class);
                //isNotificationVisible();

                startActivity(i1);


            }
        });


    }
    private void requestNewInterstitial() {
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();

        if(getString(R.string.adtype).equals("TEST")) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(deviceId)
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            iad.loadAd(adRequest);
        }
        else if(getString(R.string.adtype).equals("ON"))
        {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            iad.loadAd(adRequest);

        }
    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(context);

        alertDialogbuilder.setTitle(title);
        alertDialogbuilder.setMessage(message);
        alertDialogbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        alertDialogbuilder.setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog obj = alertDialogbuilder.create();
        obj.show();

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(android.R.drawable.ic_delete);


    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

}
