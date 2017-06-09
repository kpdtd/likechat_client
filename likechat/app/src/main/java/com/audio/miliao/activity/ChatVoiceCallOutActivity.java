package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.util.EntityUtil;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.vo.ActorVo;

/**
 * 聊天——呼出
 */
public class ChatVoiceCallOutActivity extends BaseActivity
{
    private ActorVo m_actor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_voice_call_out);
        try
        {
            m_actor = (ActorVo) getIntent().getSerializableExtra("user");

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
            if (m_actor == null)
            {
                return;
            }

            ImageView imgInfo = (ImageView) findViewById(R.id.img_info_bg);
            ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            TextView txtAge = (TextView) findViewById(R.id.txt_age);
            TextView txtName = (TextView) findViewById(R.id.txt_name);
            TextView txtTalkTime = (TextView) findViewById(R.id.txt_talk_time);

            ImageLoaderUtil.displayListAvatarImageFromAsset(imgInfo, m_actor.getIcon());
            //imgInfo.setImageResource(m_user.avatar_res);
            imgInfo.setAlpha(0.4f);
            ImageLoaderUtil.displayListAvatarImageFromAsset(imgAvatar, m_actor.getIcon());
            //imgAvatar.setImageResource(m_user.avatar_res);
            txtAge.setText(m_actor.getAge());
            txtName.setText(m_actor.getNickname());
            txtTalkTime.setText("09:43");
            EntityUtil.setActorGenderDrawable(txtAge, m_actor, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
