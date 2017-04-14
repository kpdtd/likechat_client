package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;

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
                    try
                    {
                        switch (v.getId())
                        {
                            case R.id.img_back:
                                finish();
                                break;
                            case R.id.txt_switch_account:
                                //测试
                                //Intent intentText = new Intent(SettingsActivity.this, VideoZoneActivity.class);
                                //startActivity(intentText);
                                break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.txt_switch_account).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
