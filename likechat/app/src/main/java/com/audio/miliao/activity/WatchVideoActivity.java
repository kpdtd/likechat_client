package com.audio.miliao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.audio.miliao.R;

/**
 * 观看视频
 */
public class WatchVideoActivity extends BaseActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_video);

		try
		{
			initUI();
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
			String strKeyUrl = "url";
			if (getIntent().hasExtra(strKeyUrl))
			{
				String strUrl = getIntent().getStringExtra(strKeyUrl);

				try
				{
					final VideoView video = (VideoView) findViewById(R.id.video_anchor);
					strUrl = "http://mvvideo2.meitudata.com/58eb6e45337ec9972.mp4";
					Uri ur = Uri.parse(strUrl);
					video.setVideoURI(ur);
					video.setMediaController(new MediaController(this));
					video.start();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}