package com.likechat.likechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.likechat.likechat.R;
import com.likechat.likechat.activity.ChatVoiceCallOutActivity;
import com.likechat.likechat.adapter.CallHistoryAdapter;
import com.likechat.likechat.entity.CallHistory;
import com.likechat.likechat.entity.ChatMessage;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.util.DebugUtil;

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
                        User user = (m_adapter.getItem(position) == null ? null : ((ChatMessage) m_adapter.getItem(position)).from);
                        intentChat.putExtra("user", user);
                        startActivity(intentChat);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
