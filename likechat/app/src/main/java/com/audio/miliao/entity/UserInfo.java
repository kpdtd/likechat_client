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
    //public String nickname;
    //public String avatar;
    //public String openId;
    //public String gender;
    //public String province;
    //public String city;
    //public String type; // QQ、微信登录

    public String openId; //微信或qq返回的openid
    public String loginType; //值：qq  weixin拼音
    public String nickname; // '昵称'
    public String icon; //头像访问地址
    public String signature; //个性签名
    public String province; //省份
    public String city; //城市
    public int sex; //1男2女
}
