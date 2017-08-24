package com.audio.miliao.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.fragment.BaseFootableFragment;
import com.audio.miliao.util.EntityUtil;
import com.audio.miliao.util.ImageLoaderUtil;
import com.netease.nim.uikit.miliao.vo.ActorVo;

import java.util.List;

/**
 * 好友列表
 */

public class FriendAdapter extends BaseFootableFragment.BaseUpdateAdapter<ActorVo>
{
    private Activity m_parent;
    private List<ActorVo> m_listActors;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;

    public FriendAdapter(Activity activity, List<ActorVo> listActors)
    {
        m_parent = activity;
        m_listActors = listActors;
    }

    public void updateData(List listActors)
    {
        m_listActors = listActors;
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
            return m_listActors.size();
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
            return m_listActors.get(position);
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
                convertView = View.inflate(m_parent, R.layout.item_friend, null);

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
            ActorVo actor = (ActorVo) getItem(nPosition);
            if (actor != null)
            {
                holder.name.setText(actor.getNickname());
                holder.sign.setText(actor.getSignature());
                holder.gender.setText(actor.getAge());
                //if (!m_bIsScrolling)
                {
                    ImageLoaderUtil.displayListAvatarImage(holder.avatar, actor.getIcon());
                }
                EntityUtil.setActorGenderDrawable(holder.gender, actor, true);
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
                sign = (TextView) root.findViewById(R.id.txt_sign);

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
        public TextView sign;
    }
}
