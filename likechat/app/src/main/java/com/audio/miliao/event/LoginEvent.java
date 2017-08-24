package com.audio.miliao.event;

import com.netease.nim.uikit.miliao.vo.UserRegisterVo;

/**
 * EventBus发送的登录结果Event
 */
public class LoginEvent extends BaseEvent
{
    private UserRegisterVo registerVo;
    private int userId;

    public LoginEvent(UserRegisterVo registerVo, int userId)
    {
        this.registerVo = registerVo;
        this.userId = userId;
    }

    public UserRegisterVo getRegisterVo()
    {
        return registerVo;
    }

    public int getUserId()
    {
        return userId;
    }
}
