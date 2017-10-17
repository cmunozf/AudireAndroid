package com.app.audireandroid;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Edercmf on 05/10/2017.
 */

public class DescargarAudio {

    public static Context context;
    public static DownloadFile downloadFile;
    public static void DescargarAudio1(Context context1){
        context = context1;
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
                SoundActivity.Reproducir();
            }else {
                Toast.makeText(context, ab, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
