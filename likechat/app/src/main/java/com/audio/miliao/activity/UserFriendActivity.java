package com.audio.miliao.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.audio.miliao.R;
import com.audio.miliao.adapter.CustomFragmentPageAdapter;
import com.audio.miliao.fragment.FansFragment;
import com.audio.miliao.fragment.FriendsFragment;
import com.audio.miliao.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友
 */
public class UserFriendActivity extends BaseActivity
{
    private RadioButton m_rdoFollow;
    private RadioButton m_rdoFans;

    /** 切换各个界面 */
    private ViewPager m_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friend);

        try
        {
            initUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initUI()
    {
        try
        {
            m_rdoFollow = (RadioButton) findViewById(R.id.rdo_follow);
            m_rdoFans = (RadioButton) findViewById(R.id.rdo_fans);
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
                        case R.id.img_back:
                            finish();
                            break;
                        case R.id.rdo_follow:
                            m_pager.setCurrentItem(0, false);
                            break;
                        case R.id.rdo_fans:
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

            initPager();

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.rdo_follow).setOnClickListener(clickListener);
            findViewById(R.id.rdo_fans).setOnClickListener(clickListener);

            findViewById(R.id.rdo_follow).performClick();
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
            List<Fragment> fragments = new ArrayList<>();
            fragments.add(new FriendsFragment());
            fragments.add(new FansFragment());
            m_pager.setAdapter(new CustomFragmentPageAdapter(getSupportFragmentManager(), fragments));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateFriendCount(int follow, int fans)
    {
        try
        {
            String strFollow = getString(R.string.txt_user_friend_follow) + "(" + follow + ")";
            m_rdoFollow.setText(strFollow);

            String strFans = getString(R.string.txt_user_friend_fans) + "(" + fans + ")";
            m_rdoFans.setText(strFans);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
