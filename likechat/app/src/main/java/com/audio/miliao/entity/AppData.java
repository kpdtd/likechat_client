package com.audio.miliao.entity;

import com.audio.miliao.util.PreferUtil;

import org.json.JSONObject;

/**
 * 内存中使用的数据
 */

public class AppData
{
    public static void saveCurUser(Actor actor)
    {
        //ms_curUser = user;
        PreferUtil.setStringPreference(KEY_ACTOR, actor.toJson().toString());
    }

    /**
     * 获取当前用户
     * @return
     */
    public static Actor getCurUser()
    {
        //return ms_curUser;
        try
        {
            JSONObject jsonObject = new JSONObject(PreferUtil.getStringPreference(KEY_ACTOR));
            Actor actor = Actor.fromJson(jsonObject);
            return actor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断用户是否是当前用户
     * @param actor
     * @return
     */
    public static boolean isCurUser(Actor actor)
    {
        try
        {
            if (actor != null && getCurUser() != null)
            {
                return actor.id.equals(getCurUser().id);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取是否已登录
     * @return
     */
    public static boolean isLogin()
    {
        return getUserInfo() != null;
    }

    public static void setYunXinAccount(String account)
    {
        PreferUtil.setStringPreference(KEY_YUNXIN_ACCOUNT, account);
    }

    public static String getYunXinAccount()
    {
        return PreferUtil.getStringPreference(KEY_YUNXIN_ACCOUNT);
    }

    public static void setYunXinToken(String token)
    {
        PreferUtil.setStringPreference(KEY_YUNXIN_TOKEN, token);
    }

    public static String getYunXinToken()
    {
        return PreferUtil.getStringPreference(KEY_YUNXIN_TOKEN);
    }

    public static void setRefreshToken(String refreshToken)
    {
        PreferUtil.setStringPreference(KEY_REFRESH_TOKEN, refreshToken);
    }

    public static String getRefreshToken()
    {
        return PreferUtil.getStringPreference(KEY_REFRESH_TOKEN);
    }

    public static void setUserInfo(UserInfo userInfo)
    {
        PreferUtil.setStringPreference(KEY_USERINFO, userInfo.toJsonString());
    }

    public static UserInfo getUserInfo()
    {
        String strUserInfo = PreferUtil.getStringPreference(KEY_USERINFO);
        UserInfo userInfo = UserInfo.parse(strUserInfo, UserInfo.class);
        return userInfo;
    }

    private final static String KEY_YUNXIN_ACCOUNT = "key_yunxin_account";
    private final static String KEY_YUNXIN_TOKEN = "key_yunxin_token";

    private final static String KEY_IS_LOGIN = "key_is_login";
    private final static String KEY_ACTOR = "key_actor";
    private static final String KEY_TOKEN = "key_token";

    private static final String KEY_ACCESS_TOKEN = "key_access_token";
    private static final String KEY_EXPIRES_IN = "key_expires_in";
    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_OPEN_ID = "key_open_id";
    private static final String KEY_NICKNAME = "key_nickname";
    private static final String KEY_AVATAR = "key_avatar";
    private static final String KEY_USERINFO = "key_userinfo";
}
