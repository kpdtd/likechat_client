package com.likechat.likechat.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.Anchor;
import com.likechat.likechat.util.UIUtil;

import java.util.List;

/**
 * 主播列表
 */

public class AnchorAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<Anchor> m_listAnchors;

    public AnchorAdapter(Activity activity, List<Anchor> listAnchors)
    {
        m_parent = activity;
        m_listAnchors = listAnchors;
    }

    public void updateData(List<Anchor> listAnchors)
    {
        m_listAnchors = listAnchors;
    }

    @Override
    public int getCount()
    {
        try
        {
            return m_listAnchors.size();
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
            return m_listAnchors.get(position);
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
            Anchor anchor = (Anchor) getItem(nPosition);
            if (anchor != null)
            {
                holder.name.setText(anchor.name);
                holder.intro.setText(anchor.intro);
                holder.gender.setText(String.valueOf(anchor.age));
                holder.avatar.setImageResource(anchor.avatar_res);
                if (anchor.gender.equalsIgnoreCase("female"))
                {
                    Drawable female = m_parent.getResources().getDrawable(R.drawable.ic_female_normal);
                    int width = UIUtil.dip2px(m_parent, 15);
                    UIUtil.setCompoundDrawables(holder.gender, female, 0, 0, 0, width, width);
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
