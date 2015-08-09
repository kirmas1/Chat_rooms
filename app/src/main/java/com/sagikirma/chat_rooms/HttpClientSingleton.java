package com.sagikirma.chat_rooms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.sagikirma.chat_rooms.Map_and_RoomsList.MyRoomsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sagikirma on 13/07/2015.
 */

public class HttpClientSingleton {

    private static HttpClientSingleton mInstance = null;
    private DefaultHttpClient mhttpclient;
    SharedPreferences m_sharedPref;
    private Context m_context;
    MyRoomsManager m_roomsManager;

    private HttpClientSingleton(){
        m_roomsManager = new MyRoomsManager();
    }

    public static HttpClientSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new HttpClientSingleton();
        }
        return mInstance;
    }
    public void setM_context (Context context) {m_context = context;}
    public void setHttpClient(DefaultHttpClient httpClient){
        mhttpclient = httpClient;
    }
    public void setSharedPref( SharedPreferences sharedPref){
        m_sharedPref = sharedPref;
    }

    //saving channels to sharedPrefernnce
    public void getChannels() {
        try {

            HttpGet httpGet = new HttpGet("http://mapchat-app-983.appspot.com/getChannels");
            HttpResponse response = mhttpclient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                String content = getASCIIContentFromEntity(entity);
                SharedPreferences.Editor editor = m_sharedPref.edit();
                editor.putString("allChannels",content);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addChannel(String name, String id) {
        String icon = "da";
        new MyClient(mhttpclient, 0, m_context).execute("http://mapchat-app-983.appspot.com/addChannel?id="+id+"&name="+name+"&icon="+icon);

    }

    public boolean joinChannel(String id) {
        new MyClient(mhttpclient, 1, m_context).execute("http://mapchat-app-983.appspot.com/joinChannel?id=",id);
        return true;
    }

    public void leaveChannel(String id) {
        new MyClient(mhttpclient, 2, m_context).execute("http://mapchat-app-983.appspot.com/leaveChannel?id=",id);
    }

    public void sendMessage(String channel_id, String text, Double longtitude, Double latitude) {
        new MyClient(mhttpclient, 3, m_context).execute("http://mapchat-app-983.appspot.com/sendMessage?channel_id="+channel_id,
                "&text="+text,"&longtitude="+longtitude.toString(),"&latitude="+latitude.toString());
    }
//    public void sendMessage(String channel_id, String text, Double longtitude, Double latitude) {
//        new MyClient(mhttpclient, 3, m_context).execute("http://mapchat-app-983.appspot.com/sendMessage",channel_id, text, longtitude.toString(),latitude.toString());
//    }
    protected String getASCIIContentFromEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    //run on separated thread!!
    public void getUpdates() {
        try {

            HttpGet httpGet = new HttpGet("http://mapchat-app-983.appspot.com/getUpdates");
            HttpResponse response = mhttpclient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                String content = getASCIIContentFromEntity(entity);

                JSONObject obj = new JSONObject(content);
                //TO Do: check "change server"
                m_roomsManager.saveUpdates(m_context, obj.getJSONArray("messages"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
