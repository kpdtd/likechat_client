package com.audio.miliao.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;

import com.app.library.event.QueryActorVoEvent;
import com.app.library.event.QueryActorVoResultEvent;
import com.app.library.util.AppChecker;
import com.app.library.util.Checker;
import com.app.library.util.DownloadUtil;
import com.app.library.util.RandomUtil;
import com.app.library.vo.AppUpdateVo;
import com.audio.miliao.R;
import com.audio.miliao.adapter.CustomFragmentPageAdapter;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.LogoutEvent;
import com.audio.miliao.event.ShowMessageListEvent;
import com.audio.miliao.fragment.MessageListFragment;
import com.audio.miliao.fragment.TabFindFragment;
import com.audio.miliao.fragment.TabMainFragment;
import com.audio.miliao.fragment.TabMeFragment;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.CheckUpdate;
import com.audio.miliao.http.cmd.FetchActorPage;
import com.audio.miliao.http.cmd.FetchVipMember;
import com.audio.miliao.service.NotificationService;
import com.audio.miliao.widget.NoScrollViewPager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity
{
    private static boolean isRunning = false;

    /** Fragment 列表 */
    private List<Fragment> m_listFragment;
    /** 切换各个界面 */
    private ViewPager m_pager;
    private RadioButton m_rdoMain, m_rdoFind, m_rdoMessage, m_rdoMe;


    private int m_initCheckedIndex = 0;
    private Timer m_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            isRunning = true;

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

            int versionCode = (getPackageInfo() != null ? getPackageInfo().versionCode : 1);
            CheckUpdate checkUpdate = new CheckUpdate(handler(), versionCode, null);
            checkUpdate.send();

            //NotificationUtil.notify(this);

            final int times = AppData.getAutoCallInTime(System.currentTimeMillis());
            int interval = (times == 0 ? 10 : RandomUtil.nextInt(60, 300));
            //theApp.showToast("times " + times + " interval " + interval);

            handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
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
            }, interval * 1000);

            // 只要不在消息界面，隔几秒消息按钮就出现小圆点
            TimerTask timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    if (!m_rdoMessage.isChecked())
                    {
                        handler().post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
//                                int result=new java.util.Random().nextInt(10) + 1;// 返回[0,10)集合中的整数，注意不包括10
//                                try {
//                                    Thread.sleep(result * 1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                m_rdoMessage.setActivated(true);
                            }
                        });
                    }
                }
            };
            m_timer = new Timer();
            m_timer.schedule(timerTask, 2000, 16000);

            Intent intent = new Intent(this, NotificationService.class);
            startService(intent);
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
        isRunning = false;
        EventBus.getDefault().unregister(this);
        m_timer.cancel();
    }

    public static boolean isRunning()
    {
        return isRunning;
    }


    /**
     * 初始化控件
     */
    private void initUI()
    {
        try
        {
            m_pager = (NoScrollViewPager) findViewById(R.id.view_pager);
            m_rdoMain = (RadioButton) findViewById(R.id.rdo_main);
            m_rdoFind = (RadioButton) findViewById(R.id.rdo_find);
            m_rdoMessage = (RadioButton) findViewById(R.id.rdo_message);
            m_rdoMe = (RadioButton) findViewById(R.id.rdo_me);

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
                            // 发现
                            case R.id.rdo_find:
                                m_pager.setCurrentItem(1, false);
                                break;
                            // 消息
                            case R.id.rdo_message:
                                m_pager.setCurrentItem(2, false);
                                v.setActivated(false);
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

            m_rdoMain.setOnClickListener(clickListener);
            m_rdoFind.setOnClickListener(clickListener);
            m_rdoMessage.setOnClickListener(clickListener);
            m_rdoMe.setOnClickListener(clickListener);

            m_rdoMessage.setActivated(true);
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
            //m_listFragment.add(new TabMessageFragment());
            m_listFragment.add(new MessageListFragment());
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
            m_rdoMain.performClick();
        }
        else if(m_initCheckedIndex == 2)
        {
            // 模拟首页按钮点击
            m_rdoMessage.performClick();
        }

        m_pager.setCurrentItem(m_initCheckedIndex, false);
    }

    private PackageInfo getPackageInfo()
    {
        PackageManager manager = this.getPackageManager();
        try
        {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
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
     * @param event 点击通知显示消息列表
     */
    public void onEventMainThread(ShowMessageListEvent event)
    {
        m_initCheckedIndex = 2;
        setDefaultShow();
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

    public void downloadApk(AppUpdateVo appUpdateVo)
    {
        String url = appUpdateVo.getUrl();
        String fileName = "miliao_" + appUpdateVo.getVersionCode() + ".apk";
        //theApp.showToast(fileName);
        DownloadUtil.startDoanload(url, fileName, new DownloadStatusListenerV1()
        {
            @Override
            public void onDownloadComplete(DownloadRequest downloadRequest)
            {
                try
                {
                    Uri destinationUri = downloadRequest.getDestinationURI();
                    //theApp.showToast("download complete " + destinationUri);
                    //LogUtil.d(destinationUri.toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + destinationUri.toString()),
                            "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    //LogUtil.d(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage)
            {

            }

            @Override
            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress)
            {

            }
        });
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
            if (FetchVipMember.isSucceed(fetchVipMember)
                    && fetchVipMember.rspVipMember != null
                    && fetchVipMember.rspVipMember.getIsvip() == 0)
            {
                // 非VIP用户，弹出加的呼入请求界面
                TabMainFragment fragment = (TabMainFragment) m_listFragment.get(0);
                if (fragment != null && Checker.isNotEmpty(fragment.getActorVoList()))
                {
                    int times = (int) fetchVipMember.rspCallBackTag;
                    AppData.setAutoCallInTime(System.currentTimeMillis(), times + 1);
                    int nIndex = RandomUtil.nextInt(fragment.getActorVoList().size());
                    AutoCallInActivity.show(MainActivity.this, fragment.getActorVoList().get(nIndex));
                }
            }
            break;
        case HttpUtil.RequestCode.CHECK_UPDATE:
            final CheckUpdate checkUpdate = (CheckUpdate) msg.obj;
            if (CheckUpdate.isSucceed(checkUpdate) && checkUpdate.rspAppUpdate != null)
            {
                if (checkUpdate.rspAppUpdate.getIsForce())
                {
                    downloadApk(checkUpdate.rspAppUpdate);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.title_new_version);
                    builder.setMessage(checkUpdate.rspAppUpdate.getDesc());
                    builder.setPositiveButton(R.string.btn_download, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            downloadApk(checkUpdate.rspAppUpdate);
                        }
                    });
                    builder.setNegativeButton(R.string.btn_not_download, null);
                    builder.show();
                }
            }
            break;
        }
    }
}
