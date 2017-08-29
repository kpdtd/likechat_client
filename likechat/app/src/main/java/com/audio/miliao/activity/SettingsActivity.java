package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;
import com.audio.miliao.http.cmd.FetchHomeContent;
import com.audio.miliao.theApp;
import com.audio.miliao.util.ImageLoaderUtil;

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
                        case R.id.img_back:
                            finish();
                            break;
                    case R.id.lay_contact_us:
                        FetchHomeContent addAttention = new FetchHomeContent(null, null);
                        addAttention.send();
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
            findViewById(R.id.lay_contact_us).setOnClickListener(clickListener);
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
}
