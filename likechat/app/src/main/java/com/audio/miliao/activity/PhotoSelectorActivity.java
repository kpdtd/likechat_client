package com.audio.miliao.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;

import com.audio.miliao.R;
import com.audio.miliao.adapter.PhotoSelectorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器
 */
public class PhotoSelectorActivity extends BaseActivity
{
	private List<String> m_listSelect = new ArrayList<>();
	private GridView m_grid;
	private PhotoSelectorAdapter m_adapter;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_selector);

		try
		{
			initUI();
			updateDate();
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
							case  R.id.img_ok:
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
			findViewById(R.id.img_ok).setOnClickListener(clickListener);

			m_grid = (GridView) findViewById(R.id.gv_image);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void updateDate()
	{
		try
		{
			String[] IMAGE_PROJECTION = {
					MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.DISPLAY_NAME,
					MediaStore.Images.Media.DATE_ADDED,
					MediaStore.Images.Media._ID};

			List<String> listPhotos = new ArrayList<>();

			// 查询相册缓存
			Cursor cData = PhotoSelectorActivity.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");

			if (cData != null) {
				int nCount = cData.getCount();

				if (nCount > 0) {
					cData.moveToFirst();
					do {
						// 查询实际的大图路径
						String strPath = cData.getString(cData.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
						listPhotos.add(strPath);
					} while (cData.moveToNext());

				}
			}

			if (m_adapter == null)
			{
				m_adapter = new PhotoSelectorAdapter(PhotoSelectorActivity.this, listPhotos, m_listSelect);
				m_grid.setAdapter(m_adapter);

				m_adapter.setOnClickListener(new PhotoSelectorAdapter.OnClickListener()
				{
					@Override
					public void onCameraClick()
					{
						//
					}

					@Override
					public void onPhotoClick(String strPath)
					{
						//
					}
				});
			}
			else
			{
				m_adapter.updateData(listPhotos);
				m_adapter.notifyDataSetChanged();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}