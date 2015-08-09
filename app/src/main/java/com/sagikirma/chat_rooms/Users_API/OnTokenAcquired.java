package com.sagikirma.chat_rooms.Users_API;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.apache.http.impl.client.DefaultHttpClient;

//the result for the auth token request is returned to your application
//via the Account Manager Callback you specified when making the request.
//check the returned bundle if an Intent is stored against the AccountManager.KEY_INTENT key.
//if there is an Intent then start the activity using that intent to ask for user permission
//otherwise you can retrieve the auth token from the bundle.
public class OnTokenAcquired implements AccountManagerCallback<Bundle> {

    private static final int USER_PERMISSION = 989;
    private static final String APP_ID = "mapchat-app-983";
    private DefaultHttpClient httpclient;
    Activity activity;

    public OnTokenAcquired(DefaultHttpClient httpclient, Activity activity)
    {
        this.httpclient = httpclient;
        this.activity = activity;
    }

    public void run(AccountManagerFuture<Bundle> result) {

        Bundle bundle;

        try {
            bundle = (Bundle) result.getResult();
            //if it's the first use. we need to ask permission from user
            if (bundle.containsKey(AccountManager.KEY_INTENT)) {
                Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
                intent.setFlags(intent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
                //start activity (GoogleAPIactivity) at function onActivityResult
                activity.startActivityForResult(intent, USER_PERMISSION);
            } else {
                //we have got token so get Cookie.
                setAuthToken(bundle);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //using the auth token and ask for a auth cookie
    protected void setAuthToken(Bundle bundle) {
        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        //run in another thread
        new GetCookie(httpclient, APP_ID, activity.getBaseContext()).execute(authToken);
//        GetCookie gc = (GetCookie) new GetCookie(httpclient, APP_ID, activity.getBaseContext());
//        gc.execute(authToken);
//        gc.


    }
};