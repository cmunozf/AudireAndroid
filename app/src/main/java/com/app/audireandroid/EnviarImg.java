package com.app.audireandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Edercmf on 05/10/2017.
 */

public class EnviarImg {

    public static Context context;

    public static EnviarImgTask enviarImgTask;

    public static void Inicializar(Context context1){
        context = context1;
        enviarImgTask = new EnviarImgTask();
        enviarImgTask.execute();
    }

    public static void Cancelar(){
        try{
            enviarImgTask.cancel(true);
        }catch (Exception e) {

        }
    }

    static class EnviarImgTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... arg0) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("image", Datos.file));
            return post(Datos.url,params);

        }

        protected void onPostExecute(String ab) {
            //MainActivity.tv1.setText("Imagen Enviada");
            Toast.makeText(context, "Imagen Enviada", Toast.LENGTH_LONG).show();
            //MainActivity.tv1.setText(ab+"asdasdasd");
            //Toast.makeText(context, ab, Toast.LENGTH_LONG).show();
        }
    }

    public static String post(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        try {

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

/* example for setting a HttpMultipartMode */
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    builder.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    builder.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

            HttpEntity multiPartEntity = builder.build();

            httpPost.setEntity(multiPartEntity);

            HttpResponse response = httpClient.execute(httpPost, localContext);

            String responseAsString = EntityUtils.toString(response.getEntity());

            return responseAsString;

        } catch (IOException e) {
            return "ERR1: "+e.toString();
        }
    }
}
