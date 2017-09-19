package com.audio.miliao.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.Zone;
import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.ActorDynamicVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 主播动态列表
 */
public class ActorDynamicAdapter extends BaseAdapter
{
    private Activity m_parent;
    private List<ActorDynamicVo> m_listZones;
    /** 列表是否处于滑动状态 */
    private boolean m_bIsScrolling = false;
    /** 是否显示删除按钮 */
    private boolean m_bShowDelete = false;
    /** 动态内容的宽度（宽度、高度都与屏幕宽度一致） */
    private int m_nContentWidth = 0;

    /** 点击监听器 */
    private OnClickListener m_onClickListener  = null;

    public ActorDynamicAdapter(Activity activity, List<ActorDynamicVo> listZones)
    {
        m_parent = activity;
        m_listZones = listZones;
    }

    public void updateData(List<ActorDynamicVo> listZones)
    {
        m_listZones = listZones;
    }

    public void setScrolling(boolean bIsScrolling)
    {
        m_bIsScrolling = bIsScrolling;
    }

    public void setShowDelete(boolean bShowDelete)
    {
        m_bShowDelete = bShowDelete;
    }

    public List<ActorDynamicVo> getZones()
    {
        return m_listZones;
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
            final ActorDynamicVo actorDynamicVo = (ActorDynamicVo) getItem(nPosition);
            if (actorDynamicVo != null)
            {
                holder.name.setText(actorDynamicVo.getNickname());
                holder.date.setText(actorDynamicVo.getCreateTime());
                holder.sign.setText(actorDynamicVo.getSignature());
                holder.text.setText(actorDynamicVo.getContent());
                holder.watch.setText(actorDynamicVo.getPageView() + m_parent.getString(R.string.txt_zone_watch));
                ImageLoaderUtil.displayListAvatarImage(holder.avatar, actorDynamicVo.getImgUrl());
                holder.avatar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (null != m_onClickListener)
                        {
                            m_onClickListener.onAvatarClick(actorDynamicVo);
                        }
                    }
                });

                ImageView vThumb;

                int visibility = (m_bShowDelete ? View.VISIBLE : View.GONE);
                holder.delete.setVisibility(visibility);

                if (actorDynamicVo.getDynamicType() == ActorDynamicVo.MEDIA_PHOTO)
                {
                    List<String> dynamicUrlList = actorDynamicVo.getDynamicUrl();
                    //JSONArray jsonArray = new JSONArray((null == zone.thumbsUrl || "".equals(zone.thumbsUrl)) ? "[]" : zone.thumbsUrl);

                    final int nSize = dynamicUrlList.size();
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
                        if (j < nSize)
                        {
                            String strThumbUrl = dynamicUrlList.get(j);
                            ImageLoaderUtil.displayListImage(vThumb, strThumbUrl);
                            vThumb.setVisibility(View.VISIBLE);
                            vThumb.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    try
                                    {
                                        if (null != m_onClickListener)
                                        {
                                            int nPos = (int)v.getTag();
                                            m_onClickListener.onThumbClick(actorDynamicVo, nPos, nSize);
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

                    setViewThumbHeightEquWidth(holder, nSize);
                }
                else if (actorDynamicVo.getDynamicType() == ActorDynamicVo.MEDIA_VOICE)
                {
                    int total = actorDynamicVo.getVoiceSec();
                    int hour = total / 3600;
                    int minute = (total - hour * 3600) / 60;
                    int second = total - hour * 3600 - minute * 60;

                    String strVoice = "";

                    if (hour > 0)
                    {
                        strVoice = "" + hour + "h" + String.format("%02d", minute) + "'" + String.format("%02d", second) + "\"";
                    }
                    else if (minute > 0)
                    {
                        strVoice = "" + minute + "'" + String.format("%02d", second) + "\"";
                    }
                    else
                    {
                        strVoice = "" + second + "\"";
                    }

                    holder.voiceLen.setText(strVoice);
                    setViewVoiceWidth(holder.voiceLen);

                    holder.voiceLen.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            try
                            {
                                if (null != m_onClickListener)
                                {
                                    m_onClickListener.onVoiceClick(actorDynamicVo);
                                }
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    });
                }
                else if (actorDynamicVo.getDynamicType() == ActorDynamicVo.MEDIA_VIDEO)
                {
                    setViewVideoHeightEquWidth(holder.videoThumb);
                    setViewVideoHeightEquWidth(holder.videoNeedPay);
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    String strPrice = String.format(m_parent.getString(R.string.txt_zone_video_price), df.format(actorDynamicVo.getPrice()));
                    strPrice = "<font color='#d908ed'>" + strPrice + "</font>";
                    String strPay = String.format(m_parent.getString(R.string.txt_zone_video_pay), strPrice);
                    holder.videoNeedPay.setText(Html.fromHtml(strPay));
                    ImageLoaderUtil.displayListImage(holder.videoThumb, actorDynamicVo.getVideoFaceUrl());
                    holder.videoThumb.setVisibility(View.VISIBLE);
                    holder.videoNeedPay.setVisibility((actorDynamicVo.getPrice() > 0) ? View.VISIBLE : View.GONE);
                    holder.videoLoading.setVisibility(View.GONE);

                    holder.video.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            try
                            {
                                if (null != m_onClickListener)
                                {
                                    m_onClickListener.onVideoClick(actorDynamicVo);
                                }
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    });
                }
                //android.util.Log.e("mediatype", "" + zone.getDynamicType());

                holder.pictures.setVisibility(actorDynamicVo.getDynamicType() == ActorDynamicVo.MEDIA_PHOTO ? View.VISIBLE : View.GONE);
                holder.voiceLen.setVisibility(actorDynamicVo.getDynamicType() == ActorDynamicVo.MEDIA_VOICE ? View.VISIBLE : View.GONE);
                holder.video.setVisibility(actorDynamicVo.getDynamicType() == ActorDynamicVo.MEDIA_VIDEO ? View.VISIBLE : View.GONE);
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

    /**
     * 设置动态的声音内容的宽度
     */
    private void setViewVoiceWidth(final View view)
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
            LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) view.getLayoutParams();
            int nSpace = para.leftMargin;

            // 点三分之一的宽度
            int width = (int)((m_nContentWidth - nSpace * 2) * 0.5);
            para.width  = width;
            view.setLayoutParams(para);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置动态的声音内容的宽度
     */
    private void setViewVideoHeightEquWidth(final View view)
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
            FrameLayout.LayoutParams para = (FrameLayout.LayoutParams) view.getLayoutParams();
            int nSpace = para.leftMargin;

            int width = m_nContentWidth - nSpace * 2;
            para.width  = width;
            para.height = width;
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
                voiceLen = (TextView) root.findViewById(R.id.txt_voice);
                video = (FrameLayout) root.findViewById(R.id.lay_video);
                videoThumb = (ImageView) root.findViewById(R.id.img_video);
                videoLoading = (ProgressBar) root.findViewById(R.id.pb_loading);
                videoNeedPay = (TextView) root.findViewById(R.id.txt_video);
                text = (TextView) root.findViewById(R.id.txt_text);
                delete = (ImageView) root.findViewById(R.id.img_delete_zone);

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
        /** 声音长度 */
        public TextView voiceLen;
        /** 视频组容器 */
        public FrameLayout video;
        /** 视频首页缩略图 */
        public ImageView videoThumb;
        /** 视频等待图标 */
        public ProgressBar videoLoading;
        /** 视频付款 */
        public TextView videoNeedPay;
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
        /** 删除 */
        private ImageView delete;
    }
    /**
     * 点击监听器
     */
    public interface OnClickListener
    {
        /**
         * 点击缩略图
         * @param actorDynamicVo
         * @param nPosition
         * @param nSize
         */
        void onThumbClick(ActorDynamicVo actorDynamicVo, final int nPosition, final int nSize);

        /**
         * 点击声音
         * @param actorDynamicVo
         */
        void onVoiceClick(ActorDynamicVo actorDynamicVo);

        /**
         * 点击视频
         * @param actorDynamicVo
         */
        void onVideoClick(ActorDynamicVo actorDynamicVo);

        /**
         * 点击头像
         * @param actorDynamicVo
         */
        void onAvatarClick(ActorDynamicVo actorDynamicVo);
    }
}
