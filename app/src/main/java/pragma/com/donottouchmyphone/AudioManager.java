package pragma.com.donottouchmyphone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AudioManager extends AppCompatActivity {



    MediaRecorder recorder;
    File audiofile = null;
    static final String TAG = "MediaRecording";
    Button startButton,stopButton;
    String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_manager);
        startButton = (Button) findViewById(R.id.button1);
        stopButton = (Button) findViewById(R.id.button2);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
       state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void startRecording() throws IOException {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
isExternalStorageWritable();
        if(state.equals(""+true)){
        //Creating file
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".3gp", dir);
        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return;
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
        }
        else {
            Toast.makeText(getApplication(),"no external storage",Toast.LENGTH_SHORT).show();
        }
    }

    public void stopRecording() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        isExternalStorageWritable();
        if(state.equals(""+true)) {
            //stopping recorder
            recorder.stop();
            recorder.release();
            //after stopping the recorder, create the sound file and add it to media library.
            addRecordingToMediaLibrary();
        }else {
            Toast.makeText(getApplication(),"no external storage",Toast.LENGTH_SHORT).show();
        }
    }

    protected void addRecordingToMediaLibrary() {
        //creating content values of size 4
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        //sending broadcast message to scan the media file so that it can be available
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }



}
