package com.audio.miliao.entity;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfo extends GsonObj<UserInfo> implements Serializable
{
    public String accessToken;
    public int expiresIn;
    public String nickname;
    public String avatar;
    public String openId;
    public String gender;
}
