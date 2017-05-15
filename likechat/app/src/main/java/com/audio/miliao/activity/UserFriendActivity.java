package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.audio.miliao.R;
import com.audio.miliao.adapter.FriendAdapter;
import com.audio.miliao.entity.Actor;
import com.audio.miliao.util.DebugUtil;

import java.util.List;

/**
 * 我的好友
 */
public class UserFriendActivity extends BaseActivity
{
    private RadioButton m_rdoFollow;
    private RadioButton m_rdoFans;
    private ListView m_list;
    private FriendAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friend);

        try
        {
            initUI();
            updateData(0);
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
            m_rdoFollow = (RadioButton) findViewById(R.id.rdo_follow);
            m_rdoFans = (RadioButton) findViewById(R.id.rdo_fans);
            m_list = (ListView) findViewById(R.id.list);

            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        Actor actor = (Actor) m_adapter.getItem(position);
                        Intent intentUserInfo = new Intent(UserFriendActivity.this, UserInfoActivity.class);
                        intentUserInfo.putExtra("user", actor);
                        startActivity(intentUserInfo);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

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

            findViewById(R.id.rdo_follow).performClick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 更新列表数据
     * @param index 0:关注；1：粉丝
     */
    private void updateData(int index)
    {
        try
        {
            List<Actor> actorList = DebugUtil.getUserList();

            if (m_adapter == null)
            {
                m_adapter = new FriendAdapter(this, actorList);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(actorList);
                m_adapter.notifyDataSetChanged();
            }

            updateFriendCount();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateFriendCount()
    {
        try
        {
            int nFollowCount = 20;
            int nFansCount = 300;
            String strFollow = getString(R.string.txt_user_friend_follow) + "(" + nFollowCount + ")";
            m_rdoFollow.setText(strFollow);

            String strFans = getString(R.string.txt_user_friend_fans) + "(" + nFansCount + ")";
            m_rdoFans.setText(strFans);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
