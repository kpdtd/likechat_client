package com.audio.miliao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.audio.miliao.R;
import com.audio.miliao.activity.ChatVoiceCallOutActivity;
import com.audio.miliao.adapter.CallHistoryAdapter;
import com.audio.miliao.entity.CallHistory;
import com.audio.miliao.entity.ChatMessage;
import com.audio.miliao.util.DebugUtil;
import com.app.library.vo.ActorVo;

import java.util.List;

public class CallHistoryListFragment extends BaseFragment
{
    /** 界面中的root view */
    private View m_root;
    private ListView m_list;
    private CallHistoryAdapter m_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_call_history_list, container, false);

            initUI(m_root);
            updateData();
        }

        return m_root;
    }


    private void initUI(final View root)
    {
        try
        {
            m_list = (ListView) root.findViewById(R.id.list);

            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        Intent intentChat = new Intent(getActivity(), ChatVoiceCallOutActivity.class);
                        ActorVo actor = (m_adapter.getItem(position) == null ? null : ((ChatMessage) m_adapter.getItem(position)).from);
                        intentChat.putExtra("user", actor);
                        startActivity(intentChat);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

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
            List<CallHistory> callHistories = DebugUtil.getCallHistory();
            if (m_adapter == null)
            {
                m_adapter = new CallHistoryAdapter(getActivity(), callHistories);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(callHistories);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
