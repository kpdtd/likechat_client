package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.adapter.ActorDynamicAdapter;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.util.StringUtil;
import com.netease.nim.uikit.util.UIUtil;
import com.audio.miliao.vo.ActorDynamicVo;
import com.audio.miliao.vo.ActorVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的动态
 */
public class UserZoneActivity extends BaseActivity
{
    private ActorVo m_actor;
    private ListView m_list;
    private ActorDynamicAdapter m_adapter;
    private List<ActorDynamicVo> m_actorDynamicVos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_zone);
        try
        {
            m_actor = (ActorVo) getIntent().getSerializableExtra("user");

            initUI();
            updateData();
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
            if (m_actor == null)
            {
                return;
            }

            //List<Zone> lstZone = DebugUtil.getZonesByAnchor(m_actor);
            if (m_adapter == null)
            {
                m_adapter = new ActorDynamicAdapter(UserZoneActivity.this, m_actorDynamicVos);
                m_list.setAdapter(m_adapter);

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
                    public void onVoiceClick(ActorDynamicVo actorDynamicVo)
                    {
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
                });
            }
            else
            {
                m_adapter.updateData(m_actorDynamicVos);
                m_adapter.notifyDataSetChanged();
            }

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);
            String strTitle = getString(R.string.title_user_zone_at);
            if (AppData.isCurUser(m_actor))
            {
                String strMe = getString(R.string.txt_zone_me);
                txtTitle.setText(strMe + strTitle);
                m_adapter.setShowDelete(true);
            }
            else
            {
                txtTitle.setText(m_actor.getNickname() + strTitle);
                m_adapter.setShowDelete(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
