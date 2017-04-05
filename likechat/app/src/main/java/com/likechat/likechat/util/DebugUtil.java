package com.likechat.likechat.util;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.TextChatMessage;
import com.likechat.likechat.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成调试数据
 */
public class DebugUtil
{
    public static List<User> getUserList()
    {
        try
        {
            int[] avatars = new int[]{
                    R.mipmap.avatar1,
                    R.mipmap.avatar2,
                    R.mipmap.avatar3
            };
            String[] citys = new String[]
                    {
                            "四川 成都",
                            "广东 深圳",
                            "江苏 苏州"
                    };
            int[] fanses = new int[]
                    {
                            160000, 18000, 20000
                    };
            int[] follows = new int[]
                    {
                            100, 200, 300
                    };
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < 20; i++)
            {
                int index = i % 3;
                User user = new User();
                user.name = "我是直播主播" + (i + 1);
                user.age = 20;
                user.id = String.valueOf(10000 + i + 1);
                user.city = citys[index];
                user.gender = User.GENDER_FEMALE;
                user.intro = "虽说这座临时洞府外仅仅布置了一套隐秘旗阵，很难瞒过真丹境的修士，但若要骗过化晶修士还是绰绰有余的";
                user.avatar_res = avatars[index];
                user.fans = fanses[index];
                user.follow = follows[index];
                userList.add(user);
            }

            return userList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static List<TextChatMessage> getChatMessage()
    {
        try
        {
            String[] strTexts = new String[]{"测试文字消息", "测试文字消息，测试文字消息", "测试文字消息，测试文字消息，测试文字消息，测试文字消息"};
            List<TextChatMessage> chatMessages = new ArrayList<>();
            for (int i = 0; i < 20; i++)
            {
                TextChatMessage chatMessage = new TextChatMessage();
                chatMessage.text = strTexts[i % 3] + i;
                if (i % 2 == 0)
                {
                    chatMessage.from = AppData.getCurUser();
                }

                chatMessages.add(chatMessage);
            }

            return chatMessages;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
