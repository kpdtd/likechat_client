package com.audio.miliao.widget;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.audio.miliao.R;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ListView的Header View
 */
public class HeaderView
{
    public static View load(final Activity activity, int layoutId, final List<String> urlList)
    {
        try
        {
            View view = View.inflate(activity, layoutId, null);
            final ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rgp_indicator);

            final List<View> viewList = new ArrayList<>();
            final List<View> radioList = new ArrayList<>();

            int width = UIUtil.dip2px(activity, 8);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(width, width);
            params.rightMargin = width / 2;
            for (int i = 0; i < urlList.size(); i++)
            {
                ImageView imageView = (ImageView) View.inflate(activity, R.layout.layout_banner, null);
                viewList.add(imageView);
                RadioButton radio = (RadioButton) View.inflate(activity, R.layout.layout_indicator, null);
                radioList.add(radio);

                radioGroup.addView(radio, params);
            }

            pager.setAdapter(new PagerAdapter()
            {
                @Override
                public int getCount()
                {
                    return (urlList != null ? urlList.size() : 0);
                }

                @Override
                public boolean isViewFromObject(View view, Object object)
                {
                    return view == object;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object)
                {
                    //super.destroyItem(container, position, object);

                    //删除页卡
                    container.removeView(viewList.get(position));
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position)
                {
                    //return super.instantiateItem(container, position);
                    String url = urlList.get(position);
                    ImageView view = (ImageView) viewList.get(position);
                    ImageLoaderUtil.displayListAvatarImageFromAsset(view, url);

                    //添加页卡
                    container.addView(view, 0);
                    return view;
                }
            });

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
            {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                {

                }

                @Override
                public void onPageSelected(int position)
                {
                    // 一个新页被调用时执行,仍为原来的page时，该方法不被调用
                    try
                    {
                        RadioButton radioButton = (RadioButton) radioList.get(position);
                        radioButton.setChecked(true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state)
                {
                    /**
                     * SCROLL_STATE_IDLE: pager处于空闲状态<br/>
                     * SCROLL_STATE_DRAGGING： pager处于正在拖拽中 <br/>
                     * SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                     */
                }
            });

            if (UIUtil.isListNotEmpty(radioList))
            {
                RadioButton radioButton = (RadioButton) radioList.get(0);
                radioButton.setChecked(true);
            }

            final Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    try
                    {
                        activity.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    int curItem = pager.getCurrentItem();
                                    if (curItem == urlList.size() - 1)
                                    {
                                        pager.setCurrentItem(0);
                                    }
                                    else
                                    {
                                        pager.setCurrentItem(curItem + 1);
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 3000, 3000);

            return view;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
