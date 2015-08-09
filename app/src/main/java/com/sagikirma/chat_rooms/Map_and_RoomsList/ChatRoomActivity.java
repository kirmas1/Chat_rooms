package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sagikirma.chat_rooms.HttpClientSingleton;
import com.sagikirma.chat_rooms.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatRoomActivity extends ActionBarActivity {

    private String channel_id;
    private EditText chatText;
    private Button buttonSend;
    private ListView listView;
    private ChatArrayAdapter chatArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Bundle info = getIntent().getExtras();

        channel_id = (String) info.get("channel_id");
        String channel_name = (String) info.get("channel_name");
        getSupportActionBar().setTitle(channel_name);

        listView = (ListView) findViewById(R.id.listView1);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        //chatArrayAdapter.add(new ChatMessage(true, readSavedData()));
//        new testGetUpdate().execute();

        String data = readSavedData();

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });
    }

    private boolean sendChatMessage(){
        ///////////TO DO
        Double latitude= 10.20;
        Double longtitude= 10.20;

        HttpClientSingleton.getInstance().sendMessage(channel_id, chatText.getText().toString(), longtitude,  latitude);
        return true;
    }


    public String readSavedData ( ) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = openFileInput (channel_id) ;
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buff_reader = new BufferedReader ( isr ) ;

            String readString = buff_reader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buff_reader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch ( IOException ioe ) {
            ioe.printStackTrace ( ) ;
        }
        return datax.toString() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private class testGetUpdate extends AsyncTask {
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            HttpClientSingleton.getInstance().getUpdates();
//            return null;
//        }
//    }

}

