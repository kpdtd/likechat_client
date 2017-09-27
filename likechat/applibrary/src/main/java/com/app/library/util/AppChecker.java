package com.app.library.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * App的检查工具
 */
public class AppChecker
{
    /**
     * 判断应用是否在前台
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))
        {
            return true;
        }

        return false;
    }


    /**
     * 微信是否已安装
     * @param context
     * @return
     */
    public static boolean isWechatInstalled(Context context)
    {
        return isAvilible(context, "com.tencent.mm");
    }

    /**
     * qq是否已安装
     * @param context
     * @return
     */
    public static boolean isQQInstalled(Context context)
    {
        return isAvilible(context, "com.tencent.mobileqq");
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName
     *            ：应用包名
     * @return
     */
    private static boolean isAvilible(Context context, String packageName)
    {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}