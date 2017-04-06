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
 * 用户信息
 */
public class UserInfoActivity extends BaseActivity
{
    private User m_user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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
                        // 嗨聊
                        case R.id.lay_voice_chat:
                            Intent intentVoice = new Intent(UserInfoActivity.this, ChatVoiceCallOutActivity.class);
                            intentVoice.putExtra("user", m_user);
                            startActivity(intentVoice);
                            break;
                        // 文字聊天
                        case R.id.lay_text_chat:
                            Intent intentText = new Intent(UserInfoActivity.this, ChatTextActivity.class);
                            intentText.putExtra("user", m_user);
                            startActivity(intentText);
                            break;
                        // 关注
                        case R.id.lay_follow:
                            break;
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
            findViewById(R.id.lay_voice_chat).setOnClickListener(clickListener);
            findViewById(R.id.lay_text_chat).setOnClickListener(clickListener);
            findViewById(R.id.lay_follow).setOnClickListener(clickListener);


            View.OnClickListener imgPicClickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        // 弹出大图浏览界面
                        if (null != v.getTag())
                        {
                            int nIndex = (int)v.getTag();
                            Intent intentText = new Intent(UserInfoActivity.this, ImageBrowseActivity.class);
                            intentText.putExtra("index", nIndex);
                            startActivity(intentText);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            // 动态设置点击事件, 即有图片的才设置点击事件
            findViewById(R.id.img_pic1).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic2).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic3).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic4).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic5).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic6).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic7).setOnClickListener(imgPicClickListener);
            findViewById(R.id.img_pic8).setOnClickListener(imgPicClickListener);

            findViewById(R.id.img_pic1).setTag(1);
            findViewById(R.id.img_pic2).setTag(2);
            findViewById(R.id.img_pic3).setTag(3);
            findViewById(R.id.img_pic4).setTag(4);
            findViewById(R.id.img_pic5).setTag(5);
            findViewById(R.id.img_pic6).setTag(6);
            findViewById(R.id.img_pic7).setTag(7);
            findViewById(R.id.img_pic8).setTag(8);
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

            ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            TextView txtName = (TextView) findViewById(R.id.txt_name);
            TextView txtAge = (TextView) findViewById(R.id.txt_age);
            TextView txtId = (TextView) findViewById(R.id.txt_id);
            TextView txtCity = (TextView) findViewById(R.id.txt_city);
            TextView txtFansFollow = (TextView) findViewById(R.id.txt_fans_follow);
            TextView txtIntro = (TextView) findViewById(R.id.txt_intro);
            TextView txtCallRate = (TextView) findViewById(R.id.txt_call_rate);
            TextView txtTalkTime = (TextView) findViewById(R.id.txt_talk_time);

            imgAvatar.setImageResource(m_user.avatar_res);
            txtName.setText(m_user.name);
            txtAge.setText(String.valueOf(m_user.age));
            String strId = getString(R.string.txt_user_info_like_chat_id);
            txtId.setText(strId + m_user.id);
            txtCity.setText(m_user.city);
            String strFansFollow = getString(R.string.txt_user_info_fans_count);
            strFansFollow += m_user.fans + "  ";
            strFansFollow += getString(R.string.txt_user_info_follow_count);
            strFansFollow += m_user.follow;
            txtFansFollow.setText(strFansFollow);
            txtIntro.setText(m_user.intro);
            txtCallRate.setText("1.5币/分");
            txtTalkTime.setText("21小时35分钟");

            EntityUtil.setAnchorGenderDrawable(txtAge, m_user, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
