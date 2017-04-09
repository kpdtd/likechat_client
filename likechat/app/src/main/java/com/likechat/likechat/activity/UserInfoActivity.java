package com.likechat.likechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.util.DebugUtil;
import com.likechat.likechat.util.EntityUtil;
import com.likechat.likechat.util.ImageLoaderUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            updatePhoto();
            updateAnchor();
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
                            // 最新动态
                            case R.id.txt_latest_news:
                                Intent intentZone = new Intent(UserInfoActivity.this, UserZoneActivity.class);
                                intentZone.putExtra("user", m_user);
                                startActivity(intentZone);
                                break;

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

            findViewById(R.id.txt_latest_news).setOnClickListener(clickListener);
            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.lay_voice_chat).setOnClickListener(clickListener);
            findViewById(R.id.lay_text_chat).setOnClickListener(clickListener);
            findViewById(R.id.lay_follow).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updatePhoto()
    {
        try
        {
            if (m_user == null)
            {
                return;
            }

            final List<String> lstThumb   = new ArrayList<>();
            final JSONArray    jArrPhoto = new JSONArray();

            // 这是生成的随机数
            Random rand = new Random(System.currentTimeMillis());
            java.util.HashSet<Integer> setExist = new java.util.HashSet<>();

            // 椭机数产生主播
            for(int i = 0; i < 8; i++)
            {
                int nIndex = -1;
                while(nIndex == -1 || setExist.contains(nIndex))
                {
                    nIndex = rand.nextInt(20) + 1;
                }

                setExist.add(nIndex);
                lstThumb.add("thumb" + nIndex + ".jpg");
                jArrPhoto.put("avatar" + nIndex + ".jpg");
            }

            View.OnClickListener clickListener = new View.OnClickListener()
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
                            int nCount = 8;
                            Intent intentText = new Intent(UserInfoActivity.this, ImageBrowseActivity.class);
                            intentText.putExtra("pos", nIndex);
                            intentText.putExtra("count", nCount);
                            intentText.putExtra("urls", jArrPhoto.toString());
                            startActivity(intentText);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            List<View> viewList = new ArrayList<>();
            viewList.add(findViewById(R.id.img_pic1));
            viewList.add(findViewById(R.id.img_pic2));
            viewList.add(findViewById(R.id.img_pic3));
            viewList.add(findViewById(R.id.img_pic4));
            viewList.add(findViewById(R.id.img_pic5));
            viewList.add(findViewById(R.id.img_pic6));
            viewList.add(findViewById(R.id.img_pic7));
            viewList.add(findViewById(R.id.img_pic8));

            ImageView vThumb;
            for(int i = 0; i < 8; i++)
            {
                vThumb = (ImageView)viewList.get(i);
                vThumb.setTag(i);
                vThumb.setOnClickListener(clickListener);
                ImageLoaderUtil.displayListAvatarImageFromAsset(vThumb, lstThumb.get(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateAnchor()
    {
        try
        {
            if (m_user == null)
            {
                return;
            }

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        // 主播界面
                        if (null != v.getTag())
                        {
                            // 关闭当前页
                            finish();

                            User user = (User)v.getTag();
                            Intent intentUserInfo = new Intent(UserInfoActivity.this, UserInfoActivity.class);
                            intentUserInfo.putExtra("user", user);
                            startActivity(intentUserInfo);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            List<User> userList = DebugUtil.getUserList();

            // 去掉当前用户
            for(int i = 0; i < userList.size(); i++)
            {
                if (userList.get(i).name.equals(m_user.name))
                {
                    userList.remove(i);
                    break;
                }
            }

            List<View> viewList = new ArrayList<>();
            viewList.add(findViewById(R.id.lay_anchor1));
            viewList.add(findViewById(R.id.lay_anchor2));
            viewList.add(findViewById(R.id.lay_anchor3));
            viewList.add(findViewById(R.id.lay_anchor4));
            viewList.add(findViewById(R.id.lay_anchor5));
            viewList.add(findViewById(R.id.lay_anchor6));
            viewList.add(findViewById(R.id.lay_anchor7));
            viewList.add(findViewById(R.id.lay_anchor8));

            // 这是生成的随机数
            Random rand = new Random(System.currentTimeMillis());
            java.util.HashSet<Integer> setExist = new java.util.HashSet<>();
            LinearLayout layAnchor = null;
            int nSize = userList.size() < 8 ? userList.size() : 8;
            User user = null;

            // 椭机数产生主播
            for(int i = 0; i < 8; i++)
            {
                user = null;
                layAnchor = (LinearLayout)viewList.get(i);
                if (i < nSize)
                {
                    int nIndex = -1;
                    while(nIndex == -1 || setExist.contains(nIndex))
                    {
                        nIndex = rand.nextInt(userList.size());
                    }

                    setExist.add(nIndex);
                    user = userList.get(nIndex);
                    layAnchor.setTag(user);
                    layAnchor.setOnClickListener(clickListener);
                }else
                {
                    layAnchor.setTag(null);
                    layAnchor.setOnClickListener(null);
                }

                int chSize = layAnchor.getChildCount();
                for(int j = 0; j < chSize; j++)
                {
                    View vChile = layAnchor.getChildAt(j);
                    if (vChile instanceof ImageView)
                    {
                        if (null == user)
                        {
                            ((ImageView)vChile).setImageResource(0);
                        }
                        else
                        {
                            ImageLoaderUtil.displayListAvatarImageFromAsset((ImageView)vChile, user.avatar);
                        }
                    }
                    else if (vChile instanceof TextView)
                    {
                        ((TextView)vChile).setText(null == user ? "" : user.name);
                    }
                }
            }
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

            ImageLoaderUtil.displayListAvatarImageFromAsset(imgAvatar, m_user.avatar);
            //imgAvatar.setImageResource(m_user.avatar_res);
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
