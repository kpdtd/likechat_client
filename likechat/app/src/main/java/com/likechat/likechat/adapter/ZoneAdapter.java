package com.likechat.likechat.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.Zone;
import com.likechat.likechat.util.ImageLoaderUtil;
import com.likechat.likechat.util.StringUtil;

import org.json.JSONArray;

import java.util.List;

/**
 * 主播动态列表
 */

public class ZoneAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<Zone> m_listZones;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;

    public ZoneAdapter(Activity activity, List<Zone> listZones)
    {
        m_parent = activity;
        m_listZones = listZones;
    }

    public void updateData(List<Zone> listZones)
    {
        m_listZones = listZones;
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
            return m_listZones.size();
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
            return m_listZones.get(position);
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
                convertView = View.inflate(m_parent, R.layout.item_zone, null);

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
            Zone zone = (Zone) getItem(nPosition);
            if (zone != null)
            {
                holder.name.setText(zone.anchorName);
                holder.date.setText(StringUtil.formatDate(zone.date));
                holder.sign.setText(zone.anchorSign);
                holder.text.setText(zone.text);
                holder.watch.setText(zone.viewCount + m_parent.getString(R.string.txt_zone_watch));
                ImageLoaderUtil.displayListAvatarImageFromAsset(holder.avatar, zone.anchorAvatar);
                JSONArray jsonArray = new JSONArray(zone.photosUrl);
                setViewHeightEquWidth(holder.content);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String strPhotoUrl = jsonArray.getString(i);
                    ImageLoaderUtil.displayListAvatarImageFromAsset(holder.content, strPhotoUrl);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置动态的图片内容的高度与宽度相等
     */
    private void setViewHeightEquWidth(View view)
    {
        try
        {
            DisplayMetrics metric = new DisplayMetrics();
            m_parent.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;   // 屏幕高度（像素）

            //获取按钮的布局
            LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) view.getLayoutParams();
            para.height = width;
            para.width  = width;
            view.setLayoutParams(para);
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
                content = (ImageView) root.findViewById(R.id.img_content);
                name = (TextView) root.findViewById(R.id.txt_name);
                sign = (TextView) root.findViewById(R.id.txt_sign);
                date = (TextView) root.findViewById(R.id.txt_date);
                watch = (TextView) root.findViewById(R.id.txt_watch);
                text = (TextView) root.findViewById(R.id.txt_text);

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
        /** 图片内容 */
        public ImageView content;
        /** 名字 */
        public TextView name;
        /** 个性签名 */
        public TextView sign;
        /** 时间 */
        public TextView date;
        /** 文本 */
        public TextView text;
        /** 观看人数 */
        public TextView watch;
    }
}
