package com.audio.miliao.util;

import android.app.Activity;
import android.content.Intent;

import com.audio.miliao.theApp;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * QQ登录相关
 */
public class QQUtil
{
    private final static String APP_ID = "1106066290";
    private final static String APP_KEY = "sLaaa2uLsQfNukWE";

    private static Tencent mTencent = null;
    private static IUiListener mUiListener = null;

    private static void init()
    {
        if (mTencent == null)
        {
            mTencent = Tencent.createInstance(APP_ID, theApp.CONTEXT);
        }

        if (mUiListener == null)
        {
            mUiListener = new IUiListener()
            {
                @Override
                public void onComplete(Object o)
                {
                    JSONObject jsonObject = (JSONObject) o;
                    theApp.showToast("onComplete" + jsonObject.toString());
                    getUserInfoSync();
                }

                @Override
                public void onError(UiError uiError)
                {
                    theApp.showToast("onError");
                }

                @Override
                public void onCancel()
                {
                    theApp.showToast("onCancel");
                }
            };
        }
    }

    public static void login(Activity activity)
    {
        init();
        if (!mTencent.isSessionValid())
        {
            // 应用需要获得哪些接口的权限，由“,”分隔，
            // 例如：SCOPE = “get_user_info,add_topic”；
            // 如果需要所有权限则使用”all”
            mTencent.login(activity, "all", mUiListener);
        }
    }

    public static void logout(Activity activity)
    {
        init();
        mTencent.logout(activity);
    }

    /**
     * 同步获取用户信息
     */
    public static void getUserInfoSync()
    {
        new Thread(){
            @Override
            public void run() {
                try
                {
                    String get_simple_userinfo = "get_simple_userinfo";
                    JSONObject json = mTencent.request(
                            get_simple_userinfo, null,
                            Constants.HTTP_GET);

                    LogUtil.d(json.toString());
                    theApp.showToast(json.toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        init();
        Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
    }
}
