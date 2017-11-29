package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.ActorVo;
import com.audio.miliao.R;

/**
 * 每当用户重新启动APP时
 * 在首页都会弹出打招呼弹窗
 * 除非用户已经发过一次红包（不再弹）
 */
public class AutoSayHelloActivity extends BaseActivity
{
    private ActorVo mActorVo;
    private TextView m_txtName;
    private ImageView m_imgAvatar;
    //private MediaPlayer m_mediaPlayer;

    public static void show(Activity activity, ActorVo actorVo)
    {
        Intent intent = new Intent(activity, AutoSayHelloActivity.class);
        intent.putExtra("actor_vo", actorVo);
        activity.startActivity(intent);
        // 打开activity没有动画，看起来像Dialog一样
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_say_hello);
        try
        {
            mActorVo = (ActorVo) getIntent().getSerializableExtra("actor_vo");

            initUI();
            updateData();

//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    playNotify();
//                }
//            }).start();
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
//        stopNotify();
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
                    case R.id.img_close:
                        finish();
                        // 设置关闭没有动画
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.txt_answer:
                        SimpleBalanceActivity.show(AutoSayHelloActivity.this);
                        break;
                    }
                }
            };

            findViewById(R.id.txt_hang_up).setOnClickListener(clickListener);
            findViewById(R.id.txt_answer).setOnClickListener(clickListener);
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

//    private void playNotify()
//    {
//        try
//        {
//            m_mediaPlayer = MediaPlayer.create(this,R.raw.auto_call_in);
//            m_mediaPlayer.setLooping(true);
//            m_mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
//            {
//                @Override
//                public void onPrepared(MediaPlayer mp)
//                {
//                    // 装载完毕回调
//                    m_mediaPlayer.start();
//                }
//            });
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void stopNotify()
//    {
//        try
//        {
//            if (m_mediaPlayer != null && m_mediaPlayer.isPlaying())
//            {
//                m_mediaPlayer.stop();
//                m_mediaPlayer.release();
//                m_mediaPlayer = null;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
