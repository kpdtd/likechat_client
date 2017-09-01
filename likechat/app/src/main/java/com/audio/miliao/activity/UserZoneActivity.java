package com.audio.miliao.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.adapter.ActorDynamicAdapter;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.AddDynamicPageView;
import com.audio.miliao.http.cmd.FetchActorDynamicList;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.MediaPlayerUtil;
import com.audio.miliao.util.StringUtil;
import com.netease.nim.uikit.miliao.util.UIUtil;
import com.netease.nim.uikit.miliao.vo.ActorDynamicVo;
import com.netease.nim.uikit.miliao.vo.ActorPageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的动态
 */
public class UserZoneActivity extends BaseActivity
{
    private ActorPageVo m_actorPage;
    private ListView m_list;
    private View m_footer;
    private ActorDynamicAdapter m_adapter;
    private List<ActorDynamicVo> m_actorDynamicVos = new ArrayList<>();

    private boolean mHasNextPage = false;
    private String mStamp = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_zone);
        try
        {
            m_actorPage = (ActorPageVo) getIntent().getSerializableExtra("actor_page");

            initUI();
            //updateData();
            fetchData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
                        // 关注
                        case R.id.img_back:
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

            m_list = (ListView) findViewById(R.id.list);

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
            if (m_actorPage == null)
            {
                return;
            }

            //List<Zone> lstZone = DebugUtil.getZonesByAnchor(m_actorPage);
            if (m_adapter == null)
            {
                m_footer = View.inflate(this, R.layout.footer_load_more, null);
                m_footer.findViewById(R.id.lay_footer).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        v.findViewById(R.id.btn_click_load_more).setVisibility(View.GONE);
                        v.findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        fetchData();
                    }
                });

                m_list.addFooterView(m_footer);
                m_adapter = new ActorDynamicAdapter(UserZoneActivity.this, m_actorDynamicVos);
                m_list.setAdapter(m_adapter);
                m_list.setOnScrollListener(ImageLoaderUtil.getPauseListener());

                m_adapter.setOnClickListener(new ActorDynamicAdapter.OnClickListener()
                {
                    @Override
                    public void onThumbClick(ActorDynamicVo actorDynamicVo, final int nPosition, final int nSize)
                    {
                        int nIndex = nPosition;
                        int nCount = nSize;
                        Intent intentText = new Intent(UserZoneActivity.this, ImageBrowseActivity.class);
                        intentText.putExtra("pos", nIndex);
                        intentText.putExtra("count", nCount);
                        intentText.putExtra("urls", StringUtil.listToArray(actorDynamicVo.getDynamicUrl()));
                        startActivity(intentText);
                    }

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
                            String videoUrl = actorDynamicVo.getDynamicUrl().get(0);
                            Intent intentText = new Intent(UserZoneActivity.this, WatchVideoActivity.class);
                            intentText.putExtra("url", videoUrl);
                            startActivity(intentText);
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

                    }
                });
            }
            else
            {
                m_adapter.updateData(m_actorDynamicVos);
                m_adapter.notifyDataSetChanged();
            }

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

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);
            String strTitle = getString(R.string.title_user_zone_at);
//            if (AppData.isCurUser(m_actorPage))
//            {
//                String strMe = getString(R.string.txt_zone_me);
//                txtTitle.setText(strMe + strTitle);
//                m_adapter.setShowDelete(true);
//            }
//            else
            {
                txtTitle.setText(m_actorPage.getNickname() + strTitle);
                m_adapter.setShowDelete(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void fetchData()
    {
        if (m_actorPage != null)
        {
            FetchActorDynamicList fetchActorDynamicList = new FetchActorDynamicList(handler(), m_actorPage.getId(), mStamp, null);
            fetchActorDynamicList.send();
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_ACTOR_DYNAMIC_LIST:
            FetchActorDynamicList fetchActorDynamicList = (FetchActorDynamicList) msg.obj;
            if (FetchActorDynamicList.isSucceed(fetchActorDynamicList))
            {
                mStamp = fetchActorDynamicList.rspStamp;
                mHasNextPage = fetchActorDynamicList.rspHasNextPage;
                m_actorDynamicVos.addAll(fetchActorDynamicList.rspActorDynamicVos);
                updateData();
            }
            break;
        }
    }
}
