package com.audio.miliao.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.CallHistory;
import com.netease.nim.uikit.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.StringUtil;

import java.util.List;

/**
 * 通话记录
 */
public class CallHistoryAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<CallHistory> m_listCallHistories;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;

    public CallHistoryAdapter(Activity activity, List<CallHistory> callHistories)
    {
        m_parent = activity;
        m_listCallHistories = callHistories;
    }

    public void updateData(List<CallHistory> callHistories)
    {
        m_listCallHistories = callHistories;
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
                        strTalkTime = m_parent.getString(R.string.txt_call_history_call_out)
                                + " " + StringUtil.formatTime(callHistory.talkTime);
                    }

                    holder.textState.setText(strTalkTime);
                    holder.textName.setText(callHistory.to.getNickname());
                    //holder.imgAvatar.setImageResource(callHistory.to.avatar_res);

                    if (!m_bIsScrolling)
                    {
                        ImageLoaderUtil.displayListAvatarImageFromAsset(holder.imgAvatar, callHistory.to.getIcon());
                    }
                }
                else
                {
                    // 呼入
                    String strTalkTime = "";
                    if (callHistory.talkTime == 0)
                    {
                        strTalkTime = m_parent.getString(R.string.txt_call_history_miss);
                    }
                    else
                    {
                        strTalkTime = m_parent.getString(R.string.txt_call_history_call_in)
                                + " " + StringUtil.formatTime(callHistory.talkTime);
                    }

                    holder.textState.setText(strTalkTime);
                    holder.textName.setText(callHistory.from.getNickname());
                    //holder.imgAvatar.setImageResource(callHistory.from.avatar_res);

                    if (!m_bIsScrolling)
                    {
                        ImageLoaderUtil.displayListAvatarImageFromAsset(holder.imgAvatar, callHistory.from.getIcon());
                    }
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
