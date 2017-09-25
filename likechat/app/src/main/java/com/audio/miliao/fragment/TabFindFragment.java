package com.audio.miliao.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.util.StringUtil;
import com.app.library.util.UIUtil;
import com.app.library.vo.ActorDynamicVo;
import com.audio.miliao.R;
import com.audio.miliao.activity.BalanceActivity;
import com.audio.miliao.activity.ImageBrowseActivity;
import com.audio.miliao.activity.UserInfoActivity;
import com.audio.miliao.activity.WatchVideoActivity;
import com.audio.miliao.adapter.ActorDynamicAdapter;
import com.audio.miliao.algorithm.SortByDate;
import com.audio.miliao.algorithm.SortByFollow;
import com.audio.miliao.algorithm.SortByWatch;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.AddDynamicPageView;
import com.audio.miliao.http.cmd.ChargeDynamic;
import com.audio.miliao.http.cmd.FetchFindList;
import com.audio.miliao.util.MediaPlayerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面中的发现界面
 */
public class TabFindFragment extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private ListView m_list;
    private View m_footer;
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
    private boolean mHasNextPage = false;
    private String mStamp = "0";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_find, container, false);

            initUI(m_root);
            fetchFindList("header");
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

            m_list.setOnScrollListener(ImageLoaderUtil.getPauseListener());
