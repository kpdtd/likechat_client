package com.audio.miliao.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.audio.miliao.R;
import com.audio.miliao.photoview.PhotoView;
import com.audio.miliao.photoview.PhotoViewAttacher.OnViewTapListener;
import com.audio.miliao.theApp;
import com.netease.nim.uikit.miliao.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter
{
    private LayoutInflater m_inflater = null;
    private Context m_context = null;
    //private DisplayImageOptions m_diOptions = null;
    private List<String> m_lstImage = new ArrayList<String>();
    private java.util.Map<String, View> m_mapView = new java.util.HashMap<String, View>();
    private OnViewTapListener m_tapListener = null;

    public ImagePagerAdapter(Context context, List<String> lstImage)
    {
        try
        {
            m_context = context;
            m_lstImage = lstImage;
            m_inflater = ((Activity) context).getLayoutInflater();

//            m_diOptions = new DisplayImageOptions.Builder()
//                    .showImageForEmptyUri(R.mipmap.loader_empty)
//                    .showImageOnFail(R.mipmap.loader_error)
//                    .resetViewBeforeLoading(true)
//                    .cacheOnDisc(true)
//                    .imageScaleType(ImageScaleType.EXACTLY)
//                    .bitmapConfig(Bitmap.Config.ARGB_8888)
//                    .displayer(new FadeInBitmapDisplayer(300))
//                    .build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setOnViewTapListener(OnViewTapListener tapListener)
    {
        m_tapListener = tapListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        try
        {
            ((ViewPager) container).removeView((View) object);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void finishUpdate(View container) {}

    @Override
    public int getCount() {
        return m_lstImage.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position)
    {
        try
        {
            // 多张图片才会循环
            if (m_lstImage.size() > 1)
            {
                if (position < 1)
                {
                    position = m_lstImage.size() - 2;
                }
                else if (position > m_lstImage.size() - 2)
                {
                    position = 1;
                }
            }

            if (m_mapView.containsKey(position))
            {
                return m_mapView.get(position);
            }

            View imageLayout = m_inflater.inflate(R.layout.item_pager_image, view, false);
            m_mapView.put("" + position, imageLayout);

            PhotoView photoView = (PhotoView) imageLayout.findViewById(R.id.pv_image);
            final ProgressBar pbLoading = (ProgressBar) imageLayout.findViewById(R.id.pb_loading);
            ImageLoaderUtil.displayListPhotoImage(photoView, m_lstImage.get(position), new ImageLoadingListener()
            {
                @Override
                public void onLoadingStarted(String strUrl, View view)
                {
                    pbLoading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String srUrl, View view, Bitmap loadedImage)
                {
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingFailed(String strUrl, View view, FailReason failReason)
                {
                    String strMsg = "";
                    switch (failReason.getType())
                    {
                        case IO_ERROR:
                            // Input/Output error
                            strMsg = m_context.getString(R.string.img_loader_io_error);
                            break;
                        case DECODING_ERROR:
                            // Image can't be decoded
                            strMsg = m_context.getString(R.string.img_loader_decoding_error);
                            break;
                        case NETWORK_DENIED:
                            // Downloads are denied
                            strMsg = m_context.getString(R.string.img_loader_network_denied);
                            break;
                        case OUT_OF_MEMORY:
                            // Out Of Memory error
                            strMsg = m_context.getString(R.string.img_loader_out_of_memory);
                            break;
                        case UNKNOWN:
                            // Unknown error
                            strMsg = m_context.getString(R.string.img_loader_unknown_error);
                            break;
                    }

                    //android.widget.Toast.makeText(m_context, strMsg, android.widget.Toast.LENGTH_SHORT).show();
                    theApp.showToast(strMsg);

                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1)
                {
                }
            });

            photoView.setOnViewTapListener(m_tapListener);

            ((ViewPager) view).addView(imageLayout, 0);
            return imageLayout;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) { }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) { }
}