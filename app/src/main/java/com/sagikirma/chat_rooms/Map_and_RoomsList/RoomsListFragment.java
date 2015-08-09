package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sagikirma.chat_rooms.HttpClientSingleton;
import com.sagikirma.chat_rooms.R;
import com.sagikirma.chat_rooms.RecommendationChannel.RecommendationRoomActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class RoomsListFragment extends Fragment implements JoinLeaveRoomDialog.NoticeDialogListener {

    private RoomsListMenuAdapter menuAdapter;
    private List<MenuItem> roomsList;
    SharedPreferences sharedPref;
    JoinLeaveRoomDialog join_leave_dialog;
    MyRoomsManager myRoomsManager;
    ListView lstMenu;
    final BroadcastReceiver m_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String channel_id = b.getString("channelID");
            Boolean join = b.getBoolean("join");
            if (join) {
                myRoomsManager.addRoom(getActivity(), channel_id);
            } else {
                myRoomsManager.removeRoom(getActivity(), channel_id);
            }
            //chosen_room.join_leave();
            for (MenuItem  m: roomsList) {
                if (m.getId().equals(channel_id)) {
                    m.join_leave();
                    break;
                }
            }
            menuAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rooms_list, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        myRoomsManager = new MyRoomsManager();
        Button recomRoom = (Button) view.findViewById(R.id.recom_room_button);
        recomRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RecommendationRoomActivity.class);
                startActivity(i);
            }
        });
        roomsList = new ArrayList<>();
        getTheRooms(roomsList);
        markMyRoomsInRoomsList();

        lstMenu = (ListView) view.findViewById(R.id.lstMenu);
        menuAdapter = new RoomsListMenuAdapter((ActionBarActivity) getActivity(), roomsList);

        lstMenu.setAdapter(menuAdapter);
        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String channel_name = roomsList.get(position).getName();
                String channel_id = roomsList.get(position).getId();
                Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("channel_name",channel_name);
                intent.putExtra("channel_id",channel_id);
                startActivity(intent);
//                Toast toast = Toast.makeText(getActivity(), "normal click on " + clicked_room, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
            }
        });

        join_leave_dialog = JoinLeaveRoomDialog.newInstance(this);
        lstMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putBoolean("T join F leave", !roomsList.get(position).getJoined());
                b.putInt("location", position);
                join_leave_dialog.setArguments(b);
                join_leave_dialog.show(getFragmentManager(), "kirma");

                return true;
            }
        });

        IntentFilter intent_filter = new IntentFilter();
        intent_filter.addAction("refresh_list");
        getActivity().registerReceiver(m_receiver, intent_filter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void markMyRoomsInRoomsList() {
        List<String> mySignedRoomsId = myRoomsManager.getRooms(getActivity());
        if (mySignedRoomsId!=null) {
            //
            for (int i=0; i<mySignedRoomsId.size(); i++) {
               // find mySignedRoomsId(i) in roomsList and setJoind(true)
                for (MenuItem  m: roomsList) {
                    if (m.getId().equals(mySignedRoomsId.get(i))) {
                        m.setJoined(true);
                        break;
                    }
                }
            }
        }
    }

    private void getTheRooms (List<MenuItem> roomsList) {

        String rooms_raw = sharedPref.getString("allChannels", "-1");
        extractRooms(rooms_raw, roomsList); //add rooms to roomList.
    }
    private void extractRooms(String rooms_raw, List<MenuItem> roomsList) {
        JSONObject rooms = null;
        try {
            rooms = new JSONObject(rooms_raw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (rooms!=null) {
            try {
                JSONArray jArray = rooms.getJSONArray("channels");
                for (int i=0; i<jArray.length(); i++) {

                    JSONObject oneObject = jArray.getJSONObject(i);
                    String id = oneObject.getString("id");
                    String name = oneObject.getString("name");
                    String icon = oneObject.getString("icon");
                    roomsList.add(new com.sagikirma.chat_rooms.Map_and_RoomsList.MenuItem(id, name, icon));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDialogOkClick(int location) {
        MenuItem chosen_room  = roomsList.get(location);
        if (!chosen_room.getJoined()) {
            HttpClientSingleton.getInstance().joinChannel(chosen_room.getId());
        } else {
            HttpClientSingleton.getInstance().leaveChannel(chosen_room.getId());
        }
    }

}


