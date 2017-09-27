package com.audio.miliao.entity;

import com.app.library.util.DateUtil;
import com.app.library.util.PreferUtil;
import com.app.library.vo.ActorPageVo;
import com.app.library.vo.ActorVo;
import com.app.library.vo.UserRegisterVo;
import com.app.library.util.JSONUtil;

import org.json.JSONObject;

import java.util.Date;

/**
 * 内存中使用的数据
 */

public class AppData
{
    public static void setCurUser(ActorPageVo actor)
    {
        //ms_curUser = user;
        PreferUtil.setStringPreference(KEY_ACTOR, actor.toJson().toString());
    }

    /**
     * 获取当前用户
     * @return
     */
    public static ActorPageVo getCurUser()
    {
        //return ms_curUser;
        try
        {
            JSONObject jsonObject = new JSONObject(PreferUtil.getStringPreference(KEY_ACTOR));
            ActorPageVo actor = ActorPageVo.parse(jsonObject.toString(), ActorPageVo.class);
            return actor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
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

//    public static void setCurActorPageVo(ActorPageVo actor)
//    {
//        //ms_curUser = user;
//        PreferUtil.setStringPreference(KEY_ACTOR_PAGE_VO, actor.toJson().toString());
//    }
//
//    /**
//     * 获取当前用户
//     * @return
//     */
//    public static ActorPageVo getCurActorPageVo()
//    {
//        //return ms_curUser;
//        try
//        {
//            JSONObject jsonObject = new JSONObject(PreferUtil.getStringPreference(KEY_ACTOR_PAGE_VO));
//            ActorPageVo actor = ActorPageVo.parse(jsonObject.toString(), ActorPageVo.class);
//            return actor;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * 判断用户是否是当前用户
     * @param actor
     * @return
     */
    public static boolean isCurUser(ActorVo actor)
    {
        try
        {
            if (actor != null)
            {
                return actor.getId().equals(getCurUserId());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

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
        // Debug
        return true;
        //return Checker.isNotEmpty(getCurUserId());
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

    public static void setAutoCallInTime(long timeMill, int time)
    {
        String strDate = DateUtil.parseDate(new Date(timeMill));
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(strDate, time);

            PreferUtil.setStringPreference(KEY_AUTO_CALL_IN, jsonObject.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 返回当天显示呼叫的次数
     * @param timeMill 当天的时间
     * @return
     */
    public static int getAutoCallInTime(long timeMill)
    {
        String strDate = DateUtil.parseDate(new Date(timeMill));
        try
        {
            String strAutoCallInTimes = PreferUtil.getStringPreference(KEY_AUTO_CALL_IN);
            JSONObject jsonObject = new JSONObject(strAutoCallInTimes);
            int times = JSONUtil.getInt(jsonObject, strDate);

            return times >= 0 ? times : 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

//    /**
//     * 登录后返回的用户id
//     * @param userId
//     */
//    public static void setCurUserId(int userId)
//    {
//        PreferUtil.setIntPreference(KEY_USER_ID, userId);
//    }
//
//    /**
//     * 登录后返回的用户id
//     * @return
//     */
//    public static int getCurUserId()
//    {
//        return PreferUtil.getIntPreference(KEY_USER_ID, -1);
//    }
//
//    /**
//     * qq或者微信登录后返回的openid
//     * @param openId
//     */
//    public static void setOpenId(String openId)
//    {
//        PreferUtil.setStringPreference(KEY_OPEN_ID, openId);
//    }
//
//    /**
//     * qq或者微信登录后返回的openid
//     * @return
//     */
//    public static String getOpenId()
//    {
//        return PreferUtil.getStringPreference(KEY_OPEN_ID);
//    }

    private final static String KEY_YUNXIN_ACCOUNT = "key_yunxin_account";
    private final static String KEY_YUNXIN_TOKEN = "key_yunxin_token";

    private final static String KEY_ACTOR = "key_actor";
    private final static String KEY_ACTOR_PAGE_VO = "key_actor_page_vo";
    private static final String KEY_TOKEN = "key_token";

    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_USERINFO = "key_userinfo";

    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_OPEN_ID = "key_open_id";

    private static final String KEY_AUTO_CALL_IN = "key_auto_cal_in";
}
