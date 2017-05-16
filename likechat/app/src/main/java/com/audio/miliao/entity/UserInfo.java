package com.audio.miliao.entity;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfo extends GsonObj<UserInfo> implements Serializable
{
    public String accessToken;
    public int expiresIn;
    public String refreshToken; // 微信有用，qq没用
    public String nickname;
    public String avatar;
    public String openId;
    public String gender;
    public String province;
    public String city;
    public String type; // QQ、微信登录
}
