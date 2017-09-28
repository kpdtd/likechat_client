package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;

import com.audio.miliao.event.ShowMessageListEvent;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class NotificationActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        finish();
    }

    private void handleIntent(Intent intent)
    {
        if (MainActivity.isRunning())
        {
            EventBus.getDefault().post(new ShowMessageListEvent());
        }
        else
        {
            Intent intentMain = new Intent(this, MainActivity.class);
            intentMain.putExtra("come_from", "notification");
            startActivity(intentMain);
        }
    }
}
