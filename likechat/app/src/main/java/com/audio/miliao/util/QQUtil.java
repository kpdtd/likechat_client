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
                    //theApp.showToast("onComplete" + jsonObject.toString());
                    final String openId = jsonObject.optString("openid");
                    final String accessToken = jsonObject.optString("access_token");
                    final String expires = jsonObject.optString("expires_in");
                    mTencent.setOpenId(openId);
                    mTencent.setAccessToken(accessToken, expires);
                    //fetchUserinfo(mNotifyCode, mHandler);
                    fetchUserInfoSync(new IUiListener() {
                        @Override
                        public void onComplete(Object o)
                        {
                            JSONObject jsonUserInfo = (JSONObject) o;
                            JSONObject json = new JSONObject();

                            try
                            {
                                json.put("open_id", openId);
                                json.put("access_token", accessToken);
                                json.put("expires_in", expires);
                                json.put("nickname", JSONUtil.getString(jsonUserInfo, "nickname"));
                                json.put("gender", JSONUtil.getString(jsonUserInfo, "gender"));
                                json.put("avatar", JSONUtil.getString(jsonUserInfo, "figureurl_2"));

                                if (mHandler != null)
                                {
                                    mHandler.obtainMessage(mNotifyCode, json).sendToTarget();
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

//    public static void fetchUserinfo(final int notifyCode, final Handler handler)
//    {
//        Runnable runnable = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                JSONObject json = fetchUserInfoSync();
//                if (handler != null)
//                {
//                    mNotifyCode = notifyCode;
//                    handler.obtainMessage(notifyCode, json).sendToTarget();
//                }
//            }
//        };
//
//        new Thread(runnable).start();
//    }

    /**
     * 同步获取用户信息
     */
    public static JSONObject fetchUserInfoSync(final IUiListener listener)
    {
        try
        {
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(theApp.CONTEXT, qqToken);
            info.getUserInfo(listener);
//            info.getUserInfo(new IUiListener()
//            {
//                @Override
//                public void onComplete(Object response)
//                {
//                    JSONObject json = (JSONObject) response;
//                    theApp.showToast(json.toString());
//                    if (mHandler != null)
//                    {
//                        mHandler.obtainMessage(mNotifyCode, json).sendToTarget();
//                    }
////                    // 昵称
////                    String nickname = null;
////                    try
////                    {
////                        Message msg = new Message();
////                        nickname = json.getString("nickname");
////                        msg.getData().putString("nickname", nickname);
////                        msg.what = 0;
////                        mHandler.sendMessage(msg);
////                    }
////                    catch (JSONException e)
////                    {
////                        e.printStackTrace();
////                    }
////                    //头像
////                    String smallimgurl, bigimgurl;
////                    try
////                    {
////                        Message msg = new Message();
////                        smallimgurl = json.getString("figureurl_qq_1");
////                        bigimgurl = json.getString("figureurl_qq_1");
////                        msg.getData().putString("smallimgurl", smallimgurl);
////                        msg.getData().putString("bigimgurl", bigimgurl);
////                        msg.what = 1;
////                        mHandler.sendMessage(msg);
////                    }
////                    catch (JSONException e)
////                    {
////                        e.printStackTrace();
////                    }
//                }
//
//                @Override
//                public void onError(UiError uiError)
//                {
//
//                }
//
//                @Override
//                public void onCancel()
//                {
//
//                }
//            });

//            String get_simple_userinfo = Constants.GRAPH_BASE; //"get_simple_userinfo";
//            JSONObject json = mTencent.request(
//                    get_simple_userinfo, null,
//                    Constants.HTTP_GET);
//
//            LogUtil.d(json.toString());
//            theApp.showToast(json.toString());
//
//            return json;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        init();
        Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
    }
}
