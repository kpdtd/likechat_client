package com.audio.miliao.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.library.util.DateUtil;
import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.MessageVo;
import com.audio.miliao.R;

import java.util.List;

/**
 * 消息列表
 */
public class MessageAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<MessageVo> m_listTextChatMessages;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;

    public MessageAdapter(Activity activity, List<MessageVo> listTextChatMessages)
    {
        m_parent = activity;
        m_listTextChatMessages = listTextChatMessages;
    }

    public void updateData(List<MessageVo> listTextChatMessages)
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
            MessageVo message = (MessageVo) getItem(nPosition);
            if (message != null)
            {
                holder.textName.setText(message.getNickName());
                holder.textSummary.setText(message.getMessage());
                holder.textDate.setText(DateUtil.formatDate(message.getMdate()));
                ImageLoaderUtil.displayListAvatarImage(holder.imgAvatar, message.getIcon());
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
