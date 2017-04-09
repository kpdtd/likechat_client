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
import com.likechat.likechat.util.UIUtil;

import org.json.JSONArray;

import java.util.ArrayList;
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
    /** 动态内容的宽度（宽度、高度都与屏幕宽度一致） */
    private int m_nContentWidth = 0;

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

    public List<Zone> getZones()
    {
        return m_listZones;
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
                String strWatch = zone.watch + m_parent.getString(R.string.txt_zone_watch);
                holder.watch.setText(strWatch);
                ImageLoaderUtil.displayListAvatarImageFromAsset(holder.avatar, zone.anchorAvatar);
                JSONArray jsonArray = new JSONArray(zone.photosUrl);
                setViewHeightEquWidth(holder.content);
                if (!m_bIsScrolling)
                {
                    List<ImageView> visiableViews = setZoneContentVisibility(jsonArray.length(), holder);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String strPhotoUrl = jsonArray.getString(i);
                        ImageView imageView = visiableViews.get(i);
                        ImageLoaderUtil.displayListAvatarImageFromAsset(imageView, strPhotoUrl);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置动态的图片内容的高度与宽度都与屏幕宽度相等
     */
    private void setViewHeightEquWidth(View view)
    {
        try
        {
            if (m_nContentWidth == 0)
            {
                DisplayMetrics metric = new DisplayMetrics();
                m_parent.getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width = metric.widthPixels;     // 屏幕宽度（像素）
                //int height = metric.heightPixels;   // 屏幕高度（像素）

                m_nContentWidth = width;
            }

            //setViewSize(view, m_nContentWidth, m_nContentWidth);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置图片内容控件的可见性
     * @param visiableCount 几项可见
     */
    private List<ImageView> setZoneContentVisibility(int visiableCount, ViewHolder holder)
    {
        List<ImageView> visiableView = new ArrayList<>();
        try
        {
            List<ImageView> allContentView = new ArrayList<>();
            allContentView.add(holder.imgContent1);
            allContentView.add(holder.imgContent2);
            allContentView.add(holder.imgContent3);
            allContentView.add(holder.imgContent4);
            allContentView.add(holder.imgContent5);
            allContentView.add(holder.imgContent6);
            allContentView.add(holder.imgContent7);
            allContentView.add(holder.imgContent8);
            allContentView.add(holder.imgContent9);

            // 中间第二张图片与第一第三张图片的边距
            int width;
            if (visiableCount == 1)
            {
                width = m_nContentWidth;
            }
            else
            {
                int margin = UIUtil.dip2px(m_parent, 5) * 2;
                width = (m_nContentWidth - margin) / 3;
            }

            for (int i = 0; i < allContentView.size(); i++)
            {
                View view = allContentView.get(i);
                view.setVisibility(View.INVISIBLE);
                setViewSize(view, width, width);
            }

            if (visiableCount <= 0)
            {
                return visiableView;
            }

            int[] visibleIndex = null;
            if (visiableCount == 1)
            {
                visibleIndex = new int[]{0};
            }
            else if (visiableCount <= 2)
            {
                visibleIndex = new int[]{0, 1};
            }
            else if (visiableCount <= 3)
            {
                visibleIndex = new int[]{0, 1, 2};
            }
            else if (visiableCount == 4)
            {
                visibleIndex = new int[]{0, 1, 3, 4};
            }
            else if (visiableCount <= 5)
            {
                visibleIndex = new int[]{0, 1, 2, 3, 4};
            }
            else if (visiableCount <= 6)
            {
                visibleIndex = new int[]{0, 1, 2, 3, 4, 5};
            }
            else if (visiableCount <= 7)
            {
                visibleIndex = new int[]{0, 1, 2, 3, 4, 5, 6};
            }
            else if (visiableCount <= 8)
            {
                visibleIndex = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            }
            else if (visiableCount <= 9)
            {
                visibleIndex = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
            }

            for (int index : visibleIndex)
            {
                ImageView view = allContentView.get(index);
                view.setVisibility(View.VISIBLE);
                visiableView.add(view);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return visiableView;
    }

    /**
     * 设置控件大小
     * @param view
     * @param width
     * @param height
     */
    private void setViewSize(View view, int width, int height)
    {
        try
        {
            //获取按钮的布局
            LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) view.getLayoutParams();
            para.height = height;
            para.width  = width;
            view.setLayoutParams(para);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class ViewHolder
    {
        ViewHolder(View root)
        {
            try
            {
                avatar = (ImageView) root.findViewById(R.id.img_avatar);
                content = (ViewGroup) root.findViewById(R.id.lay_content);
                content1 = (ViewGroup) root.findViewById(R.id.lay_content1);
                content2 = (ViewGroup) root.findViewById(R.id.lay_content2);
                content3 = (ViewGroup) root.findViewById(R.id.lay_content3);
                imgContent1 = (ImageView) root.findViewById(R.id.img_content1);
                imgContent2 = (ImageView) root.findViewById(R.id.img_content2);
                imgContent3 = (ImageView) root.findViewById(R.id.img_content3);
                imgContent4 = (ImageView) root.findViewById(R.id.img_content4);
                imgContent5 = (ImageView) root.findViewById(R.id.img_content5);
                imgContent6 = (ImageView) root.findViewById(R.id.img_content6);
                imgContent7 = (ImageView) root.findViewById(R.id.img_content7);
                imgContent8 = (ImageView) root.findViewById(R.id.img_content8);
                imgContent9 = (ImageView) root.findViewById(R.id.img_content9);
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
                e.printStackTrace();
            }
        }

        /** 头像 */
        ImageView avatar;
        /** 图片内容 */
        ViewGroup content;
        /** 图片内容1 */
        ViewGroup content1;
        /** 图片内容2 */
        ViewGroup content2;
        /** 图片内容3 */
        ViewGroup content3;
        /** 图片内容1 */
        ImageView imgContent1;
        /** 图片内容2 */
        ImageView imgContent2;
        /** 图片内容3 */
        ImageView imgContent3;
        /** 图片内容4 */
        ImageView imgContent4;
        /** 图片内容5 */
        ImageView imgContent5;
        /** 图片内容6 */
        ImageView imgContent6;
        /** 图片内容7 */
        ImageView imgContent7;
        /** 图片内容8 */
        ImageView imgContent8;
        /** 图片内容9 */
        ImageView imgContent9;
        /** 名字 */
        TextView name;
        /** 个性签名 */
        TextView sign;
        /** 时间 */
        TextView date;
        /** 文本 */
        TextView text;
        /** 观看人数 */
        TextView watch;
    }
}