//            m_list.setOnScrollListener(new AbsListView.OnScrollListener()
//            {
//                @Override
//                public void onScrollStateChanged(AbsListView view, int scrollState)
//                {
//                    try
//                    {
//                        switch (scrollState)
//                        {
//                        //停止滚动
//                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                            m_adapter.setScrolling(false);
//                            m_adapter.notifyDataSetChanged();
//                            break;
//                        //滚动做出了抛的动作
//                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                            m_adapter.setScrolling(true);
//                            break;
//                        //正在滚动
//                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                            m_adapter.setScrolling(true);
//                            break;
//                        }
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
//                {
//
//                }
//            });

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
                            mStamp = "0";
                            fetchFindList("header");
                            break;
                        // 热门
                        case R.id.rdo_find_hot:
                            //m_adapter.updateData(m_sortByWatch.sort(m_adapter.getZones()));
                            //m_adapter.notifyDataSetChanged();
                            mFetchFindListTag = FetchFindList.HOT;
                            mStamp = "0";
                            fetchFindList("header");
                            break;
                        // 关注
                        case R.id.rdo_find_follow:
                            //m_adapter.updateData(m_sortByFollow.sort(m_adapter.getZones()));
                            //m_adapter.notifyDataSetChanged();
                            mFetchFindListTag = FetchFindList.FOCUS;
                            mStamp = "0";
                            fetchFindList("header");
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

    /**
     *
     * @param strFrom 是点击哪个按钮触发的获取列表 (点击标题栏上的最新、热关注或者点击加载更多)
     */
    private void fetchFindList(String strFrom)
    {
        FetchFindList fetchFindList = new FetchFindList(handler(), mFetchFindListTag, mStamp, strFrom);
        fetchFindList.send();
    }

    private void updateData()
    {
        try
        {
            //List<Zone> zoneList = DebugUtil.getZonesFind();
            if (m_adapter == null)
            {
                m_footer = View.inflate(getActivity(), R.layout.footer_load_more, null);
                m_footer.findViewById(R.id.lay_footer).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        v.findViewById(R.id.btn_click_load_more).setVisibility(View.GONE);
                        v.findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        fetchFindList("footer");
                    }
                });

                m_list.addFooterView(m_footer);

                m_adapter = new ActorDynamicAdapter(getActivity(), m_actorDynamicVos);
                m_list.setAdapter(m_adapter);
                //m_list.setOnScrollListener(ImageLoaderUtil.getPauseListener());

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

                        AddDynamicPageView addDynamicPageView = new AddDynamicPageView(null, actorDynamicVo.getId(), null);
                        addDynamicPageView.send();
                    }

                    //MediaPlayer mediaPlayer = new MediaPlayer();

                    @Override
                    public void onVoiceClick(final ActorDynamicVo actorDynamicVo)
                    {
                        if (UIUtil.isListNotEmpty(actorDynamicVo.getDynamicUrl()))
                        {
                            try
                            {
                                String voiceUrl = actorDynamicVo.getDynamicUrl().get(0);
                                if (MediaPlayerUtil.isPlaying())
                                {
                                    MediaPlayerUtil.stopVoice();
                                    //mediaPlayer.release();
                                }
                                else
                                {
                                    MediaPlayerUtil.playVoice(voiceUrl, new MediaPlayer.OnPreparedListener()
                                    {
                                        @Override
                                        public void onPrepared(MediaPlayer mp)
                                        {
                                            AddDynamicPageView addDynamicPageView = new AddDynamicPageView(null, actorDynamicVo.getId(), null);
                                            addDynamicPageView.send();
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
                            if (actorDynamicVo.getPrice() > 0)
                            {
                                ChargeDynamic chargeDynamic = new ChargeDynamic(handler(), actorDynamicVo.getActorId(), actorDynamicVo.getPrice(), actorDynamicVo);
                                chargeDynamic.send();
                            }
                        }
                    }

                    /**
                     * 点击头像
                     *
                     * @param actorDynamicVo
                     */
                    @Override
                    public void onAvatarClick(ActorDynamicVo actorDynamicVo)
                    {
                        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                        intent.putExtra("sessionId", String.valueOf(actorDynamicVo.getActorId()));
                        startActivity(intent);
                    }
                });
            }
            else
            {
                m_adapter.updateData(m_actorDynamicVos);
                m_adapter.notifyDataSetChanged();

                if (mHasNextPage)
                {
                    m_footer.findViewById(R.id.btn_click_load_more).setVisibility(View.VISIBLE);
                    m_footer.findViewById(R.id.loading).setVisibility(View.GONE);
                }
                else
                {
                    m_footer.findViewById(R.id.btn_click_load_more).setVisibility(View.GONE);
                    m_footer.findViewById(R.id.loading).setVisibility(View.GONE);
                }
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
                if (fetchFindList.rspCallBackTag != null)
                {
                    String strCallbackTag = (String) fetchFindList.rspCallBackTag;
                    if (strCallbackTag.equals("header"))
                    {
                        mStamp = fetchFindList.rspStamp;
                        mHasNextPage = fetchFindList.rspHasNextPage;
                        m_actorDynamicVos.clear();
                        m_actorDynamicVos.addAll(fetchFindList.rspActorDynamicVos);
                        updateData();
                    }
                    else if (strCallbackTag.equalsIgnoreCase("footer"))
                    {
                        mStamp = fetchFindList.rspStamp;
                        mHasNextPage = fetchFindList.rspHasNextPage;
                        m_actorDynamicVos.addAll(fetchFindList.rspActorDynamicVos);
                        updateData();
                    }
                }
            }
            else
            {
//                m_actorDynamicVos.clear();
//                updateData();
            }
            break;
        case HttpUtil.RequestCode.CHARGE_DYNAMIC:
            ChargeDynamic chargeDynamic = (ChargeDynamic) msg.obj;
            if (ChargeDynamic.isSucceed(chargeDynamic))
            {
                ActorDynamicVo actorDynamicVo = (ActorDynamicVo) chargeDynamic.rspCallBackTag;

                String videoUrl = actorDynamicVo.getDynamicUrl().get(0);
                Intent intentText = new Intent(getActivity(), WatchVideoActivity.class);
                intentText.putExtra("url", videoUrl);
                startActivity(intentText);

                AddDynamicPageView addDynamicPageView = new AddDynamicPageView(null, actorDynamicVo.getId(), null);
                addDynamicPageView.send();
            }
            else
            {
                //SimpleBalanceActivity.show(getActivity());
                Intent intentAccountBalance = new Intent(getActivity(), BalanceActivity.class);
                startActivity(intentAccountBalance);
            }
            break;
        }
    }
}
