package com.likechat.likechat.util;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.CallHistory;
import com.likechat.likechat.entity.ChatMessage;
import com.likechat.likechat.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.likechat.likechat.R.mipmap.avatar1;

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
            String[] strAvatars = new String[]{"avatar1.jpg", "avatar2.jpg", "avatar3.jpg"};
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
                user.avatar = strAvatars[index];
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

    public static List<ChatMessage> getChatMessage()
    {
        try
        {
            Date dateOneDay = StringUtil.getDate("2017-02-06");
            String[] strTexts = new String[]{"测试文字消息", "测试文字消息，测试文字消息", "测试文字消息，测试文字消息，测试文字消息，测试文字消息"};
            List<ChatMessage> chatMessages = new ArrayList<>();
            for (int i = 0; i < 20; i++)
            {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.text = strTexts[i % 3] + i;
                if (i % 2 == 0)
                {
                    chatMessage.from = AppData.getCurUser();
                    chatMessage.date = System.currentTimeMillis();
                }
                else
                {
                    chatMessage.date = dateOneDay.getTime();
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

    public static List<CallHistory> getCallHistory()
    {
        List<CallHistory> callHistories = new ArrayList<>();
        try
        {
            Date dateOneDay = StringUtil.getDate("2017-02-06");
            User user1 = new User();
            user1.name = "美丽可儿";
            user1.avatar_res = avatar1;
            user1.avatar = "avatar1.jpg";
            User user2 = new User();
            user2.name = "寂寞美人";
            user2.avatar_res = R.mipmap.avatar2;
            user2.avatar = "avatar2.jpg";
            User user3 = new User();
            user3.name = "足球宝贝";
            user3.avatar_res = R.mipmap.avatar3;
            user3.avatar = "avatar3.jpg";
            User users1[] = new User[]{user1, user2, user3, AppData.getCurUser()};
            User users2[] = new User[]{user1, user2, user3};
            int talkTimes[] = new int[]{0, 59, 61, 3599, 3601, 123456};
            for (int i = 0; i < 20; i++)
            {
                CallHistory callHistory = new CallHistory();
                callHistory.from = users1[i % 4];
                if (callHistory.from.equals(AppData.getCurUser()))
                {
                    callHistory.to = users2[i % 3];
                }
                else
                {
                    callHistory.to = AppData.getCurUser();
                }

                if (i % 2 == 0)
                {
                    callHistory.startTime = dateOneDay.getTime();
                }
                else
                {
                    callHistory.startTime = System.currentTimeMillis();
                }

                callHistory.talkTime = talkTimes[i % talkTimes.length];

                callHistories.add(callHistory);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return callHistories;
    }
}
