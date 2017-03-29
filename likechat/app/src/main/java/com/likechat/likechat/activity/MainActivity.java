package com.likechat.likechat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.likechat.likechat.R;
import com.likechat.likechat.fragment.BaseFragment;
import com.likechat.likechat.fragment.TabFindFragment;
import com.likechat.likechat.fragment.TabMainFragment;
import com.likechat.likechat.fragment.TabMeFragment;
import com.likechat.likechat.fragment.TabMessageFragment;
import com.likechat.likechat.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
{
    /** Fragment 列表 */
    private List<BaseFragment> m_listFragment;
    /** 切换各个界面 */
    private ViewPager m_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initUI();
            initPager();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 初始化控件
     */
    private void initUI()
    {
        try
        {
            m_pager = (NoScrollViewPager) findViewById(R.id.view_pager);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                            // 首页
                            case R.id.rdo_main:
                                m_pager.setCurrentItem(0, false);
                                break;

                            // 车况
                            case R.id.rdo_find:
                                m_pager.setCurrentItem(1, false);
                                break;

                            // 行程
                            case R.id.rdo_message:
                                m_pager.setCurrentItem(2, false);
                                break;

                            // 我的
                            case R.id.rdo_me:
                                m_pager.setCurrentItem(3, false);
                                break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.rdo_main).setOnClickListener(clickListener);
            findViewById(R.id.rdo_find).setOnClickListener(clickListener);
            findViewById(R.id.rdo_message).setOnClickListener(clickListener);
            findViewById(R.id.rdo_me).setOnClickListener(clickListener);

            // 模拟首页按钮点击
            findViewById(R.id.rdo_main).performClick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 初始化ViewPager
     */
    private void initPager()
    {
        try
        {
            m_listFragment = new ArrayList<>();
            m_listFragment.add(new TabMainFragment());
            m_listFragment.add(new TabFindFragment());
            m_listFragment.add(new TabMessageFragment());
            m_listFragment.add(new TabMeFragment());
            m_pager.setAdapter(new CustomFragmentPageAdapter(getSupportFragmentManager(), m_listFragment));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 自定义 FragmentPagerAdapter
     */
    class CustomFragmentPageAdapter extends FragmentPagerAdapter
    {
        private List<BaseFragment> m_listFragment;

        public CustomFragmentPageAdapter(FragmentManager fm, List<BaseFragment> listFragment)
        {
            super(fm);

            m_listFragment = listFragment;
        }

        @Override
        public Fragment getItem(int position)
        {
            try
            {
                if (m_listFragment != null)
                {
                    return m_listFragment.get(position);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            try
            {
                if (m_listFragment != null)
                {
                    return m_listFragment.size();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return 0;
        }

    }
}
