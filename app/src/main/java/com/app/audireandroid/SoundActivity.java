package com.app.audireandroid;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import static com.app.audireandroid.R.id.textView;

/**
 * Created by Edercmf on 12/10/2017.
 */

public class SoundActivity extends AppCompatActivity {

    public static Context context;

    private float x = 0;
    private float y = 0;

    public static MediaPlayer mp;

    public static Uri datos;
    public static int timeMp;

    public static ProgressBar progressBar5;

    static TextToSpeech tts;
    public static String textoAReproducir = "";

    public static boolean speechDisp = false;
    public static boolean reproduciendoDescripcion = false;

    public static boolean descargaAudioFinalizada = false;
    public static boolean reproduciendoAudio = false;
    public static boolean ttsFinalizado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        context = this;
        mp = new MediaPlayer();

        progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
        progressBar5.setVisibility(View.GONE);



        EnviarImg.Inicializar(context);
        //DescargarAudio.DescargarAudio1(context);

        tts=new TextToSpeech(SoundActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        if(textoAReproducir.equals("")){
                            speechDisp = true;
                        }else{
                            if(!reproduciendoDescripcion){
                                speechDisp = true;
                                ReproducirDescripcion();
                            }
                        }
                    }

                    if (Build.VERSION.SDK_INT >= 15)
                    {
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener()
                        {
                            @Override
                            public void onDone(String utteranceId)
                            {
                                runOnUiThread(new Runnable() {

                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Finaliza",Toast.LENGTH_LONG).show();
                                        ttsFinalizado = true;
                                        if(descargaAudioFinalizada) {
                                            Reproducir();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(String utteranceId)
                            {

                            }

                            @Override
                            public void onStart(String utteranceId)
                            {

                            }
                        });
                    }
                    else
                    {
                        tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener()
                        {
                            @Override
                            public void onUtteranceCompleted(String utteranceId)
                            {
                                Toast.makeText(getApplicationContext(),"Finaliza",Toast.LENGTH_LONG).show();
                                ttsFinalizado = true;
                                if(descargaAudioFinalizada) {
                                    Reproducir();
                                }
                            }
                        });
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }

        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                x = event.getX();
                y = event.getY();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                //Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                //Si se mueve hcia abajo
                if((event.getY()-y)>250){
                    if(mp.isPlaying()){
                        timeMp = mp.getCurrentPosition();
                        mp.pause();
                    }else{
                        mp.seekTo(timeMp);
                        mp.start();
                    }
                }else
                    //Si se mueve hacia la izquierda
                    if(x-event.getX()>250){
                        //Toast.makeText(context,"Tomar Nueva Fotografia",Toast.LENGTH_LONG).show();
                        finish();
                    }

                //Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                //Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                //Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                //        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }


    }



    public static void Reproducir(){
        if(ttsFinalizado && !reproduciendoAudio) {
            reproduciendoAudio = true;
            datos = Uri.parse("/sdcard/Audire/" + Datos.fileName + ".mp3");
            mp = MediaPlayer.create(context, datos);
            mp.start();
        }
    }

    public static void ReproducirDescripcion(){
        if(speechDisp) {
            Toast.makeText(context,"Reproduciendo",Toast.LENGTH_LONG).show();
            reproduciendoDescripcion = true;
            tts.setLanguage(Locale.US);
            tts.setSpeechRate(0.65f);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"id1234");
            tts.speak(textoAReproducir, TextToSpeech.QUEUE_FLUSH, params);
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mp.isPlaying()){
            mp.stop();
        }

        speechDisp = false;
        textoAReproducir = "";
        reproduciendoDescripcion = false;
        descargaAudioFinalizada = false;
        reproduciendoAudio = false;
        ttsFinalizado = false;

        EnviarImg.Cancelar();
        DescargarAudio.Cancelar();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp.isPlaying()){
            timeMp = mp.getCurrentPosition();
            mp.pause();
        }

        if(tts != null){

            tts.stop();
            tts.shutdown();
        }
    }


}
