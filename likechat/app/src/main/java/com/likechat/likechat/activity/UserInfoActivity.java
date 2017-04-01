package com.likechat.likechat.activity;

import android.os.Bundle;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.Anchor;

/**
 * 用户信息
 */
public class UserInfoActivity extends BaseActivity
{
    private Anchor m_anchor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        try
        {
            m_anchor = (Anchor) getIntent().getSerializableExtra("anchor");

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
