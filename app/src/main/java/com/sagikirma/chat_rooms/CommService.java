package com.sagikirma.chat_rooms;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

public class CommService extends Service {

    int REFRESH_TIME = 10000;
    HttpClientSingleton m_clientSingleton;
    Timer m_Timer;
    final BroadcastReceiver m_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //restart updating channels
            m_Timer.cancel();
            startGetChannels();
        }
    };

    public CommService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intent_filter = new IntentFilter("refresh_channels");
        intent_filter.setPriority(1);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(m_receiver, intent_filter);
        m_clientSingleton =  HttpClientSingleton.getInstance();

        m_Timer = new Timer();
        startGetChannels();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startGetChannels() {
        m_Timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                m_clientSingleton.getChannels();
                //m_clientSingleton.getUpdates();
            }
        }, 0, REFRESH_TIME);
    }
}
