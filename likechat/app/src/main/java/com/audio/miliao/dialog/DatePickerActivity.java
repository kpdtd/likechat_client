package com.audio.miliao.dialog;

import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;
import com.audio.miliao.activity.BaseActivity;

/**
 * 日期选择框
 */
public class DatePickerActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
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
            findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
