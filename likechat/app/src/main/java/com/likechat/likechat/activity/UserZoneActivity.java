package com.likechat.likechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.util.EntityUtil;

/**
 * 我的动态
 */
public class UserZoneActivity extends BaseActivity
{
    private User m_user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_zone);
        try
        {
            m_user = (User) getIntent().getSerializableExtra("user");

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
                    try
                    {
                        switch (v.getId())
                        {
                            // 关注
                            case R.id.img_back:
                                finish();
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
            if (m_user == null)
            {
                return;
            }

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);

            txtTitle.setText(m_user.name + getString(R.string.title_user_zone_at));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
