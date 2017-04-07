package com.likechat.likechat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.CallHistory;
import com.likechat.likechat.util.StringUtil;

import java.util.List;

/**
 * 通话记录
 */
public class CallHistoryAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<CallHistory> m_listCallHistories;

    public CallHistoryAdapter(Activity activity, List<CallHistory> callHistories)
    {
        m_parent = activity;
        m_listCallHistories = callHistories;
    }

    public void updateData(List<CallHistory> callHistories)
    {
        m_listCallHistories = callHistories;
    }

    @Override
    public int getCount()
    {
        try
        {
            return m_listCallHistories.size();
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
            return m_listCallHistories.get(position);
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
                convertView = View.inflate(m_parent, R.layout.item_call_history, null);

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
            CallHistory callHistory = (CallHistory) getItem(nPosition);
            if (callHistory != null)
            {
                holder.textStart.setText(StringUtil.formatDate(callHistory.startTime));

                if (AppData.isCurUser(callHistory.from))
                {
                    // 呼出
                    String strTalkTime = "";
                    if (callHistory.talkTime == 0)
                    {
                        strTalkTime = "00:00";
                    }
                    else
                    {
                        strTalkTime = StringUtil.formatTime(callHistory.talkTime);
                    }

                    holder.textState.setText(strTalkTime);
                    holder.textName.setText(callHistory.to.name);
                    holder.imgAvatar.setImageResource(callHistory.to.avatar_res);
                }
                else
                {
                    // 呼入
                    String strTalkTime = "";
                    if (callHistory.talkTime == 0)
                    {
                        strTalkTime = "未接来电";
                    }
                    else
                    {
                        strTalkTime = StringUtil.formatTime(callHistory.talkTime);
                    }

                    holder.textState.setText(strTalkTime);
                    holder.textName.setText(callHistory.from.name);
                    holder.imgAvatar.setImageResource(callHistory.from.avatar_res);
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
                textStart = (TextView) root.findViewById(R.id.txt_start);
                textState = (TextView) root.findViewById(R.id.txt_call_state);

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
        public TextView textStart;
        /** 通话状态（呼入、呼出、未接来电） */
        public TextView textState;
    }
}
