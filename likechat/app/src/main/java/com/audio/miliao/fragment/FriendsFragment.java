package com.audio.miliao.fragment;

import android.os.Message;

import com.audio.miliao.R;
import com.audio.miliao.activity.UserFriendActivity;
import com.audio.miliao.adapter.FriendAdapter;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchMyFriends;
import com.audio.miliao.theApp;
import com.app.library.vo.ActorVo;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends BaseFootableFragment<ActorVo>
{
    private List<ActorVo> m_actorVos = new ArrayList<>();
    private FriendAdapter mAdapter = null;

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_friends;
    }

    /**
     * 获取数据
     *
     * @param stamp
     */
    @Override
    public void fetchData(String stamp)
    {
        FetchMyFriends fetchMyFriends = new FetchMyFriends(handler(), stamp, null);
        fetchMyFriends.send();
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
        case HttpUtil.RequestCode.FETCH_MY_FRIENDS:
            FetchMyFriends fetchMyFriends = (FetchMyFriends) msg.obj;
            if (FetchMyFriends.isSucceed(fetchMyFriends))
            {
                m_actorVos.addAll(fetchMyFriends.rspFriends);
                setHasNextPage(fetchMyFriends.rspHasNext);
                setStamp(fetchMyFriends.rspStamp);
                updateData();
                UserFriendActivity activity = (UserFriendActivity) getActivity();
                if (activity != null)
                {
                    activity.updateFriendCount(fetchMyFriends.rspAttentionCount, fetchMyFriends.rspFansCount);
                }
            }
            else
            {
                theApp.showToast(getString(R.string.toast_fetch_friends_failed));
            }
            break;
        }
    }
}
