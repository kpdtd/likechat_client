package com.likechat.likechat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.TextChatMessage;

import java.util.List;

/**
 * 主播列表
 */

public class TextChatAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<TextChatMessage> m_listTextChatMessages;

    public TextChatAdapter(Activity activity, List<TextChatMessage> listTextChatMessages)
    {
        m_parent = activity;
        m_listTextChatMessages = listTextChatMessages;
    }

    public void updateData(List<TextChatMessage> listTextChatMessages)
    {
        m_listTextChatMessages = listTextChatMessages;
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

            // 设置项信息
            setItemInfo(position, holder);
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
            TextChatMessage textChatMessage = (TextChatMessage) getItem(nPosition);
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
