package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.photoview.HackyViewPager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览
 */
public class ImageBrowseActivity extends Activity {
	private static final String STATE_POSITION = "STATE_POSITION";

	protected ImageLoader       m_iLoader    = ImageLoader.getInstance();
	private HackyViewPager      m_hViewPager = null;
	private TextView            m_txtCount   = null;
	private List<String>        m_lstImage   = new ArrayList<String>();

	private int m_nPos   = 0;
	private int m_nCount = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_browse);

		initImageLoader(this);

		String strKeyPos = "pos", strKeyUrls = "urls";
		if (getIntent().hasExtra(strKeyUrls)){
			String strUrls = getIntent().getStringExtra(strKeyUrls);
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray((null == strUrls || "".equals(strUrls)) ? "[]" : strUrls);
				m_nCount = jsonArray.length();
				m_nPos = getIntent().hasExtra(strKeyPos) ? getIntent().getIntExtra(strKeyPos, 1) : 1;

				if (m_nCount == 1){
					m_lstImage.add(jsonArray.getString(0));
				}else{
					// 多张图片才会循环
					// 头部添加最后一张图片
					m_lstImage.add(jsonArray.getString(m_nCount - 1));

					for(int i = 0; i < m_nCount; i ++)
					{
						m_lstImage.add(jsonArray.getString(i));
					}

					// 尾部添加第一张图片
					m_lstImage.add(jsonArray.getString(0));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		m_txtCount = (TextView)findViewById(R.id.txt_count);
		m_txtCount.setText("" + (m_nCount > 0 ? (m_nPos + 1) : 0) + "/" + m_nCount);;

		m_hViewPager = (HackyViewPager) findViewById(R.id.hvp_pager);
		m_hViewPager.setAdapter(new com.audio.miliao.adapter.ImagePagerAdapter(this, m_iLoader, m_lstImage));
		m_hViewPager.setCurrentItem(m_nCount > 1 ? (m_nPos + 1) : m_nPos);
		m_hViewPager.setOnPageChangeListener(new HackyViewPager.OnPageChangeListener(){
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int position, float pesent, int px) {
			}

			@Override
			public void onPageSelected(int position) {
				// 多张图片才会循环
				if (m_nCount > 1) {
					if ( position < 1) {
						// 首位之前，跳转到末尾(N)
						// false 不显示跳转过程的动画
						position = m_lstImage.size() - 2;
						m_hViewPager.setCurrentItem(position, false);
					} else if ( position > m_lstImage.size() - 2) {
						// 末位之后，跳转到首位
						position = 1;
						m_hViewPager.setCurrentItem(position, false);
					}

					m_txtCount.setText("" + (position) + "/" + (m_nCount));
				}
				else{
					m_txtCount.setText("" + (m_nCount > 0 ? 1 : 0) + "/" + (m_nCount));
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	public  void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, m_hViewPager.getCurrentItem());
	}
}