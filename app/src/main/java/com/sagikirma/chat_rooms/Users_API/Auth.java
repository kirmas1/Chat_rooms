package com.sagikirma.chat_rooms.Users_API;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.sagikirma.chat_rooms.HttpClientSingleton;
import com.sagikirma.chat_rooms.Map_and_RoomsList.*;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.util.List;

/*
auth from mapchat-app-983,appspot.com/login
 */
public class Auth extends AsyncTask<String, Void, Boolean> {

    private DefaultHttpClient httpclient;
    private HttpResponse response;
    private String content =  null;
    Context context;

    public Auth(DefaultHttpClient httpclient, Context context)
    {
        this.httpclient = httpclient;
        this.context = context;
    }

    protected Boolean doInBackground(String... urls) {

        try {

            HttpGet httpGet = new HttpGet(urls[0]);
            response = httpclient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                content = out.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
        return false;
    }

    //display the response from the request above
    protected void onPostExecute(Boolean result) {
//        Toast.makeText(context, "Response from request: " + content,
//                Toast.LENGTH_LONG).show();
        //TO DO check if response (content) is ok
        HttpClientSingleton.getInstance().setHttpClient(httpclient);
        HttpClientSingleton.getInstance().setM_context(context.getApplicationContext());
        Intent intent = new Intent(context.getApplicationContext(), MainDisplayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);

    }
}
