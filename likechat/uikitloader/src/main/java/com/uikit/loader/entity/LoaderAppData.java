package com.uikit.loader.entity;


import com.uikit.loader.util.PreferUtil;

/**
 * 内存中使用的数据
 */

public class LoaderAppData
{
    /** 当前用户 */
    //private static User ms_curUser;
    private static boolean ms_bIsLogin = false;

    public static void saveToken(String strToken)
    {
        PreferUtil.setStringPreference(KEY_TOKEN, strToken);
    }

    public static String getToken()
    {
        return PreferUtil.getStringPreference(KEY_TOKEN);
    }

    /**
     * 获取是否已登录
     * @return
     */
    public static boolean isLogin()
    {
        return getCurUserId() != -1;
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

    /**
     * 登录后返回的用户id
     * @param userId
     */
    public static void setCurUserId(int userId)
    {
        PreferUtil.setIntPreference(KEY_USER_ID, userId);
    }

    /**
     * 登录后返回的用户id
     * @return
     */
    public static int getCurUserId()
    {
        return PreferUtil.getIntPreference(KEY_USER_ID, -1);
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
    private final static String KEY_ACTOR_PAGE_VO = "key_actor_page_vo";
    private static final String KEY_TOKEN = "key_token";

    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_USERINFO = "key_userinfo";

    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_OPEN_ID = "key_open_id";
}
