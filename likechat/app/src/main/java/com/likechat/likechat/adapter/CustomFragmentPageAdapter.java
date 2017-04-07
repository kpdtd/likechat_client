package com.likechat.likechat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.likechat.likechat.fragment.BaseFragment;

import java.util.List;

/**
 * Viewpager 跟 Fragment 结合使用的 adapter
 */
public class CustomFragmentPageAdapter extends FragmentPagerAdapter
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
