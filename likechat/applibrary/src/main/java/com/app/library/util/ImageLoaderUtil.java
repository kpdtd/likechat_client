package com.app.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.app.library.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 加载图片工具类<br/>
 * 使用Android-Universal-Image-Loader<br/>
 * https://github.com/nostra13/Android-Universal-Image-Loader
 */
public class ImageLoaderUtil
{
    private static Context m_context;
    private static ImageLoader m_imageLoader;

    /** 列表中的图片（没有圆角） */
    private static DisplayImageOptions m_optionsListItem;
    /** 单独显示的头像 */
    private static DisplayImageOptions m_optionsAvatar;
    /** 用于绘制圆角 */
    private static DisplayImageOptions m_optionsListAvatar;
    /** 用于显示大图浏览 */
    private static DisplayImageOptions m_optionsListPhoto;


    private static void init()
    {
        init(m_context);
    }

    public static void init(Context context)
    {
        try
        {
            if (m_imageLoader == null)
            {
                m_context = context;
                //Context context = theApp.CONTEXT;
                File cacheDir = StorageUtils.getCacheDirectory(context);
                // Configuration(ImageLoaderConfiguration) 是相对于整个应用的配置
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .threadPoolSize(5) // 线程池大小
                        .denyCacheImageMultipleSizesInMemory()
                        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                        //.memoryCacheExtraOptions(360, 360) // default = device screen dimensions
                        .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
                        .diskCache(new UnlimitedDiskCache(cacheDir))
                        .diskCacheSize(50 * 1024 * 1024)
                        .diskCacheFileCount(100)
                        //.memoryCache(new WeakMemoryCache())
                        .build();

                m_imageLoader = ImageLoader.getInstance();
                m_imageLoader.init(config);

                m_optionsListItem = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.loader_empty)// 设置图片在下载期间显示的图片
                        .showImageForEmptyUri(R.mipmap.loader_empty) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.mipmap.loader_error) // 设置图片加载/解码过程中错误时候显示的图片
                        //.delayBeforeLoading(300)
                        .bitmapConfig(Bitmap.Config.RGB_565) // Bitmaps in RGB_565 consume 2 times less memory than in ARGB_8888
                        .cacheOnDisk(true)
                        //.cacheInMemory()
                        //.cacheOnDisc()
                        // .displayer(new RoundedBitmapDisplayer(20))
                        .build();

                m_optionsAvatar = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.avatar_def)
                        // 设置图片在下载期间显示的图片
                        .showImageOnLoading(R.drawable.avatar_def)// 在ImageView加载过程中显示图片
                        .showImageOnFail(R.drawable.avatar_def)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        // 设置图片Uri为空或是错误的时候显示的图片
                        //.showStubImage(R.mipmap.ic_user)
                        // 设置图片加载/解码过程中错误时候显示的图片
                        //.cacheInMemory()
                        //.cacheOnDisc()
                        // 圆角显示
                        //.displayer(new RoundedBitmapDisplayer(10))
                        .build();

                m_optionsListAvatar = new DisplayImageOptions.Builder()
                        // 设置图片在下载期间显示的图片
                        .showImageOnLoading(R.drawable.avatar_def)// 在ImageView加载过程中显示图片
                        .showImageOnFail(R.drawable.avatar_def)
                        .showImageForEmptyUri(R.drawable.avatar_def)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        // 设置图片Uri为空或是错误的时候显示的图片
                        //.showStubImage(R.mipmap.ic_user)
                        // 设置图片加载/解码过程中错误时候显示的图片
                        //.cacheInMemory()
                        //.delayBeforeLoading(300)
                        //.cacheOnDisc()
                        //.displayer(new RoundedBitmapDisplayer(14))
                        .build();

                m_optionsListPhoto = new DisplayImageOptions.Builder()
                        //.showImageOnLoading(R.mipmap.loader_empty)// 设置图片在下载期间显示的图片
                        .showImageForEmptyUri(R.mipmap.loader_empty) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.mipmap.loader_error) // 设置图片加载/解码过程中错误时候显示的图片
                        .resetViewBeforeLoading(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        //.delayBeforeLoading(300)
                        //.showStubImage(R.mipmap.ic_user) // 设置图片在下载期间显示的图片
                        //.cacheInMemory()
                        //.cacheOnDisc()
                        // .displayer(new RoundedBitmapDisplayer(20))
                        .build();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static ImageLoader getInstance()
    {
        return m_imageLoader;
    }

    public static AbsListView.OnScrollListener getPauseListener()
    {
        boolean pauseOnScroll = true, pauseOnFling = true;
        return new PauseOnScrollListener(ImageLoaderUtil.getInstance(), pauseOnScroll, pauseOnFling);
    }

    /**
     * 在列表中显示头像
     * @param imageView
     * @param url
     */
    public static void displayListImage(ImageView imageView, String url)
    {
        try
        {
            if (imageView == null)
            {
                return;
            }

            init();

            String strUrl = (url == null ? "" : url);
            m_imageLoader.displayImage(strUrl, imageView, m_optionsListItem);
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

    /**
     * 在列表中显示大图<br/>
     * 加载asset中的图片
     * @param imageView
     * @param imageName asset 中图片文件名
     */
    public static void displayListPhotoImage(ImageView imageView, String imageName, ImageLoadingListener listener)
    {
        try
        {
            if (imageView == null)
            {
                return;
            }

            init();

            String strUrl = imageName;
            m_imageLoader.displayImage(strUrl, imageView, m_optionsListPhoto, listener);
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
    public static void displayFromFile(ImageView imageView, String imageName)
    {
        try
        {
            if (imageView == null)
            {
                return;
            }

            init();

            String strUrl = "file:///" + imageName;
            m_imageLoader.displayImage(strUrl, imageView, m_optionsAvatar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    /**
//     * 在列表中显示图片<br/>
//     * 加载asset中的图片
//     * @param imageView
//     * @param imageName asset 中图片文件名
//     */
//    public static void displayPhotoFromFile(ImageView imageView, String imageName)
//    {
//        try
//        {
//            if (imageView == null)
//            {
//                return;
//            }
//
//            String strUrl = "file:///" + imageName;
//            Glide.with(theApp.CONTEXT)
//                    .load(strUrl)
//                    //.placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
//                    .centerCrop()
//                    .into(imageView);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
