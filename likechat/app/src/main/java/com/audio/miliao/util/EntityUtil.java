package com.audio.miliao.util;

import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.GsonObj;
import com.audio.miliao.theApp;
import com.audio.miliao.vo.ActorVo;
import com.netease.nim.uikit.util.ViewsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * 放置经常使用的跟Entity和UI相关的代码
 */
public class EntityUtil
{
    /**
     * 设置主播性别的图标
     * @param textView 要设置图标的View
     * @param actor
     * @param checked 使用的是选择状态的图标还是非选择状态的图标
     */
    public static void setActorGenderDrawable(TextView textView, ActorVo actor, boolean checked)
    {
        try
        {
            if (textView == null || actor == null)
            {
                return;
            }

            ViewsUtil.setActorGenderDrawable(textView, actor.getSex(), checked);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setActorGenderText(TextView textView, int gender)
    {
        String strText = (gender == 1 ?
                theApp.CONTEXT.getString(R.string.txt_male) :
                theApp.CONTEXT.getString(R.string.txt_female));
        textView.setText(strText);
    }

    public static <T extends GsonObj<T>> void parseList(JSONArray jsonArray, List<T> list, Class<T> cls)
    {
        int len = jsonArray.length();
        for (int i= 0; i < len; i++)
        {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            T obj = T.parse(jsonObject.toString(), cls);
            list.add(obj);
        }
    }
}
