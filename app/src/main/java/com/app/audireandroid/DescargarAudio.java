package com.app.audireandroid;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

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

    public static void DescargarAudio1(Context context1){
        context = context1;
        new DownloadFile().execute();
    }

    static class DownloadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... url1) {
            int count;
            try {
                URL url = new URL("http://satoshibutton.tk/sound.mp3");
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
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(String ab) {
            MainActivity.datos = Uri.parse("/sdcard/Audire/"+Datos.fileName+".mp3");
            MainActivity.mp = MediaPlayer.create(context, MainActivity.datos);
            MainActivity.mp.start();
        }
    }
}
