package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sagikirma.chat_rooms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagikirma on 09/07/2015.
 */
public class RoomsListMenuAdapter extends BaseAdapter {
    ActionBarActivity activity;
    LayoutInflater inflater;
    List<MenuItem> items;

    RoomsListMenuAdapter (ActionBarActivity activity, List<MenuItem> items) {
        this.activity = activity;
        this.items = new ArrayList<>();
        this.items = items;
    }

    public int getCount () { return items.size(); }

    public MenuItem getItem(int position) {
        return items.get(position);
    }
    public long getItemId (int position) { return position;}

    ////////
    public View getView (int position, View convertView, ViewGroup parent) {

        if (inflater==null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
            convertView = inflater.inflate(R.layout.rooms_list_item, null);

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.menu_title);
        TextView channel_id = (TextView) convertView.findViewById(R.id.channel_id);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.menu_item);
        ImageView imgSign = (ImageView) convertView.findViewById(R.id.sign_unsigned_to_channel);


        MenuItem item = getItem(position);

        //imgIcon.setImageResource(item.getIcon());
        txtTitle.setText(item.getName());
        channel_id.setText(item.getId());
        if (item.getJoined()) {
            imgSign.setImageResource(android.R.drawable.presence_online);
        } else {
            imgSign.setImageResource(android.R.drawable.presence_offline);
        }

        return convertView;
    }
}
