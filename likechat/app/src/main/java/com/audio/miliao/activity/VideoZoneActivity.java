package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.audio.miliao.R;

/**
 * 视频动态
 */
public class VideoZoneActivity extends BaseActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_zone);

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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}