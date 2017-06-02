package com.audio.miliao.event;

import com.audio.miliao.vo.UserRegisterVo;

/**
 * EventBus发送的登录结果Event
 */
public class LoginEvent
{
    private UserRegisterVo registerVo;
    private String userId;

    public LoginEvent(UserRegisterVo registerVo, String userId)
    {
        this.registerVo = registerVo;
        this.userId = userId;
    }

    public UserRegisterVo getRegisterVo()
    {
        return registerVo;
    }

    public String getUserId()
    {
        return userId;
    }
}
