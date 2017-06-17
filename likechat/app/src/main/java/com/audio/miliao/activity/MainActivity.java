package com.audio.miliao.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.audio.miliao.R;
import com.audio.miliao.adapter.CustomFragmentPageAdapter;
import com.audio.miliao.fragment.TabFindFragment;
import com.audio.miliao.fragment.TabMainFragment;
import com.audio.miliao.fragment.TabMeFragment;
import com.audio.miliao.fragment.TabMessageFragment;
import com.audio.miliao.http.cmd.FetchHomeContent;
import com.audio.miliao.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
{
    /** Fragment 列表 */
    private List<Fragment> m_listFragment;
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

            FetchHomeContent fetchHomeContent = new FetchHomeContent(null, null);
            fetchHomeContent.send();
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
}
