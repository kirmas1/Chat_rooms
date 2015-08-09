package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by sagikirma on 17/07/2015.
 */
public class MyRoomsManager {

    public static final String PREFS_NAME = "SIGEND_ROOMS";

    // This  methods are used for maintaining my signed FAVORITES.
    
    public void saveRooms(Context context, List<String> rooms) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        Set<String> rooms_set = new HashSet<>(rooms);
        editor.putStringSet("signedRooms", rooms_set);
        editor.commit();
    }

    public void addRoom(Context context, String id) {
        List<String> favorites = getRooms(context);
        if (favorites == null)
            favorites = new ArrayList<String>();
        favorites.add(id);
        saveRooms(context, favorites);
    }

    public void removeRoom(Context context, String id) {
        ArrayList<String> rooms = getRooms(context);
        if (rooms != null) {
            rooms.remove(id);
            saveRooms(context, rooms);
        }
    }

    public ArrayList<String> getRooms(Context context) {
        SharedPreferences settings;
        List<String> rooms;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains("signedRooms")) {

            Set<String> set_rooms = settings.getStringSet("signedRooms", null);
            List<String> rooms_list = new ArrayList<String>(set_rooms);
            return (ArrayList<String>) rooms_list;
        } else
            return null;

    }

    public void saveUpdates(Context context, JSONArray messages) {
        for (int i = 0; i < messages.length(); i++) {
            try {
                JSONObject obj = messages.getJSONObject(i);
                String channel_id = (String) obj.get("channel_id");

                FileOutputStream fos = context.openFileOutput(channel_id, Context.MODE_APPEND);
                fos.write(obj.toString().getBytes());
                fos.close();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

