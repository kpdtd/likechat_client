package com.likechat.likechat.entity;

/**
 * 内存中使用的数据
 */

public class AppData
{
    /** 当前用户 */
    private static User ms_curUser;

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
}
