package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.audio.miliao.R;
import com.audio.miliao.adapter.FriendAdapter;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchMyFans;
import com.audio.miliao.http.cmd.FetchMyFriends;
import com.audio.miliao.theApp;
import com.audio.miliao.vo.ActorVo;

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
    private List<ActorVo> m_actorVos;
    private int m_curCheckId;
    private String m_nextStamp;
    private boolean m_bHasNext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friend);

        try
        {
            initUI();
            FetchMyFriends fetchMyFriends = new FetchMyFriends(handler(), "", null);
            fetchMyFriends.send();
            //updateData(0);
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
                        ActorVo actor = (ActorVo) m_adapter.getItem(position);
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
                        case R.id.rdo_follow:
                            if (m_curCheckId != v.getId())
                            {
                                FetchMyFriends fetchMyFriends = new FetchMyFriends(handler(), "", null);
                                fetchMyFriends.send();
                            }
                            m_curCheckId = v.getId();
                            break;
                        case R.id.rdo_fans:
                            if (m_curCheckId != v.getId())
                            {
                                FetchMyFans fetchMyFans = new FetchMyFans(handler(), "", null);
                                fetchMyFans.send();
                            }
                            m_curCheckId = v.getId();
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
            findViewById(R.id.rdo_follow).setOnClickListener(clickListener);
            findViewById(R.id.rdo_fans).setOnClickListener(clickListener);

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
            //List<ActorPageVo> actorList = DebugUtil.getUserList();
            //List<ActorVo> actorVos = DebugUtil.actorPageVos2Actors(actorList);

            if (m_adapter == null)
            {
                m_adapter = new FriendAdapter(this, m_actorVos);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(m_actorVos);
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

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_MY_FRIENDS:
            FetchMyFriends fetchMyFriends = (FetchMyFriends) msg.obj;
            if (FetchMyFriends.isSucceed(fetchMyFriends))
            {
                m_actorVos = fetchMyFriends.rspFriends;
                m_bHasNext = fetchMyFriends.rspHasNext;
                m_nextStamp = fetchMyFriends.rspStamp;
                updateData(0);
            }
            else
            {
                theApp.showToast("获取好友失败！！");
            }
            break;
        case HttpUtil.RequestCode.FETCH_MY_FANS:
            FetchMyFans fetchMyFans = (FetchMyFans) msg.obj;
            if (FetchMyFans.isSucceed(fetchMyFans))
            {
                m_actorVos = fetchMyFans.rspFanses;
                m_bHasNext = fetchMyFans.rspHasNext;
                m_nextStamp = fetchMyFans.rspStamp;
                updateData(1);
            }
            else
            {
                theApp.showToast("获取粉丝失败！！");
            }
            break;
        }
    }
}
