package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ReloadService extends IntentService {

    public static final String DONE = "bla bla";

    public ReloadService()            {super(ReloadService.class.getName());}
    public ReloadService(String name) {super(name);}

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(DONE);
        this.sendBroadcast(i);

    }
}
