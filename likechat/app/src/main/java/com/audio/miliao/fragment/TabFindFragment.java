package com.audio.miliao.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.audio.miliao.R;
import com.audio.miliao.activity.ImageBrowseActivity;
import com.audio.miliao.activity.WatchVideoActivity;
import com.audio.miliao.adapter.ActorDynamicAdapter;
import com.audio.miliao.algorithm.SortByDate;
import com.audio.miliao.algorithm.SortByFollow;
import com.audio.miliao.algorithm.SortByWatch;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchFindList;
import com.audio.miliao.util.StringUtil;
import com.audio.miliao.util.UIUtil;
import com.audio.miliao.vo.ActorDynamicVo;

import java.util.ArrayList;
import java.util.List;

public class TabFindFragment extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private ListView m_list;
    private ActorDynamicAdapter m_adapter;
    private List<ActorDynamicVo> m_actorDynamicVos = new ArrayList<>();
    /**
     * 最新
     */
    private SortByDate m_sortByDate = new SortByDate();
    /**
     * 热门
     */
    private SortByWatch m_sortByWatch = new SortByWatch();
    /**
     * 关注
     */
    private SortByFollow m_sortByFollow = new SortByFollow();
    /**
     * 默认获取最新动态
     */
    private int mFetchFindListTag = FetchFindList.LATEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_find, container, false);

            initUI(m_root);
            fetchFindList();
            //updateData();
        }

        return m_root;
    }

    private void initUI(final View root)
    {
        try
        {
            m_list = (ListView) root.findViewById(R.id.list);

            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        // 返回值应该不是ChatMessage, 应该是Zone
                        /*
                        Intent intentChat = new Intent(getActivity(), ChatTextActivity.class);
                        User user = (m_adapter.getItem(position) == null ? null : ((ChatMessage) m_adapter.getItem(position)).from);
                        intentChat.putExtra("user", user);
                        startActivity(intentChat);*/
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            m_list.setOnScrollListener(new AbsListView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState)
                {
                    try
                    {
                        switch (scrollState)
                        {
                        //停止滚动
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            m_adapter.setScrolling(false);
                            m_adapter.notifyDataSetChanged();
                            break;
                        //滚动做出了抛的动作
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            m_adapter.setScrolling(true);
                            break;
                        //正在滚动
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            m_adapter.setScrolling(true);
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
                {

                }
            });

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        // 最新
                        case R.id.rdo_find_latest:
                            //m_adapter.updateData(m_sortByDate.sort(m_adapter.getZones()));
                            //m_adapter.notifyDataSetChanged();
                            mFetchFindListTag = FetchFindList.LATEST;
                            fetchFindList();
                            break;
                        // 热门
                        case R.id.rdo_find_hot:
                            //m_adapter.updateData(m_sortByWatch.sort(m_adapter.getZones()));
                            //m_adapter.notifyDataSetChanged();
                            mFetchFindListTag = FetchFindList.HOT;
                            fetchFindList();
                            break;
                        // 关注
                        case R.id.rdo_find_follow:
                            //m_adapter.updateData(m_sortByFollow.sort(m_adapter.getZones()));
                            //m_adapter.notifyDataSetChanged();
                            mFetchFindListTag = FetchFindList.FOCUS;
                            fetchFindList();
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            root.findViewById(R.id.rdo_find_latest).setOnClickListener(clickListener);
            root.findViewById(R.id.rdo_find_hot).setOnClickListener(clickListener);
            root.findViewById(R.id.rdo_find_follow).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void fetchFindList()
    {
        FetchFindList fetchFindList = new FetchFindList(handler(), mFetchFindListTag, "0", null);
        fetchFindList.send();
    }

    private void updateData()
    {
        try
        {
            //List<Zone> zoneList = DebugUtil.getZonesFind();
            if (m_adapter == null)
            {
                m_adapter = new ActorDynamicAdapter(getActivity(), m_actorDynamicVos);
                m_list.setAdapter(m_adapter);

                m_adapter.setOnClickListener(new ActorDynamicAdapter.OnClickListener()
                {
                    @Override
                    public void onThumbClick(ActorDynamicVo actorDynamicVo, final int nPosition, final int nSize)
                    {
                        int nIndex = nPosition;
                        int nCount = nSize;
                        Intent intentText = new Intent(getActivity(), ImageBrowseActivity.class);
                        intentText.putExtra("pos", nIndex);
                        intentText.putExtra("count", nCount);
                        intentText.putExtra("urls", StringUtil.listToArray(actorDynamicVo.getDynamicUrl()));
                        startActivity(intentText);

                    }

                    MediaPlayer mediaPlayer = new MediaPlayer();

                    @Override
                    public void onVoiceClick(ActorDynamicVo actorDynamicVo)
                    {
                        if (UIUtil.isListNotEmpty(actorDynamicVo.getDynamicUrl()))
                        {
                            try
                            {
                                String voiceUrl = actorDynamicVo.getDynamicUrl().get(0);
                                if (mediaPlayer.isPlaying())
                                {
                                    mediaPlayer.stop();
                                    //mediaPlayer.release();
                                }
                                else
                                {
                                    mediaPlayer.reset();
                                    mediaPlayer.setDataSource(voiceUrl);
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
                                        }
                                    });
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onVideoClick(ActorDynamicVo actorDynamicVo)
                    {
                        if (UIUtil.isListNotEmpty(actorDynamicVo.getDynamicUrl()))
                        {
                            String videoUrl = actorDynamicVo.getDynamicUrl().get(0);
                            Intent intentText = new Intent(getActivity(), WatchVideoActivity.class);
                            intentText.putExtra("url", videoUrl);
                            startActivity(intentText);
                        }
                    }
                });
            }
            else
            {
                m_adapter.updateData(m_actorDynamicVos);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_FIND_LIST:
            FetchFindList fetchFindList = (FetchFindList) msg.obj;
            if (FetchFindList.isSucceed(fetchFindList))
            {
                m_actorDynamicVos = fetchFindList.rspActorDynamicVos;
                updateData();
            }
            else
            {
                m_actorDynamicVos.clear();
                updateData();
            }
            break;
        }
    }
}
