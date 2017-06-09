package com.audio.miliao.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.GsonObj;
import com.audio.miliao.theApp;
import com.audio.miliao.vo.ActorVo;

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

            setActorGenderDrawable(textView, actor.getSex(), checked);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置主播性别的图标
     * @param textView 要设置图标的View
     * @param gender 性别 1男2女
     * @param checked 使用的是选择状态的图标还是非选择状态的图标
     */
    public static void setActorGenderDrawable(TextView textView, int gender, boolean checked)
    {
        try
        {
            if (textView == null)
            {
                return;
            }

            Context context = textView.getContext();
            if (gender == 2)
            {
                int imgRes = (checked ? R.drawable.ic_female_checked : R.drawable.ic_female_normal);
                Drawable female = context.getResources().getDrawable(imgRes);
                int width = UIUtil.dip2px(context, 15);
                UIUtil.setCompoundDrawables(textView, female, 0, width);
            }
            else
            {
                int imgRes = (checked ? R.drawable.ic_male_checked : R.drawable.ic_male_normal);
                Drawable male = context.getResources().getDrawable(imgRes);
                int width = UIUtil.dip2px(context, 15);
                UIUtil.setCompoundDrawables(textView, male, 0, width);
            }
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
