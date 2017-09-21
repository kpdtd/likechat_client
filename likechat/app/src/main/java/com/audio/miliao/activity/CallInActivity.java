package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;

/**
 * 当新用户登录进来时，是没有购买钻和VIP的
 * 此时要假弹出通话请求界面
 * 以刺激用户购买通话金币
 */
public class CallInActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_in);
        try
        {
            initUI();
            updateData();
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
                    case R.id.txt_hang_up:
                        finish();
                        break;
                    case R.id.txt_answer:
                        Intent intent = new Intent(CallInActivity.this, SimpleBalanceActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            };

            findViewById(R.id.txt_hang_up).setOnClickListener(clickListener);
            findViewById(R.id.txt_answer).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateData()
    {
        try
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
