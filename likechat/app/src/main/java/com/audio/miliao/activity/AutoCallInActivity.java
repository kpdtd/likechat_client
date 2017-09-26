package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.ActorVo;
import com.audio.miliao.R;

/**
 * 当新用户登录进来时，是没有购买钻和VIP的
 * 此时要假弹出通话请求界面
 * 以刺激用户购买通话金币
 */
public class AutoCallInActivity extends BaseActivity
{
    private ActorVo mActorVo;
    private TextView m_txtName;
    private ImageView m_imgAvatar;
    private MediaPlayer m_mediaPlayer;

    public static void show(Activity activity, ActorVo actorVo)
    {
        Intent intent = new Intent(activity, AutoCallInActivity.class);
        intent.putExtra("actor_vo", actorVo);
        activity.startActivity(intent);
        // 打开activity没有动画，看起来像Dialog一样
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_in);
        try
        {
            mActorVo = (ActorVo) getIntent().getSerializableExtra("actor_vo");

            initUI();
            updateData();

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    playNotify();
                }
            }).start();
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
        stopNotify();
    }

    private void initUI()
    {
        try
        {
            m_txtName = (TextView) findViewById(R.id.txt_name);
            m_imgAvatar = (ImageView) findViewById(R.id.img_avatar);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId())
                    {
                    case R.id.txt_hang_up:
                        finish();
                        // 设置关闭没有动画
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.txt_answer:
                        SimpleBalanceActivity.show(AutoCallInActivity.this);
                        break;
                    }
                }
            };

            findViewById(R.id.txt_hang_up).setOnClickListener(clickListener);
            findViewById(R.id.txt_answer).setOnClickListener(clickListener);
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
            if (mActorVo != null)
            {
                m_txtName.setText(mActorVo.getNickname());
                ImageLoaderUtil.displayListAvatarImage(m_imgAvatar, mActorVo.getIcon());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void playNotify()
    {
        try
        {
            m_mediaPlayer = MediaPlayer.create(this,R.raw.auto_call_in);
            m_mediaPlayer.setLooping(true);
            m_mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    // 装载完毕回调
                    m_mediaPlayer.start();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void stopNotify()
    {
        try
        {
            if (m_mediaPlayer != null && m_mediaPlayer.isPlaying())
            {
                m_mediaPlayer.stop();
                m_mediaPlayer.release();
                m_mediaPlayer = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
