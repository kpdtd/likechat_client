package com.audio.miliao.util;

import android.app.Activity;
import android.content.Intent;

import com.audio.miliao.http.cmd.Login;
import com.audio.miliao.theApp;
import com.audio.miliao.vo.UserRegisterVo;
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
                    theApp.showToast("login onComplete " + o.toString());
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
                            theApp.showToast("fetchUserInfo onComplete " + o.toString());
                            JSONObject jsonUserInfo = (JSONObject) o;

                            try
                            {
                                UserRegisterVo userInfo = new UserRegisterVo();
                                userInfo.setOpenId(openId);
                                userInfo.setCity(JSONUtil.getString(jsonUserInfo, "city"));
                                userInfo.setIcon(JSONUtil.getString(jsonUserInfo, "figureurl_2"));
                                userInfo.setLogin_type("qq");
                                userInfo.setNickname(JSONUtil.getString(jsonUserInfo, "nickname"));
                                userInfo.setProvince(JSONUtil.getString(jsonUserInfo, "province"));
                                userInfo.setSex(JSONUtil.getString(jsonUserInfo, "gender"));
                                userInfo.setSignature("");

                                theApp.showToast("begin login");
                                Login login = new Login(null, userInfo, null);
                                login.send();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                theApp.showToast("login error " + e.toString());
                            }
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
     *
     * @param activity
     */
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
