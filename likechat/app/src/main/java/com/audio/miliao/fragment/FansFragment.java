package com.audio.miliao.fragment;

import android.os.Message;

import com.audio.miliao.R;
import com.audio.miliao.activity.UserFriendActivity;
import com.audio.miliao.adapter.FriendAdapter;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchMyFans;
import com.audio.miliao.theApp;
import com.netease.nim.uikit.miliao.vo.ActorVo;

import java.util.ArrayList;
import java.util.List;

public class FansFragment extends BaseFootableFragment<ActorVo>
{
    private List<ActorVo> m_actorVos = new ArrayList<>();
    private FriendAdapter mAdapter = null;

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_fans;
    }

    /**
     * 获取数据
     *
     * @param stamp
     */
    @Override
    public void fetchData(String stamp)
    {
        FetchMyFans fetchMyFans = new FetchMyFans(handler(), stamp, null);
        fetchMyFans.send();
    }

    /**
     * 返回数据
     *
     * @return
     */
    @Override
    public List<ActorVo> datas()
    {
        return m_actorVos;
    }

    /**
     * 返回adapter
     *
     * @return
     */
    @Override
    public BaseUpdateAdapter getAdapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new FriendAdapter(getActivity(), m_actorVos);
        }
        return mAdapter;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_MY_FANS:
            FetchMyFans fetchMyFans = (FetchMyFans) msg.obj;
            if (FetchMyFans.isSucceed(fetchMyFans))
            {
                m_actorVos.addAll(fetchMyFans.rspFanses);
                setHasNextPage(fetchMyFans.rspHasNext);
                setStamp(fetchMyFans.rspStamp);
                updateData();
                UserFriendActivity activity = (UserFriendActivity) getActivity();
                if (activity != null)
                {
                    activity.updateFriendCount(fetchMyFans.rspAttentionCount, fetchMyFans.rspFansCount);
                }
            }
            else
            {
                theApp.showToast(getString(R.string.toast_fetch_fans_failed));
            }
            break;
        }
    }
}
