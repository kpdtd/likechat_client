package com.audio.miliao.util;

import android.media.MediaPlayer;

/**
 * 播放音频
 */
public class MediaPlayerUtil
{
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    public static void playVoice(String strVoiceUrl, final MediaPlayer.OnPreparedListener listener)
    {
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(strVoiceUrl);
            //mediaPlayer.prepare();
            //mediaPlayer.start();
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    // 装载完毕回调
                    mediaPlayer.start();

                    if (listener != null)
                    {
                        listener.onPrepared(mp);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void stopVoice()
    {
        mediaPlayer.stop();
    }

    public static boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }
}
