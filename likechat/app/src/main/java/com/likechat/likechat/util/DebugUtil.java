package com.likechat.likechat.util;

import com.likechat.likechat.R;
import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.CallHistory;
import com.likechat.likechat.entity.ChatMessage;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.entity.Zone;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
                    R.mipmap.avatar3,
                    R.mipmap.avatar4,
                    R.mipmap.avatar5,
                    R.mipmap.avatar6,
                    R.mipmap.avatar7,
                    R.mipmap.avatar8,
                    R.mipmap.avatar9,
                    R.mipmap.avatar10,
                    R.mipmap.avatar11,
                    R.mipmap.avatar12,
                    R.mipmap.avatar13,
                    R.mipmap.avatar14,
                    R.mipmap.avatar15,
                    R.mipmap.avatar16,
                    R.mipmap.avatar17,
                    R.mipmap.avatar18,
                    R.mipmap.avatar19,
                    R.mipmap.avatar20
            };
            String[] titles = new String[]
                    {
                            "安安心",
                            "国少小腰",
                            "无天双昊",
                            "过季颜色",
                            "冰雪飞花",
                            "小茜茜",
                            "安可儿",
                            "小虾米",
                            "安然然",
                            "欣宝宝",
                            "予馨",
                            "木槿",
                            "娜妹",
                            "村姑歌唱",
                            "依依守护",
                            "莲花林馨",
                            "紫色玫瑰",
                            "淡妆玉莹",
                            "可爱婷",
                            "文静傻呆"
                    };
            String[] signs = new String[]
                    {
                            "何苦让不快乐，尾随自己",
                            "何处是归宿，何时停脚步",
                            "不属于我的，我从来不要",
                            "寫一首傷感的詞，譜一曲留戀的歌",
                            "永远不是一种距离，而是一种决定",
                            "寂寞，让我变的那么脆弱",
                            "念念念你情不忘，想想想你情不亡",
                            "默然相爱，寂静喜欢",
                            "ㄣ过去被翻阅，瞬间才明白，原来，记忆已搁浅",
                            "无人的巷口，谁又为谁停留",
                            "不再沉默中变坏就在沉默就变态",
                            "过好自己 别人的故事里不需要你",
                            "爱我所爱 千夫所指我不改",
                            "愿姑娘们以后都是嫁给爱情",
                            "为什么我爱的人都有爱人",
                            "我有风里雨里的暴脾气，但最心疼的只有你",
                            "没有结果 感谢你曾来过",
                            "我假装不在乎你，但痛的是我自己",
                            "遇见是缘分 不遇见也是",
                            "其实很多人的爱情我们都看不懂"
                    };
            String[] intros = new String[]
                    {
                            "喜欢又不是爱，你的出现让我十分想念。",
                            "谁若用真心对我，我便拿命去珍惜。—这句话永远不会过期。",
                            "让我们继续同生命的繁华与慷慨相爱，即使岁月以刻薄与荒芜相欺。",
                            "感谢你的到来，有时唱歌没有欢迎到你们希望不要介意。",
                            "虽说这座临时洞府外仅仅布置了一套隐秘旗阵，很难瞒过真丹境的修士，但若要骗过化晶修士还是绰绰有余的。",
                            "纵剑飞舞，绣衣如雪，身周寒烟淡淡，有如轻纱笼体。",
                            "枝子低着头，看不清她的长相，只觉得她整个人都笼罩在一片安静、纯明、柔美的气氛之中。",
                            "双颊嫣红，比花还艳，目光迷蒙，那抹嫣红浸染玉颈，益发显得肌肤嫩如脂玉。",
                            "妇人素衣裹体，妍丽妖娆，举手投足，无不流露媚态。",
                            "心下得意，不由得笑魇如花，明艳不可方物。",
                            "美女卷珠帘，深坐蹙蛾眉，但见泪痕湿，不知心恨谁。",
                            "手如柔荑，肤如凝脂，领如蝤蛴，齿如瓠犀，螓首蛾眉，巧笑倩兮，美目眇兮。",
                            "懒懒一笑，拢了拢一头青丝，嘴角含着丝丝笑意。",
                            "明珠生晕、美玉莹光，眉目间隐然有一股书卷的清气。",
                            "一袭水色裙装包裹盈盈纤腰，三千青丝桃簪轻巧挽了个玲珑发髻。",
                            "妩媚一笑，梨涡轻陷。",
                            "略展了昳丽容颜，华色精妙唇线绽蔓嫣然笑意。",
                            "美女卷珠帘,深坐蹙蛾眉,但见泪痕湿,不知心恨谁。",
                            "一身翠绿衣衫,皮肤雪白,一张脸蛋清秀可爱。",
                            "折纤腰以微步，呈皓腕于轻纱。眸含春水清波流盼，头上倭堕髻斜插碧玉龙凤钗。香娇玉嫩秀靥艳比花娇，指如削葱根口如含朱丹，一颦一笑动人心魂。"
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
            Random rand = new Random(System.currentTimeMillis());
            java.util.HashSet<Integer> setExist = new java.util.HashSet<>();
            for (int i = 0; i < 20; i++)
            {
                int nIndex = -1;
                while(nIndex == -1 || setExist.contains(nIndex))
                {
                    nIndex = rand.nextInt(20);
                }

                setExist.add(nIndex);

                int avatar = nIndex;
                int index = i % 3;
                User user = new User();
                user.name = titles[avatar]; // "直播主播" + (i + 1);
                user.age = 20;
                user.id = String.valueOf(10000 + i + 1);
                user.city = citys[index];
                user.gender = User.GENDER_FEMALE;
                user.sign = signs[avatar];
                user.intro = intros[avatar]; // "虽说这座临时洞府外仅仅布置了一套隐秘旗阵，很难瞒过真丹境的修士，但若要骗过化晶修士还是绰绰有余的";
                user.avatar_res = avatars[avatar];
                user.avatar = "thumb" + (avatar + 1) + ".jpg";
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

    public static List<Zone> getZonesByAnchor(User user)
    {
        try
        {
            // 这是生成的随机数
            Random rand = new Random(System.currentTimeMillis());
            java.util.HashSet<Integer> setExist = new java.util.HashSet<>();
            JSONArray jArrThumb = new JSONArray();
            JSONArray jArrPhoto = new JSONArray();

            Date dateOneDay = StringUtil.getDate("2017-02-06");
            String strText = "测试文字说说";
            List<Zone> listZone = new ArrayList<>();
            for (int i = 0; i < 20; i++)
            {
                Zone zone = new Zone();
                zone.id = java.util.UUID.randomUUID().toString();
                zone.text = strText + rand.nextInt(20);
                zone.watch = rand.nextInt(1000000);

                // 随机数, 1~9
                int pics = rand.nextInt(9) + 1;
                setExist.clear();
                jArrThumb = new JSONArray();
                jArrPhoto = new JSONArray();

                for(int j = 0; j < pics; j++)
                {
                    int nIndex = -1;
                    while(nIndex == -1 || setExist.contains(nIndex))
                    {
                        nIndex = rand.nextInt(20) + 1;
                    }

                    setExist.add(nIndex);

                    jArrThumb.put("thumb" + nIndex + ".jpg");
                    jArrPhoto.put("avatar" + nIndex + ".jpg");
                }

                zone.mediaType = rand.nextInt(3); // 0~2 范围

                if (zone.mediaType == Zone.MEDIA_VOICE)
                {
                    zone.voiceUrl = "";
                    zone.voiceSec = rand.nextInt(1000);
                }
                else if (zone.mediaType == Zone.MEDIA_VOICE)
                {
                    zone.videoUrl = "";
                    zone.videoFaceUrl = "thumb" + (rand.nextInt(20) + 1) + ".jpg";
                    zone.videoPrice = rand.nextInt(6);
                    zone.videoPay = false;
                }
                else
                {
                    zone.mediaType = Zone.MEDIA_PHOTO;
                    zone.thumbsUrl = jArrThumb.toString();
                    zone.photosUrl = jArrPhoto.toString();
                }

                zone.anchorId = user.id;
                zone.anchorName = user.name;
                zone.anchorAvatar = user.avatar;
                zone.anchorRes = user.avatar_res;
                zone.anchorSign = user.sign;

                if (i % 2 == 0)
                {
                    zone.date = System.currentTimeMillis();
                }
                else
                {
                    zone.date = dateOneDay.getTime();
                }


                listZone.add(zone);
            }

            return listZone;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Zone> getZonesFind()
    {
        List<Zone> zoneList = new ArrayList<>();
        try
        {
            List<User> userList = getUserList();
            List<Date> dateList = getDates(20);
            List<String> thumbUrlList = new ArrayList<>();
            thumbUrlList.add("thumb1.jpg");
            thumbUrlList.add("thumb2.jpg");
            thumbUrlList.add("thumb3.jpg");
            thumbUrlList.add("thumb4.jpg");
            thumbUrlList.add("thumb5.jpg");
            thumbUrlList.add("thumb6.jpg");
            thumbUrlList.add("thumb7.jpg");
            thumbUrlList.add("thumb8.jpg");
            thumbUrlList.add("thumb9.jpg");
            for (int i = 0; i < 20; i++)
            {
                int nUserIndex = i % userList.size();
                User user = userList.get(nUserIndex);

                Zone zone = new Zone();
                zone.id = String.valueOf(i);
                zone.text = user.intro;
                zone.thumbsUrl = toJsonArray(thumbUrlList.subList(0, i % 9 + 1));
                zone.photosUrl = zone.thumbsUrl.replace("thumb", "avatar");
                zone.date = dateList.get(i).getTime();
                zone.watch = random();
                zone.anchorId = user.id;
                zone.anchorAvatar = user.avatar;
                zone.anchorName = user.name;
                zone.anchorSign = user.sign;

                zoneList.add(zone);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return zoneList;
    }

    private static Random sm_rand;
    private static int random()
    {
        try
        {
            if (sm_rand == null)
            {
                sm_rand = new Random(System.currentTimeMillis());
            }

            return Math.abs(sm_rand.nextInt());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 100;
    }

    /**
     * 输入字符串数组，输出jsonarray格式的字符串
     * @param strIn
     * @return
     */
    private static String toJsonArray(List<String> strIn)
    {
        JSONArray jsonArray = new JSONArray();
        try
        {
            for (String in : strIn)
            {
                jsonArray.put(in);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonArray.toString();
    }

    private static List<Date> getDates(int count)
    {
        List<Date> dateList = new ArrayList<>();
        try
        {
            String[] dates = new String[]{
                    "2017-02-06", "2017-02-07", "2017-02-08",
                    "2017-03-06", "2017-03-07", "2017-03-08",
                    "2017-04-06", "2017-04-07", "2017-04-08"};
            for (int i = 0; i < count; i++)
            {
                String strDate = dates[i % dates.length];
                Date dateOneDay = StringUtil.getDate(strDate);
                dateList.add(dateOneDay);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return dateList;
    }
}
