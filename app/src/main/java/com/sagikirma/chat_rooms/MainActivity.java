package com.sagikirma.chat_rooms;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    SharedPreferences sharedPref;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(MODE_PRIVATE);

        version = sharedPref.getString("new_test", "-1");


        //new ServerFeeds().execute("http://mapchat-app-983.appspot.com/login?pass=124");
        //new ServerFeeds().execute("http://mapchat-app-983.appspot.com/addChannel");
        new ServerFeeds().execute("http://mapchat-app-983.appspot.com/joinChannel?id=1000");

        Button b = (Button) findViewById(R.id.ok_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = (TextView) findViewById(R.id.version);
                t.setText(version);
            }
        });

    }

    private class ServerFeeds extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(params[0]);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return text;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                String version = obj.getString("text");


                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("new_test", result);
                editor.commit();
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
