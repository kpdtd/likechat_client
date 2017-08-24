package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.adapter.ChatAdapter;
import com.audio.miliao.entity.ChatMessage;
import com.audio.miliao.util.DebugUtil;
import com.netease.nim.uikit.miliao.vo.ActorVo;

import java.util.List;

/**
 * 聊天——文字
 */
public class ChatTextActivity extends BaseActivity
{
    private ActorVo m_actor;
    private ListView m_list;
    private ChatAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_text);
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
            m_list = (ListView) findViewById(R.id.list);

            m_list.setOnScrollListener(new AbsListView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState)
                {
                    try
                    {
                        switch (scrollState)
                        {
                        //停止滚动
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            m_adapter.setScrolling(false);
                            m_adapter.notifyDataSetChanged();
                            break;
                        //滚动做出了抛的动作
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            m_adapter.setScrolling(true);
                            break;
                        //正在滚动
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            m_adapter.setScrolling(true);
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
                {

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
                        case R.id.img_user_info:
                            Intent intentUserInfo = new Intent(ChatTextActivity.this, UserInfoActivity.class);
                            intentUserInfo.putExtra("user", m_actor);
                            startActivity(intentUserInfo);
                            break;
                        case R.id.img_phone:
                            Intent intentCallout = new Intent(ChatTextActivity.this, ChatVoiceCallOutActivity.class);
                            intentCallout.putExtra("user", m_actor);
                            startActivity(intentCallout);
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
            findViewById(R.id.img_user_info).setOnClickListener(clickListener);
            findViewById(R.id.img_phone).setOnClickListener(clickListener);
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

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);
            txtTitle.setText(m_actor.getNickname());

            List<ChatMessage> chatMessages = DebugUtil.getChatMessage();
            if (m_adapter == null)
            {
                m_adapter = new ChatAdapter(this, chatMessages);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(chatMessages);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
