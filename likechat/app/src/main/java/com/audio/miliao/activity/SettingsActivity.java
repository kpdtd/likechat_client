package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;
import com.audio.miliao.http.cmd.FetchHomeContent;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try
        {
            initUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initUI()
    {
        try
        {
            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId())
                    {
                    case R.id.lay_contact_us:
                        FetchHomeContent addAttention = new FetchHomeContent(null, null);
                        addAttention.send();
                        break;
                    }
                }
            };

            findViewById(R.id.lay_contact_us).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
