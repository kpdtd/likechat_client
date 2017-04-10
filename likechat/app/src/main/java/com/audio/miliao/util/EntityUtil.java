package com.audio.miliao.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.User;

/**
 * 放置经常使用的跟Entity和UI相关的代码
 */
public class EntityUtil
{
    /**
     * 设置主播性别的图标
     * @param textView 要设置图标的View
     * @param user
     * @param checked 使用的是选择状态的图标还是非选择状态的图标
     */
    public static void setAnchorGenderDrawable(TextView textView, User user, boolean checked)
    {
        try
        {
            if (textView == null || user == null)
            {
                return;
            }

            Context context = textView.getContext();
            if (user.gender == User.GENDER_FEMALE)
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
}
