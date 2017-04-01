package com.likechat.likechat.entity;

import java.io.Serializable;

/**
 * 主播
 */

public class Anchor implements Serializable
{
    /** 头像 */
    public String avatar;
    public int avatar_res;
    /** 名字 */
    public String name;
    /** 性别 */
    public String gender;
    /** 年龄 */
    public int age;
    /** 介绍 */
    public String intro;
}
