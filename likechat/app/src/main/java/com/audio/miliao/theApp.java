package com.audio.miliao;

import android.accounts.Account;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.app.library.util.DownloadUtil;
import com.app.library.util.ImageLoaderUtil;
import com.app.library.util.LogUtil;
import com.app.library.util.PreferUtil;
import com.app.library.util.UIUtil;
import com.app.library.vo.ActorPageVo;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.util.DBUtil;

public class theApp extends Application
{
    public static Context CONTEXT = null;
    public static Handler sm_handler = new Handler();

    public static final Account TEST3 = new Account("liu1501134", "e10adc3949ba59abbe56e057f20f883e");
    public static final Account TEST4 = new Account("18178619319", "e10adc3949ba59abbe56e057f20f883e");

    private static Account mCurAccount = TEST3;

    @Override
    public void onCreate()
    {
        super.onCreate();

        CONTEXT = this;
        initUtil();

        if (AppData.isLogin())
        {
            saveCurUser();
        }

        LogUtil.d("psuedoId:" + getUniqueID());
    }

    private void initUtil()
    {
        PreferUtil.init(CONTEXT);
        ImageLoaderUtil.init(CONTEXT);
        DBUtil.init(CONTEXT);
        DownloadUtil.init(CONTEXT);
    }

    public static void saveCurUser()
    {
        try
        {
            if (AppData.getCurUser() == null)
            {
                ActorPageVo actor = new ActorPageVo();
                actor.setId(12345678);
                actor.setNickname("我是大管家");
                actor.setIcon("avatar1.jpg");
                actor.setSignature("管家就是要有管家的样子，别拿管家不当干部");
                actor.setIntroduction("我是大管家，我要管好整个家族");
                actor.setAge("23");
                actor.setProvince("四川");
                actor.setCity("成都");
                actor.setFans("10043");
                actor.setAttention("205");
                AppData.setCurUser(actor);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static Account getCurAccount()
    {
        return mCurAccount;
    }

    public static void setCurAccount(Account account)
    {
        mCurAccount = account;
    }

    public static void showToast(final String strToast)
    {
        sm_handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                UIUtil.showToastLong(CONTEXT, strToast);
            }
        });
    }

    /**
     * 获得独一无二的伪 ID
     * @return
     */
    public static String getUniqueID()
    {
        String serial;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 // 主板
                + Build.BRAND.length() % 10  // android系统定制商
                + Build.CPU_ABI.length() % 10  // cpu指令集
                + Build.DEVICE.length() % 10 // 设备参数
                + Build.DISPLAY.length() % 10 // 显示屏参数
                + Build.HOST.length() % 10
                + Build.ID.length() % 10 // 修订版本列表
                + Build.MANUFACTURER.length() % 10 // 硬件制造商
                + Build.MODEL.length() % 10 // 版本
                + Build.PRODUCT.length() % 10 // 手机制造商
                + Build.TAGS.length() % 10 // 描述build的标签
                + Build.TYPE.length() % 10 // builder类型
                + Build.USER.length() % 10; //13 位

        try
        {
            // API >=9 通过“Build.SERIAL”这个属性来保证ID的独一无二
            serial = Build.SERIAL;
        }
        catch (Exception exception)
        {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }

        LogUtil.d("devId:" + m_szDevIDShort);
        //使用硬件信息拼凑出来的15位号码
//        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        return serial;
    }
}
