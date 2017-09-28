package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.adapter.ImagePagerAdapter;
import com.audio.miliao.photoview.HackyViewPager;
import com.audio.miliao.photoview.PhotoViewAttacher.OnViewTapListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览
 */
public class ImageBrowseActivity extends HandleNotificationActivity
{
    private static final String STATE_POSITION = "STATE_POSITION";
    private HackyViewPager m_hViewPager = null;
    private TextView m_txtCount = null;
    private List<String> m_lstImage = new ArrayList<String>();
    private ImagePagerAdapter m_adapter = null;

    private int m_nPos = 0;
    private int m_nCount = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);

        try
        {
            initData();
            updateData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        try
        {
            outState.putInt(STATE_POSITION, m_hViewPager.getCurrentItem());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private OnViewTapListener m_tapListener = new OnViewTapListener()
    {
        @Override
        public void onViewTap(View view, float x, float y)
        {
            try
            {
                // 退出浏览
                finish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    private void initData()
    {
        try
        {
            String strKeyPos = "pos", strKeyUrls = "urls";
            if (getIntent().hasExtra(strKeyUrls))
            {
                //String strUrls = getIntent().getStringExtra(strKeyUrls);
                String[] strUrls = getIntent().getStringArrayExtra(strKeyUrls);
                //JSONArray jsonArray;
                try
                {
                    //jsonArray = new JSONArray((null == strUrls || "".equals(strUrls)) ? "[]" : strUrls);
                    m_nCount = strUrls.length;
                    m_nPos = getIntent().hasExtra(strKeyPos) ? getIntent().getIntExtra(strKeyPos, 1) : 1;

                    if (m_nCount == 1)
                    {
                        m_lstImage.add(strUrls[0]);
                    }
                    else
                    {
                        // 多张图片才会循环
                        // 头部添加最后一张图片
                        m_lstImage.add(strUrls[m_nCount - 1]);

                        for (int i = 0; i < m_nCount; i++)
                        {
                            m_lstImage.add(strUrls[i]);
                        }

                        // 尾部添加第一张图片
                        m_lstImage.add(strUrls[0]);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            m_txtCount = (TextView) findViewById(R.id.txt_count);
            m_txtCount.setText("" + (m_nCount > 0 ? (m_nPos + 1) : 0) + "/" + m_nCount);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateData()
    {
        try
        {
            m_hViewPager = (HackyViewPager) findViewById(R.id.hvp_pager);

            if (null == m_adapter)
            {
                m_adapter = new com.audio.miliao.adapter.ImagePagerAdapter(this, m_lstImage);
                m_hViewPager.setAdapter(m_adapter);
                m_hViewPager.setCurrentItem(m_nCount > 1 ? (m_nPos + 1) : m_nPos);
                m_hViewPager.setOnPageChangeListener(new HackyViewPager.OnPageChangeListener()
                {
                    @Override
                    public void onPageScrollStateChanged(int arg0)
                    {
                    }

                    @Override
                    public void onPageScrolled(int position, float pesent, int px)
                    {
                    }

                    @Override
                    public void onPageSelected(int position)
                    {
                        // 多张图片才会循环
                        if (m_nCount > 1)
                        {
                            if (position < 1)
                            {
                                // 首位之前，跳转到末尾(N)
                                // false 不显示跳转过程的动画
                                position = m_lstImage.size() - 2;
                                m_hViewPager.setCurrentItem(position, false);
                            }
                            else if (position > m_lstImage.size() - 2)
                            {
                                // 末位之后，跳转到首位
                                position = 1;
                                m_hViewPager.setCurrentItem(position, false);
                            }

                            m_txtCount.setText("" + (position) + "/" + (m_nCount));
                        }
                        else
                        {
                            m_txtCount.setText("" + (m_nCount > 0 ? 1 : 0) + "/" + (m_nCount));
                        }
                    }
                });

                m_adapter.setOnViewTapListener(m_tapListener);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}