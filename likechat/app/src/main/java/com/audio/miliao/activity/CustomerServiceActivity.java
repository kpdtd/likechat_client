package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;

/**
 * 客服界面。该界面显示的效果跟Dialog一样
 */
public class CustomerServiceActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        initUI();
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
                    case R.id.btn_ok:
                        finish();
                        break;
                    }
                }
            };

            findViewById(R.id.btn_ok).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
