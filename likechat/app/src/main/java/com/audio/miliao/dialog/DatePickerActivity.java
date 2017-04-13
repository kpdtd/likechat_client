package com.audio.miliao.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.audio.miliao.R;
import com.audio.miliao.activity.BaseActivity;

/**
 * 日期选择框
 */
public class DatePickerActivity extends BaseActivity
{
    private DatePicker m_picker;

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
            m_picker = (DatePicker) findViewById(R.id.date_picker);

            findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent data = new Intent();
                    data.putExtra("year", m_picker.getYear());
                    data.putExtra("month", m_picker.getMonth());
                    data.putExtra("day", m_picker.getDayOfMonth());
                    setResult(RESULT_OK, data);
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
