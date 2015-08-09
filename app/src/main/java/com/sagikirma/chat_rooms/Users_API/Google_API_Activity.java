package com.sagikirma.chat_rooms.Users_API;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.sagikirma.chat_rooms.Map_and_RoomsList.MainDisplayActivity;
import com.sagikirma.chat_rooms.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

        import org.apache.http.impl.client.DefaultHttpClient;

        import java.util.ArrayList;


public class Google_API_Activity extends ActionBarActivity {


    AccountManager m_accountManager;
    private Account[] m_accounts;
    Spinner m_spinner;
    DefaultHttpClient httpClient = new DefaultHttpClient();
    Account m_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__api_);

        m_accountManager = AccountManager.get(getApplicationContext());
        m_accounts = m_accountManager.getAccountsByType("com.google");

        ArrayList<String> accountList = new ArrayList<String>();
        for (Account account : m_accounts) {
            accountList.add(account.name);
        }

        m_spinner = (Spinner) findViewById(R.id.account);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner.setAdapter(dataAdapter);

        Button startAuth = (Button) findViewById(R.id.startAuth);
        startAuth.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                m_spinner = (Spinner) findViewById(R.id.account);
                m_account = m_accounts[m_spinner.getSelectedItemPosition()];
                //When getAuthToken finish it will invoke run() method of OnTokenAcquired with the given parameters.
                //"ah" - recognize to appengine
                m_accountManager.getAuthToken(m_account, "ah", null, false, new OnTokenAcquired(httpClient, Google_API_Activity.this), null);

            }
        });
    }

    //after user accept or denied permission
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            //user accept permissions so lets continue and get cookie
            m_accountManager.getAuthToken(m_account, "ah", null, false, new OnTokenAcquired(httpClient, Google_API_Activity.this), null);

        }
        else if(resultCode ==  RESULT_CANCELED){
            // user canceled
        }
    }

}
