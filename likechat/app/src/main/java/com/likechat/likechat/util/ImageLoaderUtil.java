package com.likechat.likechat.util;

import android.content.Context;
import android.widget.ImageView;

import com.likechat.likechat.R;
import com.likechat.likechat.theApp;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 加载图片工具类<br/>
 * 使用Android-Universal-Image-Loader<br/>
 * https://github.com/nostra13/Android-Universal-Image-Loader
 */
public class ImageLoaderUtil
{
    private static ImageLoader m_imageLoader;

    /** 列表中的图片（没有圆角） */
    private static DisplayImageOptions m_optionsListItem;
    /** 单独显示的头像 */
    private static DisplayImageOptions m_optionsAvatar;
    /** 用于绘制圆角 */
    private static DisplayImageOptions m_optionsListAvatar;

    private static void init()
    {
        try
        {
            if (m_imageLoader == null)
            {
                Context context = theApp.CONTEXT;
                // Configuration(ImageLoaderConfiguration) 是相对于整个应用的配置
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                        //.memoryCacheExtraOptions(360, 360) // default = device screen dimensions
                        .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
                        .build();

                m_imageLoader = ImageLoader.getInstance();
                m_imageLoader.init(config);

                m_optionsListItem = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.ic_user) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.mipmap.ic_user) // 设置图片加载/解码过程中错误时候显示的图片
                        .delayBeforeLoading(300)
                        //.showStubImage(R.mipmap.ic_user) // 设置图片在下载期间显示的图片
                        //.cacheInMemory()
                        //.cacheOnDisc()
                        // .displayer(new RoundedBitmapDisplayer(20))
                        .build();

                m_optionsAvatar = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.ic_user)
                        // 设置图片在下载期间显示的图片
                        .showImageOnFail(R.mipmap.ic_user)
                        // 设置图片Uri为空或是错误的时候显示的图片
                        //.showStubImage(R.mipmap.ic_user)
                        // 设置图片加载/解码过程中错误时候显示的图片
                        //.cacheInMemory()
                        //.cacheOnDisc()
                        // 圆角显示
                        .displayer(new RoundedBitmapDisplayer(10))
                        .build();

                m_optionsListAvatar = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.ic_user)
                        // 设置图片在下载期间显示的图片
                        .showImageOnFail(R.mipmap.ic_user)
                        // 设置图片Uri为空或是错误的时候显示的图片
                        //.showStubImage(R.mipmap.ic_user)
                        // 设置图片加载/解码过程中错误时候显示的图片
                        //.cacheInMemory()
                        //.delayBeforeLoading(300)
                        //.cacheOnDisc()
                        //.displayer(new RoundedBitmapDisplayer(14))
                        .build();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 在列表中显示头像
     * @param imageView
     * @param url
     */
    public static void displayListAvatarImage(ImageView imageView, String url)
    {
        try
        {
            if (imageView == null)
            {
                return;
            }

            init();

            String strUrl = (url == null ? "" : url);
            m_imageLoader.displayImage(strUrl, imageView, m_optionsListAvatar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 在列表中显示头像<br/>
     * 加载asset中的图片
     * @param imageView
     * @param imageName asset 中图片文件名
     */
    public static void displayListAvatarImageFromAsset(ImageView imageView, String imageName)
    {
        try
        {
            if (imageView == null)
            {
                return;
            }

            init();

            String strUrl = "assets://" + imageName;
            m_imageLoader.displayImage(strUrl, imageView, m_optionsListAvatar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
