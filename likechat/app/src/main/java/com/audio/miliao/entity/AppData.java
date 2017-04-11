package com.audio.miliao.entity;

import com.audio.miliao.util.PreferUtil;

/**
 * 内存中使用的数据
 */

public class AppData
{
    /** 当前用户 */
    private static User ms_curUser;
    private static boolean ms_bIsLogin = false;

    public static void saveCurUser(User user)
    {
        ms_curUser = user;
    }

    /**
     * 获取当前用户
     * @return
     */
    public static User getCurUser()
    {
        return ms_curUser;
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

    private final static String KEY_IS_LOGIN = "key_is_login";
    private final static String KEY_USER = "key_user";
}