package com.audio.miliao.entity;

import com.audio.miliao.util.PreferUtil;

import org.json.JSONObject;

/**
 * 内存中使用的数据
 */

public class AppData
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

    public static void saveCurUser(User user)
    {
        //ms_curUser = user;
        PreferUtil.setStringPreference(KEY_USER, user.toJson().toString());
    }

    /**
     * 获取当前用户
     * @return
     */
    public static User getCurUser()
    {
        //return ms_curUser;
        try
        {
            JSONObject jsonObject = new JSONObject(PreferUtil.getStringPreference(KEY_USER));
            User user = User.fromJson(jsonObject);
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断用户是否是当前用户
     * @param user
     * @return
     */
    public static boolean isCurUser(User user)
    {
        try
        {
            if (user != null && getCurUser() != null)
            {
                return user.id.equals(getCurUser().id);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 保存是否已登录
     * @param isLogin
     */
    public static void saveIsLogin(boolean isLogin)
    {
        PreferUtil.setBooleanPreference(KEY_IS_LOGIN, isLogin);
    }

    /**
     * 获取是否已登录
     * @return
     */
    public static boolean isLogin()
    {
        return PreferUtil.getBooleanPreference(KEY_IS_LOGIN, false);
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

    private final static String KEY_YUNXIN_ACCOUNT = "key_yunxin_account";
    private final static String KEY_YUNXIN_TOKEN = "key_yunxin_token";

    private final static String KEY_IS_LOGIN = "key_is_login";
    private final static String KEY_USER = "key_user";
    private static final String KEY_TOKEN = "key_token";
}
