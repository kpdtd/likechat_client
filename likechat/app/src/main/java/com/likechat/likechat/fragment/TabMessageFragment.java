package com.likechat.likechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likechat.likechat.R;
import com.likechat.likechat.adapter.CustomFragmentPageAdapter;
import com.likechat.likechat.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class TabMessageFragment extends BaseFragment
{
    /** 界面中的root view */
    private View m_root;

    /** Fragment 列表 */
    private List<BaseFragment> m_listFragment;
    /** 切换各个界面 */
    private ViewPager m_pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_message, container, false);

            initUI(m_root);
            updateData();
        }

        return m_root;
    }

    private void initUI(final View root)
    {
        try
        {
            m_pager = (NoScrollViewPager) root.findViewById(R.id.view_pager);

            initPager();

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.rdo_msg_news:
                            m_pager.setCurrentItem(0, false);
                            break;
                        case R.id.rdo_call_history:
                            m_pager.setCurrentItem(1, false);
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            root.findViewById(R.id.rdo_msg_news).setOnClickListener(clickListener);
            root.findViewById(R.id.rdo_call_history).setOnClickListener(clickListener);

            root.findViewById(R.id.rdo_msg_news).performClick();
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
            m_listFragment.add(new MessageListFragment());
            m_listFragment.add(new CallHistoryListFragment());
            m_pager.setAdapter(new CustomFragmentPageAdapter(getChildFragmentManager(), m_listFragment));
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
