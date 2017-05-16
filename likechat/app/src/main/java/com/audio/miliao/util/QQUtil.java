package com.audio.miliao.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.audio.miliao.theApp;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
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

    private static int mNotifyCode = -1;
    private static Handler mHandler = null;

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
                    final JSONObject jsonObject = (JSONObject) o;
                    final String openId = jsonObject.optString("openid");
                    final String accessToken = jsonObject.optString("access_token");
                    final int expiresIn = jsonObject.optInt("expires_in");
                    mTencent.setOpenId(openId);
                    mTencent.setAccessToken(accessToken, String.valueOf(expiresIn));

                    fetchUserInfo(new IUiListener()
                    {
                        @Override
                        public void onComplete(Object o)
                        {
                            JSONObject jsonUserInfo = (JSONObject) o;

                            try
                            {
                                com.audio.miliao.entity.UserInfo userInfo = new com.audio.miliao.entity.UserInfo();
                                userInfo.openId = openId;
                                userInfo.accessToken = accessToken;
                                userInfo.expiresIn = expiresIn;
                                userInfo.refreshToken = "";
                                userInfo.nickname = JSONUtil.getString(jsonUserInfo, "nickname");
                                userInfo.gender = JSONUtil.getString(jsonUserInfo, "gender");
                                userInfo.avatar = JSONUtil.getString(jsonUserInfo, "figureurl_2");
                                userInfo.province = JSONUtil.getString(jsonUserInfo, "province");
                                userInfo.city = JSONUtil.getString(jsonUserInfo, "city");
                                userInfo.type = "QQ";

                                if (mHandler != null)
                                {
                                    mHandler.obtainMessage(mNotifyCode, userInfo).sendToTarget();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(UiError uiError)
                        {

                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
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

    /**
     * QQ登录<br/>
     * 调用qq登录的sak获取用户信息<br/>
     * 1、获取openid, accessToken,expiresIn<br/>
     * 2、获取用户信息（nickname,avatar）<br/>
     * 两步成功了才回调界面
     * @param activity
     * @param notifyCode
     * @param handler
     */
    public static void login(Activity activity, final int notifyCode, final Handler handler)
    {
        init();
        if (!mTencent.isSessionValid())
        {
            mNotifyCode = notifyCode;
            mHandler = handler;

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
    private static void fetchUserInfo(final IUiListener listener)
    {
        try
        {
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(theApp.CONTEXT, qqToken);
            info.getUserInfo(listener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        init();
        Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
    }
}
