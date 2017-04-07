package com.likechat.likechat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.ChatMessage;
import com.likechat.likechat.util.ImageLoaderUtil;
import com.likechat.likechat.util.StringUtil;

import java.util.List;

/**
 * 消息列表
 */
public class MessageAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<ChatMessage> m_listTextChatMessages;

    public MessageAdapter(Activity activity, List<ChatMessage> listTextChatMessages)
    {
        m_parent = activity;
        m_listTextChatMessages = listTextChatMessages;
    }

    public void updateData(List<ChatMessage> listTextChatMessages)
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
                convertView = View.inflate(m_parent, R.layout.item_message, null);

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
            ChatMessage message = (ChatMessage) getItem(nPosition);
            //holder.imgAvatar.setImageResource(R.mipmap.avatar1);
            if (message != null)
            {
                holder.textDate.setText(StringUtil.formatDate(message.date));
                holder.textSummary.setText(message.text);
                if (message.from != null)
                {
                    holder.textName.setText(message.from.name);
                    ImageLoaderUtil.displayListAvatarImageFromAsset(holder.imgAvatar, message.from.avatar);
                }
                else
                {
                    holder.textName.setText("");
                    holder.imgAvatar.setImageResource(R.mipmap.ic_system_message);
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
                imgAvatar = (ImageView) root.findViewById(R.id.img_avatar);
                textName = (TextView) root.findViewById(R.id.txt_name);
                textDate = (TextView) root.findViewById(R.id.txt_date);
                textSummary = (TextView) root.findViewById(R.id.txt_message_summary);
                viewUnread = root.findViewById(R.id.txt_unread);

                // 设置列表项
                root.setTag(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();;
            }
        }

        /** 头像 */
        public ImageView imgAvatar;
        /** 名字 */
        public TextView textName;
        /** 时间 */
        public TextView textDate;
        /** 消息摘要 */
        public TextView textSummary;
        /** 已读未读 */
        public View viewUnread;
    }
}
