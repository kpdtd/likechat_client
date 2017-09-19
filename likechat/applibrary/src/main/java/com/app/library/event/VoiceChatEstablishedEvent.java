package com.app.library.event;

/**
 * Created by liujiye-pc on 2017/8/8.
 *
 * 语音通话接通
 */
public class VoiceChatEstablishedEvent
{
    public boolean mIsInComingCall;

    public VoiceChatEstablishedEvent(boolean isInComingCall)
    {
        mIsInComingCall = isInComingCall;
    }

    public boolean isInComingCall()
    {
        return mIsInComingCall;
    }
}
