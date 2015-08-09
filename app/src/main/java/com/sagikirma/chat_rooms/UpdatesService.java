package com.sagikirma.chat_rooms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import java.util.Timer;
import java.util.TimerTask;

public class UpdatesService extends Service {

    int REFRESH_TIME = 10000;
    HttpClientSingleton m_clientSingleton;
    Timer m_Timer;

    public UpdatesService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        m_clientSingleton =  HttpClientSingleton.getInstance();

        m_Timer = new Timer();
        startGetUpdates();
    }

    private void startGetUpdates() {
        m_Timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                m_clientSingleton.getUpdates();
            }
        }, 0, REFRESH_TIME);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
