package com.audio.miliao.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.MessageStateVo;
import com.app.library.vo.MessageVo;
import com.audio.miliao.R;
import com.audio.miliao.activity.ChatTextActivity;
import com.audio.miliao.adapter.MessageAdapter;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchActorPage;
import com.audio.miliao.http.cmd.FetchMessageList;
import com.audio.miliao.theApp;
import com.audio.miliao.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private ListView m_list;
    private List<MessageVo> m_listMessageVo = new ArrayList<>();
    private MessageAdapter m_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_message_list, container, false);

            //updateData();
        }

        return m_root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (m_root != null)
        {
            initUI(m_root);

            FetchMessageList fetchMessageList = new FetchMessageList(handler(), null);
            fetchMessageList.send();
        }
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
                        MessageVo messageVo = (MessageVo) m_adapter.getItem(position);
                        MessageStateVo messageStateVo = DBUtil.queryMessageStateVoByMessageId(messageVo.getId());
                        if (messageStateVo == null)
                        {
                            messageStateVo = new MessageStateVo();
                            messageStateVo.setIsRead(true);
                            messageStateVo.setMessageId(messageVo.getId());
                        }
                        else
                        {
                            messageStateVo.setIsRead(true);
                        }
                        DBUtil.insertOrReplace(messageStateVo);
                        m_adapter.notifyDataSetChanged();
                        FetchActorPage fetchActorPage = new FetchActorPage(handler(), messageVo.getActorId(), null);
                        fetchActorPage.send();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            m_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.title_delete_message);
                    builder.setMessage(R.string.txt_delete_message);
                    builder.setPositiveButton(R.string.btn_positive, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            MessageVo messageVo = (MessageVo) m_adapter.getItem(position);
                            DBUtil.deleteMessageVo(messageVo.getId());
                            m_listMessageVo = DBUtil.queryAllMessageVo();
                            updateData();
                        }
                    });
                    builder.setNegativeButton(R.string.btn_negative, null);
                    builder.show();
                    return true;
                }
            });

            m_list.setOnScrollListener(ImageLoaderUtil.getPauseListener());
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
            if (m_adapter == null)
            {
                m_adapter = new MessageAdapter(getActivity(), m_listMessageVo);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(m_listMessageVo);
                m_adapter.notifyDataSetChanged();
            }
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
        case HttpUtil.RequestCode.FETCH_ACTOR_PAGE:
            FetchActorPage fetchActorPage = (FetchActorPage) msg.obj;
            if (FetchActorPage.isSucceed(fetchActorPage) && fetchActorPage.rspActorPageVo != null)
            {
                ChatTextActivity.show(getActivity(), fetchActorPage.rspActorPageVo);
            }
            else
            {
                theApp.showToast("获取信息失败");
            }
            break;
        case HttpUtil.RequestCode.FETCH_MESSAGE_LIST:
            FetchMessageList fetchMessageList = (FetchMessageList) msg.obj;
            if (FetchMessageList.isSucceed(fetchMessageList))
            {
                m_listMessageVo = DBUtil.queryAllMessageVoGroupByActorId();
                updateData();
            }
            break;
        }
    }
}
