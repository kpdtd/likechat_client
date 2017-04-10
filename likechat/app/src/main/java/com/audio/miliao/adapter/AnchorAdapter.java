package com.audio.miliao.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.User;
import com.audio.miliao.util.EntityUtil;
import com.audio.miliao.util.ImageLoaderUtil;

import java.util.List;

/**
 * 主播列表
 */

public class AnchorAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<User> m_listUsers;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;

    public AnchorAdapter(Activity activity, List<User> listUsers)
    {
        m_parent = activity;
        m_listUsers = listUsers;
    }

    public void updateData(List<User> listUsers)
    {
        m_listUsers = listUsers;
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
            return m_listUsers.size();
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
            return m_listUsers.get(position);
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
                convertView = View.inflate(m_parent, R.layout.item_achor, null);

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
            User user = (User) getItem(nPosition);
            if (user != null)
            {
                holder.name.setText(user.name);
                holder.intro.setText(user.intro);
                holder.gender.setText(String.valueOf(user.age));
                //holder.avatar.setImageResource(user.avatar_res);
                if (!m_bIsScrolling)
                {
                    ImageLoaderUtil.displayListAvatarImageFromAsset(holder.avatar, user.avatar);
                }
                EntityUtil.setAnchorGenderDrawable(holder.gender, user, false);
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
                avatar = (ImageView) root.findViewById(R.id.img_avatar);
                gender = (TextView) root.findViewById(R.id.txt_gender);
                name = (TextView) root.findViewById(R.id.txt_name);
                intro = (TextView) root.findViewById(R.id.txt_intro);

                // 设置列表项
                root.setTag(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();;
            }
        }

        /** 头像 */
        public ImageView avatar;
        /** 性别 */
        public TextView gender;
        /** 名字 */
        public TextView name;
        /** 介绍 */
        public TextView intro;
    }
}
