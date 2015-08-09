package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sagikirma.chat_rooms.HttpClientSingleton;
import com.sagikirma.chat_rooms.R;

public class CreateNewRoom extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_room);

        Button ok_button = (Button) findViewById(R.id.ok_new_room_button);
        final TextView name = (TextView) findViewById(R.id.roomName);
        final TextView id = (TextView) findViewById(R.id.new_room_id);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_name = name.getText().toString();
                String s_id = id.getText().toString();
                HttpClientSingleton.getInstance().addChannel(s_name, s_id);
                MyRoomsManager myRoomsManager = new MyRoomsManager();
                myRoomsManager.addRoom(getApplicationContext(), s_id);
                sendOrderedBroadcast(new Intent().setAction("refresh_channels"),null );
                Toast t = Toast.makeText(getApplicationContext(), R.string.request_sent, Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        });
    }

}
