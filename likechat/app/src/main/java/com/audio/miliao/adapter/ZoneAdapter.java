package com.audio.miliao.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.Zone;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.StringUtil;

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

    /** 缩略图点击监听器 */
    private OnThumbClickListener m_onThumbListener  = null;

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

    /**
     * 设置缩略图点击监听器
     * @param listener
     */
    public void setOnThumbClickListener(final OnThumbClickListener listener)
    {
        m_onThumbListener = listener;
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
            final Zone zone = (Zone) getItem(nPosition);
            if (zone != null)
            {
                holder.name.setText(zone.anchorName);
                holder.date.setText(StringUtil.formatDate(zone.date));
                holder.sign.setText(zone.anchorSign);
                holder.text.setText(zone.text);
                holder.watch.setText(zone.watch + m_parent.getString(R.string.txt_zone_watch));
                ImageLoaderUtil.displayListAvatarImageFromAsset(holder.avatar, zone.anchorAvatar);
                holder.pictures.setVisibility(zone.mediaType == Zone.MEDIA_PHOTO ? View.VISIBLE : View.GONE);
                ImageView vThumb;

                if (zone.mediaType == Zone.MEDIA_PHOTO)
                {
                    JSONArray jsonArray = new JSONArray((null == zone.thumbsUrl || "".equals(zone.thumbsUrl)) ? "[]" : zone.thumbsUrl);

                    final int nSize = jsonArray.length();
                    boolean bWrap = false;

                    // 加载缩略图
                    for (int i = 0, j = 0; i < Zone.PHOTOS_MAX; i++, j++)
                    {
                        vThumb = holder.listThumbs.get(i);

                        // 处理 4 张图片显示成2行2列的显示
                        if (nSize == 4 && i == 3 && !bWrap)
                        {
                            j--;
                            bWrap = true;
                        }

                        vThumb.setTag(j);
                        if (j < jsonArray.length())
                        {
                            String strThumbUrl = jsonArray.getString(j);
                            ImageLoaderUtil.displayListAvatarImageFromAsset(vThumb, strThumbUrl);
                            vThumb.setVisibility(View.VISIBLE);
                            vThumb.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    try
                                    {
                                        if (null != m_onThumbListener)
                                        {
                                            int nPos = (int)v.getTag();
                                            m_onThumbListener.onClick(zone, nPos, nSize);
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                }
                            });
                        }
                        else
                        {
                            vThumb.setImageResource(0);
                            vThumb.setVisibility(View.GONE);
                            vThumb.setOnClickListener(null);
                        }
                    }

                    setViewThumbHeightEquWidth(holder, jsonArray.length());
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
    private void setViewThumbHeightEquWidth(final ViewHolder holder, final int nCount)
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

            //获取按钮的布局
            GridLayout.LayoutParams para = (GridLayout.LayoutParams) holder.listThumbs.get(0).getLayoutParams();
            int nSpace = para.leftMargin;
            int width = m_nContentWidth - nSpace * 2;

            if (nCount == 1)
            {
               //holder.pictures.setColumnCount(1);
            }
            else if (nCount == 2 || nCount == 4)
            {
                width = (m_nContentWidth - nSpace * 2 * 2) / 2;
                //holder.pictures.setColumnCount(2);
            }
            else if (nCount > 2)
            {
                width = (m_nContentWidth - nSpace * 2 * 3) / 3;
                //holder.pictures.setColumnCount(3);
            }

            for (int i = 0; i < holder.listThumbs.size(); i++)
            {
                ImageView view = holder.listThumbs.get(i);
                setViewSize(view, width, width);
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

            setViewSize(view, m_nContentWidth, m_nContentWidth);
//            //获取按钮的布局
//            LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) view.getLayoutParams();
//            para.height = m_nContentWidth;
//            para.width  = m_nContentWidth;
//            view.setLayoutParams(para);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            GridLayout.LayoutParams para = (GridLayout.LayoutParams) view.getLayoutParams();
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
                pictures = (GridLayout) root.findViewById(R.id.lay_picture);
                name = (TextView) root.findViewById(R.id.txt_name);
                sign = (TextView) root.findViewById(R.id.txt_sign);
                date = (TextView) root.findViewById(R.id.txt_date);
                watch = (TextView) root.findViewById(R.id.txt_watch);
                text = (TextView) root.findViewById(R.id.txt_text);

                listThumbs = new ArrayList<>();
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb1));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb2));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb3));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb4));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb5));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb6));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb7));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb8));
                listThumbs.add((ImageView) root.findViewById(R.id.img_thumb9));

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
        /** 缩略图组容器 */
        public GridLayout pictures;
        /** 缩略图组 */
        public List<ImageView> listThumbs;
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
    /**
     * 缩略图点击监听器
     */
    public interface OnThumbClickListener
    {
        /**
         * 点击
         * @param zone
         * @param nPosition
         * @param nSize
         */
        public void onClick(Zone zone, final int nPosition, final int nSize);
    }
}
