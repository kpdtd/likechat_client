package com.audio.miliao.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.audio.miliao.R;
import com.netease.nim.uikit.miliao.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器列表
 */

public class PhotoSelectorAdapter extends BaseAdapter
{
    private Activity m_parent;
    /** 图片列表, 第0张为拍照按钮 */
    private List<String> m_listPhotos;
    private List<String> m_listSelect;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;
    /** 动态内容的宽度（宽度、高度都与屏幕宽度一致） */
    private int m_nContentWidth = 0;
    /** 布局参数 */
    private GridView.LayoutParams m_para;

    /** 点击监听器 */
    private OnClickListener m_onClickListener  = null;

    public PhotoSelectorAdapter(Activity activity, List<String> listPhotos, List<String> listSelPhotos)
    {
        m_parent = activity;
        m_listPhotos = listPhotos;
        m_listSelect = listSelPhotos;

        if (m_listSelect == null)
        {
            m_listSelect = new ArrayList<>();
        }
    }

    public void updateData(List<String> listPhotos)
    {
        m_listPhotos = listPhotos;
    }

    public void setScrolling(boolean bIsScrolling)
    {
        m_bIsScrolling = bIsScrolling;
    }

    public List<String> getPhotos()
    {
        return m_listPhotos;
    }

    public List<String> getSelectedPhotos()
    {
        return m_listSelect;
    }

    /**
     * 设置点击监听器
     * @param listener
     */
    public void setOnClickListener(final OnClickListener listener)
    {
        m_onClickListener = listener;
    }

    @Override
    public int getCount()
    {
        try
        {
            return m_listPhotos.size() + 1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public Object getItem(int position)
    {
        try
        {
            return m_listPhotos.get(position);
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        try
        {
            int nLay = position == 0 ? R.layout.item_photo_camera : R.layout.item_photo_normal;

            if (position == 0)
            {
                // 加载列表项
                convertView = View.inflate(m_parent, nLay, null);
                convertView.setTag(null);
            }
            else
            {
                ViewHolder holder = null;
                if (convertView != null)
                {
                    // 支持器
                    holder = (ViewHolder) convertView.getTag();

                    if (holder == null)
                    {
                        // 加载列表项
                        convertView = View.inflate(m_parent, nLay, null);

                        // 支持器
                        holder = new ViewHolder(convertView, position);
                    }
                }
                else
                {
                    // 加载列表项
                    convertView = View.inflate(m_parent, nLay, null);

                    // 支持器
                    holder = new ViewHolder(convertView, position);
                }

                // 设置项信息
                setItemInfo(position, holder);
            }

            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        if (null != m_onClickListener)
                        {
                            if (position > 0)
                            {
                                final String strPath = m_listPhotos.get(position - 1);
                                m_onClickListener.onPhotoClick(strPath);
                            }
                            else
                            {
                                m_onClickListener.onCameraClick();
                            }
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
            });

            setViewHeightEquWidth(convertView);
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
            if (nPosition > 0 && m_listPhotos.size() > 0)
            {
                final String strCurr = holder.photo.getTag() == null ? "" : holder.photo.getTag().toString();
                final String strPath = m_listPhotos.get(nPosition - 1);
                if (!strCurr.equals(strPath))
                {
                    ImageLoaderUtil.displayListPhotoImage(holder.photo, strPath, null);
                }

                boolean bSel = m_listSelect.contains(strPath);
                int nChecked = bSel ? R.drawable.ic_photo_checked : R.drawable.ic_photo_normal;
                holder.mask.setVisibility(bSel ? View.VISIBLE : View.GONE);
                holder.check.setImageResource(nChecked);
                holder.check.setTag(nPosition);
                holder.check.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            int nPos = Integer.valueOf(v.getTag().toString());
                            if (nPos > 0)
                            {
                                final String strPath = m_listPhotos.get(nPos - 1);
                                boolean bSel = m_listSelect.contains(strPath);

                                if (bSel)
                                {
                                    m_listSelect.remove(strPath);
                                }
                                else
                                {
                                    m_listSelect.add(strPath);
                                }
                                notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                        }
                    }
                });
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

                View vNormal = View.inflate(m_parent, R.layout.item_photo_normal, null);
                View vChecked = vNormal.findViewById(R.id.img_check);

                FrameLayout.LayoutParams para = (FrameLayout.LayoutParams) vChecked.getLayoutParams();
                int nSpace = para.leftMargin / 3;

                // 点三分之一的宽度
                width = (width - nSpace * 3) / 3;

                m_nContentWidth = width;

                m_para = new GridView.LayoutParams(m_nContentWidth, m_nContentWidth);
            }

            view.setLayoutParams(m_para);
            setViewSize(view, m_nContentWidth, m_nContentWidth);
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
            GridView.LayoutParams para = (GridView.LayoutParams) view.getLayoutParams();
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
        ViewHolder(View root, final int nPosition)
        {
            try
            {
                if (nPosition > 0)
                {
                    photo = (ImageView) root.findViewById(R.id.img_photo);
                    mask = root.findViewById(R.id.img_mask);
                    check = (ImageView) root.findViewById(R.id.img_check);
                }

                // 设置列表项
                root.setTag(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /** 图片 */
        public ImageView photo;
        /** 图片内容 */
        public View mask;
        /** 图片内容 */
        public ImageView check;
    }
    /**
     * 点击监听器
     */
    public interface OnClickListener
    {
        /**
         * 点击拍照
         */
        public void onCameraClick();

        /**
         * 点击图片
         * @param strPath
         */
        public void onPhotoClick(final String strPath);
    }
}
