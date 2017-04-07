package com.likechat.likechat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.ChatMessage;

import java.util.List;

/**
 * 聊天消息列表
 */
public class ChatAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<ChatMessage> m_listTextChatMessages;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;

    public ChatAdapter(Activity activity, List<ChatMessage> listTextChatMessages)
    {
        m_parent = activity;
        m_listTextChatMessages = listTextChatMessages;
    }

    public void updateData(List<ChatMessage> listTextChatMessages)
    {
        m_listTextChatMessages = listTextChatMessages;
    }

    public void setScrolling(boolean bIsScrolling)
    {
        m_bIsScrolling = bIsScrolling;
    }

    @Override
    public int getCount()
    {
        try
        {
            return m_listTextChatMessages.size();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        try
        {
            return m_listTextChatMessages.get(position);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        try
        {
            ViewHolder holder = null;

            if (convertView != null)
            {
                // 支持器
                holder = (ViewHolder) convertView.getTag();
            }
            else
            {
                // 加载列表项
                convertView = View.inflate(m_parent, R.layout.item_text_chat, null);

                // 支持器
                holder = new ViewHolder(convertView);
            }

            ///if (!m_bIsScrolling)
            {
                // 设置项信息
                setItemInfo(position, holder);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }

    private void setItemInfo(final int nPosition, final ViewHolder holder)
    {
        try
        {
            ChatMessage textChatMessage = (ChatMessage) getItem(nPosition);
            if (textChatMessage != null)
            {
                if (AppData.isCurUser(textChatMessage.from))
                {
                    holder.layMe.setVisibility(View.VISIBLE);
                    holder.layOthers.setVisibility(View.GONE);
                    holder.textMe.setText(String.valueOf(textChatMessage.text));
                }
                else
                {
                    holder.layMe.setVisibility(View.GONE);
                    holder.layOthers.setVisibility(View.VISIBLE);
                    holder.textOthers.setText(String.valueOf(textChatMessage.text));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class ViewHolder
    {
        public ViewHolder(View root)
        {
            try
            {
                textMe = (TextView) root.findViewById(R.id.txt_text_me);
                textOthers = (TextView) root.findViewById(R.id.txt_text_others);
                layMe = root.findViewById(R.id.lay_me);
                layOthers = root.findViewById(R.id.lay_others);

                // 设置列表项
                root.setTag(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();;
            }
        }

        /** 我发送的聊天内容 */
        public TextView textMe;

        /** 别人发送的聊天内容 */
        public TextView textOthers;
        /** 我发送的聊天内容的Container */
        public View layMe;
        /** 我发送的聊天内容的Container */
        public View layOthers;
    }
}
