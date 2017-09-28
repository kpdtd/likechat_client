package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.util.ViewsUtil;
import com.app.library.vo.ActorPageVo;
import com.audio.miliao.R;

/**
 * 聊天——呼出
 */
public class ChatVoiceCallOutActivity extends HandleNotificationActivity
{
    private ActorPageVo m_actorPageVo;
    private MediaPlayer m_mediaPlayer;

    public static void show(Activity activity, ActorPageVo actorPageVo)
    {
        Intent intent = new Intent(activity, ChatVoiceCallOutActivity.class);
        intent.putExtra("actor_page_vo", actorPageVo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_voice_call_out);
        try
        {
            m_actorPageVo = (ActorPageVo) getIntent().getSerializableExtra("actor_page_vo");

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
    protected void onDestroy()
    {
        super.onDestroy();
        stopNotify();
    }

    private void initUI()
    {
        try
        {
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
                        case R.id.txt_hang_up2:
                            finish();
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.txt_hang_up2).setOnClickListener(clickListener);
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
            if (m_actorPageVo == null)
            {
                return;
            }

            ImageView imgInfo = (ImageView) findViewById(R.id.img_info_bg);
            ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            TextView txtAge = (TextView) findViewById(R.id.txt_age);
            TextView txtName = (TextView) findViewById(R.id.txt_name);
            TextView txtTalkTime = (TextView) findViewById(R.id.txt_talk_time);
            TextView txtTalkPrice = (TextView) findViewById(R.id.txt_call_rate1);
            TextView txtTalkPlatformPrice = (TextView) findViewById(R.id.txt_call_rate2);
            TextView txtTalkTotalPrice = (TextView) findViewById(R.id.txt_call_rate3);

            ImageLoaderUtil.displayListAvatarImage(imgInfo, m_actorPageVo.getIcon());
            ImageLoaderUtil.displayListAvatarImage(imgAvatar, m_actorPageVo.getIcon());
            txtAge.setText(m_actorPageVo.getAge());
            txtName.setText(m_actorPageVo.getNickname());
            //txtTalkTime.setText("00:00");
            ViewsUtil.setActorGenderDrawable(txtAge, m_actorPageVo.getSex(), true);

            txtTalkPrice.setText(m_actorPageVo.getPrice() + getString(R.string.txt_call_out_bill_1));
            txtTalkPlatformPrice.setText(m_actorPageVo.getPlatformPrice() + getString(R.string.txt_call_out_bill_2));
            txtTalkTotalPrice.setText(m_actorPageVo.getTotalPrice() + getString(R.string.txt_call_out_bill_3));
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
            m_mediaPlayer = MediaPlayer.create(this,R.raw.avchat_connecting);
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
