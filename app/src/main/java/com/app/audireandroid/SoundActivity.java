package com.app.audireandroid;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        context = this;
        mp = new MediaPlayer();



        EnviarImg.Inicializar(context);
        DescargarAudio.DescargarAudio1(context);

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
                        Toast.makeText(context,"Tomar Nueva Fotografia",Toast.LENGTH_LONG).show();
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
        datos = Uri.parse("/sdcard/Audire/"+Datos.fileName+".mp3");
        mp = MediaPlayer.create(context, datos);
        mp.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mp.isPlaying()){
            mp.stop();
        }

        EnviarImg.Cancelar();
        DescargarAudio.Cancelar();

    }


}
