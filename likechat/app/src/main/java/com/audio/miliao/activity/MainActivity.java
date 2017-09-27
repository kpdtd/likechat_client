package com.audio.miliao.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.library.event.QueryActorVoEvent;
import com.app.library.event.QueryActorVoResultEvent;
import com.app.library.util.AppChecker;
import com.app.library.util.Checker;
import com.audio.miliao.R;
import com.audio.miliao.adapter.CustomFragmentPageAdapter;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.LogoutEvent;
import com.audio.miliao.fragment.TabFindFragment;
import com.audio.miliao.fragment.TabMainFragment;
import com.audio.miliao.fragment.TabMeFragment;
import com.audio.miliao.fragment.TabMessageFragment;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchActorPage;
import com.audio.miliao.http.cmd.FetchVipMember;
import com.audio.miliao.util.NotificationUtil;
import com.audio.miliao.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity
{
    /** Fragment 列表 */
    private List<Fragment> m_listFragment;
    /** 切换各个界面 */
    private ViewPager m_pager;
    private int m_initCheckedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if (getIntent().hasExtra("come_from"))
            {
                //String strComeFrom = getIntent().getStringExtra("come_from");
                m_initCheckedIndex = 2;
            }

            initUI();
            initPager();
            setDefaultShow();
            EventBus.getDefault().register(this);

//            FetchHomeContent fetchHomeContent = new FetchHomeContent(null, null);
//            fetchHomeContent.send();

            FetchActorPage fetchActorPage = new FetchActorPage(handler(), AppData.getCurUserId(), null);
            fetchActorPage.send();

            NotificationUtil.notify(this);

            handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    int times = AppData.getAutoCallInTime(System.currentTimeMillis());
                    //theApp.showToast("times " + times);
                    if (AppChecker.isRunningForeground(getApplicationContext()))
                    {
                        if (times < 2)
                        {
                            FetchVipMember fetchVipMember = new FetchVipMember(handler(), times);
                            fetchVipMember.send();
                        }
                    }
                }
            }, 10 * 1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    private void setDefaultShow()
    {

        if (m_initCheckedIndex == 0)
        {
            // 模拟首页按钮点击
            findViewById(R.id.rdo_main).performClick();
        }
        else if(m_initCheckedIndex == 2)
        {
            // 模拟首页按钮点击
            findViewById(R.id.rdo_message).performClick();
        }

        m_pager.setCurrentItem(m_initCheckedIndex, false);
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event 切换账户（退出登录）
     */
    public void onEventMainThread(LogoutEvent event)
    {
        finish();
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event uikit发来的请求查询ActorVo的事件
     */
    public void onEventMainThread(QueryActorVoEvent event)
    {
        FetchActorPage fetchActorPage = new FetchActorPage(handler(), event.getYunxinId(), null);
        fetchActorPage.send();
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_ACTOR_PAGE:
            FetchActorPage fetchActorPage = (FetchActorPage) msg.obj;
            if (FetchActorPage.isSucceed(fetchActorPage))
            {
                EventBus.getDefault().post(new QueryActorVoResultEvent(fetchActorPage.rspActorPageVo));
            }
            break;
        case HttpUtil.RequestCode.FETCH_VIP_MEMBER:
            FetchVipMember fetchVipMember = (FetchVipMember) msg.obj;
            if (FetchVipMember.isSucceed(fetchVipMember))
            {
                TabMainFragment fragment = (TabMainFragment) m_listFragment.get(0);
                if (fragment != null && Checker.isNotEmpty(fragment.getActorVoList()))
                {
                    int times = (int) fetchVipMember.rspCallBackTag;
                    AppData.setAutoCallInTime(System.currentTimeMillis(), times + 1);
                    Random rand = new Random(System.currentTimeMillis());
                    int nIndex = rand.nextInt(fragment.getActorVoList().size());
                    AutoCallInActivity.show(MainActivity.this, fragment.getActorVoList().get(nIndex));
                }
            }
            break;
        }
    }
}
