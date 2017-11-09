package com.app.audireandroid;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import static com.app.audireandroid.R.id.textView;

/**
 * Created by Edercmf on 05/10/2017.
 */

public class DescargarAudio {

    public static Context context;
    public static DownloadFile downloadFile;

    //Espera para descargar audio
    public static Handler handler = new Handler();

    static int intentos = 0;

    private static Runnable runnableDescargarAudio = new Runnable() {
        @Override
        public void run() {
            if(intentos<=5){
                intentos++;
                handler.removeCallbacks(runnableDescargarAudio);
                DescargarAudio1();
            }else{
                Toast.makeText(context,"Error, descargando melodia",Toast.LENGTH_LONG).show();
            }
        }
    };

    public static void DescargarDescripcion(Context context1){
        context = context1;
    }

    public static void EsperarTiempo(Context context1){
        context = context1;
        handler.removeCallbacks(runnableDescargarAudio);
        handler.postDelayed(runnableDescargarAudio,10000);
    }

    public static void DescargarAudio1(){
        //Empezamos a descargar a Img
        downloadFile = new DownloadFile();
        downloadFile.execute();
    }

    public static void Cancelar(){
        try{
            downloadFile.cancel(true);
        }catch (Exception e) {

        }
    }


    static class DownloadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... url1) {
            int count;
            try {
                URL url = new URL(Datos.linkAudio);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/Audire/"+Datos.fileName+".mp3");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return "true";
            } catch (Exception e) {
                return  e+"";
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            SoundActivity.progressBar5.setProgress(values[0]);
        }

        protected void onPostExecute(String ab) {

            if(ab.equals("true")){
                SoundActivity.descargaAudioFinalizada = true;
                SoundActivity.Reproducir();
            }else {
                handler.removeCallbacks(runnableDescargarAudio);
                handler.postDelayed(runnableDescargarAudio,1000);
            }
        }
    }



}
