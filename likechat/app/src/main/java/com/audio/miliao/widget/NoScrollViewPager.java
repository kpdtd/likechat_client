package com.audio.miliao.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止左右滑动的ViewPager<br/>
 * onTouchEvent和onInterceptTouchEvent返回false即可
 */
public class NoScrollViewPager extends ViewPager
{
	public NoScrollViewPager(Context context)
	{
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0)
	{
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
		return false;
	}

	@Override
	public void scrollTo(int x, int y)
	{
		super.scrollTo(x, y);
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll)
	{
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item)
	{
		super.setCurrentItem(item);
	}
}
