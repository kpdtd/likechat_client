package com.audio.miliao.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.audio.miliao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片动态
 */
public class PhotoZoneActivity extends HandleNotificationActivity
{
	private List<ImageView> m_listThumbs;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_zone);

		try
		{
			initUI();

			// 隐藏输入法, 在弹出输入法界面时调整原来的布局
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	private  void initUI()
	{
		try
		{
			View.OnClickListener clickListener = new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try
					{
						switch (v.getId())
						{
							case R.id.img_back:
								finish();
								break;
							case  R.id.txt_publish:
								break;
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			};

			findViewById(R.id.img_back).setOnClickListener(clickListener);
			findViewById(R.id.txt_publish).setOnClickListener(clickListener);

			m_listThumbs = new ArrayList<>();
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb1));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb2));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb3));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb4));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb5));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb6));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb7));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb8));
			m_listThumbs.add((ImageView) findViewById(R.id.img_thumb9));


			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);
			int width = metric.widthPixels;     // 屏幕宽度（像素）
			int nSpace = -1;
			GridLayout.LayoutParams para = null;

			for(int i = 0; i < m_listThumbs.size(); i++)
			{
				View view = m_listThumbs.get(i);

				if (nSpace < 0)
				{
					//获取按钮的布局
					para = (GridLayout.LayoutParams) view.getLayoutParams();
					nSpace = para.leftMargin;
					width = (width - nSpace * 2 * 3) / 3;
				}

				setViewSize(view, width, width);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 设置控件大小
	 * @param view
	 * @param width
	 * @param height
	 */
	private void setViewSize(View view, int width, int height)
	{
		try
		{
			//获取按钮的布局
			GridLayout.LayoutParams para = (GridLayout.LayoutParams) view.getLayoutParams();
			para.height = height;
			para.width  = width;
			view.setLayoutParams(para);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}