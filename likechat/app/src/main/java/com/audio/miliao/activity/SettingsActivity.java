package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.event.LogoutEvent;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchCustomerService;
import com.audio.miliao.theApp;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.PreferUtil;

import de.greenrobot.event.EventBus;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity
{
    private TextView mTxtCustomInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try
        {
            initUI();

            FetchCustomerService fetchCustomerService = new FetchCustomerService(handler(), null);
            fetchCustomerService.send();
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
            mTxtCustomInfo = (TextView) findViewById(R.id.txt_settings_q_group);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId())
                    {
                    case R.id.img_back:
                        finish();
                        break;
                    // 切换账户
                    case R.id.txt_switch_account:
                        PreferUtil.clearAll();
                        Intent intentLogin = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intentLogin);

                        EventBus.getDefault().post(new LogoutEvent());
                        finish();
                        break;
                    case R.id.txt_settings_clear_cache:
                        //PreferUtil.clearAll();
                        //Toast.makeText(SettingsActivity.this, "缓存已清空", Toast.LENGTH_SHORT).show();
                        final View view = v;
                        view.setEnabled(false);
                        Runnable runnable = new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                clearCache();
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        view.setEnabled(true);
                                    }
                                });

                                theApp.showToast("缓存已清空");
                            }
                        };
                        new Thread(runnable).start();
                        break;
                    }
                }
            };

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.txt_settings_clear_cache).setOnClickListener(clickListener);
            findViewById(R.id.txt_switch_account).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 清空缓存
     */
    private void clearCache()
    {
        ImageLoaderUtil.getInstance().clearDiskCache();
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_CUSTOM_SERVICE:
            FetchCustomerService fetchCustomerService = (FetchCustomerService) msg.obj;
            if (FetchCustomerService.isSucceed(fetchCustomerService))
            {
                mTxtCustomInfo.setText(fetchCustomerService.rspCustomInfo);
            }
            break;
        }
    }
}
