package com.audio.miliao.entity;

import com.audio.miliao.util.PreferUtil;
import com.audio.miliao.vo.UserRegisterVo;

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

    public static void setUserInfo(UserRegisterVo userInfo)
    {
        PreferUtil.setStringPreference(KEY_USERINFO, userInfo.toJsonString());
    }

    public static UserRegisterVo getUserInfo()
    {
        String strUserInfo = PreferUtil.getStringPreference(KEY_USERINFO);
        UserRegisterVo userInfo = UserRegisterVo.parse(strUserInfo, UserRegisterVo.class);
        return userInfo;
    }

    /**
     * 登录后返回的用户id
     * @param userId
     */
    public static void setUserId(String userId)
    {
        PreferUtil.setStringPreference(KEY_USER_ID, userId);
    }

    /**
     * 登录后返回的用户id
     * @return
     */
    public static String getUserId()
    {
        return PreferUtil.getStringPreference(KEY_USER_ID);
    }

    /**
     * qq或者微信登录后返回的openid
     * @param openId
     */
    public static void setOpenId(String openId)
    {
        PreferUtil.setStringPreference(KEY_OPEN_ID, openId);
    }

    /**
     * qq或者微信登录后返回的openid
     * @return
     */
    public static String getOpenId()
    {
        return PreferUtil.getStringPreference(KEY_OPEN_ID);
    }

    private final static String KEY_YUNXIN_ACCOUNT = "key_yunxin_account";
    private final static String KEY_YUNXIN_TOKEN = "key_yunxin_token";

    private final static String KEY_ACTOR = "key_actor";
    private static final String KEY_TOKEN = "key_token";

    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_USERINFO = "key_userinfo";

    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_OPEN_ID = "key_open_id";
}
