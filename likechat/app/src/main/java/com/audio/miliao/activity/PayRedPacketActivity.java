package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.library.util.ImageLoaderUtil;
import com.audio.miliao.R;

import java.util.Arrays;
import java.util.List;

/**
 * 支付红包界面
 */
public class PayRedPacketActivity extends BaseActivity
{
    private ImageView m_imgAvatar1;
    private ImageView m_imgAvatar2;
    private ImageView m_imgAvatar3;
    private ImageView m_imgAvatar4;
    private ImageView m_imgAvatar5;
    private ImageView m_imgAvatar6;
    private List<String> m_listAvatarUrl;

    public static void show(Activity activity, String[] avatarUrls)
    {
        Intent intent = new Intent(activity, PayRedPacketActivity.class);
        intent.putExtra("avatar_urls", avatarUrls);
        activity.startActivity(intent);
        // 打开activity没有动画，看起来像Dialog一样
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_red_packet);
        try
        {
            String[] avatarUrls = (String[]) getIntent().getSerializableExtra("avatar_urls");
            m_listAvatarUrl = Arrays.asList(avatarUrls);

            initUI();
            updateData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
//        super.onBackPressed();
//        // 设置关闭没有动画
//        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void initUI()
    {
        try
        {
            m_imgAvatar1 = (ImageView) findViewById(R.id.img_anchor1);
            m_imgAvatar2 = (ImageView) findViewById(R.id.img_anchor2);
            m_imgAvatar3 = (ImageView) findViewById(R.id.img_anchor3);
            m_imgAvatar4 = (ImageView) findViewById(R.id.img_anchor4);
            m_imgAvatar5 = (ImageView) findViewById(R.id.img_anchor5);
            m_imgAvatar6 = (ImageView) findViewById(R.id.img_anchor6);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId())
                    {
                    case R.id.img_close:
                        finish();
                        // 设置关闭没有动画
                        overridePendingTransition(0, 0);
                        break;
                    }
                }
            };

            findViewById(R.id.img_close).setOnClickListener(clickListener);
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
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar1, getAvatarUrl(0));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar2, getAvatarUrl(1));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar3, getAvatarUrl(2));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar4, getAvatarUrl(3));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar5, getAvatarUrl(4));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar6, getAvatarUrl(5));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getAvatarUrl(int index)
    {
        if (m_listAvatarUrl == null || m_listAvatarUrl.size() <= index)
        {
            return "";
        }

        return m_listAvatarUrl.get(index);
    }
}
