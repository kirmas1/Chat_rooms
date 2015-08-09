package com.sagikirma.chat_rooms;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagikirma on 13/07/2015.
 */
public class MyClient extends AsyncTask<String, Void, Boolean> {

    private int requestType; // 0 = addChannel, 1 = joinChannel, 2 = leaveChannel, 3 = sendMessage
    private DefaultHttpClient httpclient;
    private HttpResponse response;
    private String content =  null;
    private Context m_context;
    private String m_channelID;
    private Boolean answer = false;


    public MyClient(DefaultHttpClient httpclient, int requestType, Context context)
    {
        this.httpclient = httpclient;
        this.requestType = requestType;
        m_context = context;

    }

    protected Boolean doInBackground(String... urls) {
        try {
            switch (requestType) {
                case 0:
                    HttpPost httpPost = new HttpPost(urls[0]);
                    response = httpclient.execute(httpPost);
                    break;
                case 1:
                    httpPost = new HttpPost(urls[0] + urls[1]);
                    response = httpclient.execute(httpPost);
                    m_channelID = urls[1];
                    break;
                case 2:
                    httpPost = new HttpPost(urls[0]+urls[1]);
                    response = httpclient.execute(httpPost);
                    m_channelID = urls[1];
                    break;
                case 3:
                    httpPost = new HttpPost(urls[0]+urls[1]+urls[2]+urls[3]);
//                    httpPost = new HttpPost(urls[0]);
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//                    nameValuePairs.add(new BasicNameValuePair("channel_id", urls[1]));
//                    nameValuePairs.add(new BasicNameValuePair("text", urls[2]));
//                    nameValuePairs.add(new BasicNameValuePair("longtitude", urls[3]));
//                    nameValuePairs.add(new BasicNameValuePair("latitude", urls[4]));
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httpPost);
                    break;
                default:
                    HttpGet httpGet = new HttpGet(urls[0]);
                    response = httpclient.execute(httpGet);
                    break;
            }

            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                content = getASCIIContentFromEntity(entity);
                JSONObject obj = new JSONObject(content);

                if (obj.getString("status").equals("1")) answer = true;
                else answer = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
        return answer;
    }

    protected void onPostExecute(Boolean result) {
        if (result) {
            Intent intent = new Intent("refresh_list");
            //intent.setAction("refresh_list");
            intent.putExtra("channelID", m_channelID);

            if (requestType==1) {
                intent.putExtra("join", true);
                m_context.sendBroadcast(intent);
            } else if (requestType==2) {
                intent.putExtra("join", false);
                m_context.sendBroadcast(intent);
            }
        }

    }

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
}
