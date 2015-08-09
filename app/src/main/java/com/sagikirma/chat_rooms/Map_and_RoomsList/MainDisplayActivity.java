package com.sagikirma.chat_rooms.Map_and_RoomsList;


import android.hardware.SensorEventListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.sagikirma.chat_rooms.CommService;
import com.sagikirma.chat_rooms.HttpClientSingleton;
import com.sagikirma.chat_rooms.R;
import com.sagikirma.chat_rooms.UpdatesService;

import java.util.GregorianCalendar;




public class MainDisplayActivity extends ActionBarActivity implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 2000;
    float last_x,last_y, last_z;
    private long lastUpdate;
    public static FragmentManager fm;
    SensorManager sensorManager;
    RoomsListFragment roomsListFragment;
    Fragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpClientSingleton.getInstance().setSharedPref(getPreferences(MODE_PRIVATE)); //move this to first activity

        Intent i= new Intent(this, CommService.class);
        //i.putExtra("KEY1", "Value to be used by the service");
        startService(i);
//        Intent i2 = new Intent(this, UpdatesService.class);
//        startService(i2);
        setContentView(R.layout.activity_main_display);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            roomsListFragment = new RoomsListFragment();
            ft.add(R.id.roomsListFragment, roomsListFragment);
        }

        mapFragment = new com.sagikirma.chat_rooms.Map_and_RoomsList.mapFragment();
        ft.add(R.id.mapFragment, mapFragment);
        ft.commit();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }




    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void getAccelerometer(SensorEvent event) {

        float[] values = event.values;
        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {

            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                Long timeToAlert = new GregorianCalendar().getTimeInMillis() + 5000;
                Intent intent = new Intent(this, NotificationReceiver.class);
                intent.putExtra("sender", "one");

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeToAlert, PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

                Toast.makeText(this, "alarms were set", Toast.LENGTH_SHORT).show();
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_display, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.add_channel:
                Intent i = new Intent(this, CreateNewRoom.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



