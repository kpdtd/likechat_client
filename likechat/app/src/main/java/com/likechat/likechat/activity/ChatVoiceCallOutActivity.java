package com.likechat.likechat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.util.EntityUtil;

/**
 * 聊天——呼出
 */
public class ChatVoiceCallOutActivity extends BaseActivity
{
    private User m_user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_voice_call_out);
        try
        {
            m_user = (User) getIntent().getSerializableExtra("anchor");

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

            ImageView imgInfo = (ImageView) findViewById(R.id.img_info_bg);
            ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            TextView txtAge = (TextView) findViewById(R.id.txt_age);
            TextView txtName = (TextView) findViewById(R.id.txt_name);
            TextView txtTalkTime = (TextView) findViewById(R.id.txt_talk_time);

            imgInfo.setImageResource(m_user.avatar_res);
            imgInfo.setAlpha(0.4f);
            imgAvatar.setImageResource(m_user.avatar_res);
            txtAge.setText(String.valueOf(m_user.age));
            txtName.setText(m_user.name);
            txtTalkTime.setText("09:43");
            EntityUtil.setAnchorGenderDrawable(txtAge, m_user, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
