package com.app.library.util;

import android.content.Context;
import android.net.Uri;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

/**
 * 下载管理
 * 使用 https://github.com/smanikandan14/ThinDownloadManager
 */
public class DownloadUtil
{
    private static Context sm_context;
    private static ThinDownloadManager downloadManager;

    public static void init(Context context)
    {
        sm_context = context;
        downloadManager = new ThinDownloadManager();
    }

    /**
     * 开始下载
     * @param url url
     * @param fileName 保存到sd卡中的名字
     * @param listener 下载监听器
     * @return 下载任务id
     */
    public static int startDoanload(String url, String fileName, DownloadStatusListenerV1 listener)
    {
        try
        {
            Uri downloadUri = Uri.parse(url);
            Uri destinationUri = Uri.parse(FileUtil.getSDCardAppRootDir(sm_context, "apk") + "/" + fileName);
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    //.addCustomHeader("Auth-Token", "YourTokenApiKey")
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setDestinationURI(destinationUri)
                    .setPriority(DownloadRequest.Priority.HIGH)
                    .setDeleteDestinationFileOnFailure(true)
                    .setStatusListener(listener);
                    //.setDownloadContext(downloadContextObject)//Optional
                    //.setDownloadListener(listener);

            int doanloadId = downloadManager.add(downloadRequest);
            return doanloadId;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }
}
