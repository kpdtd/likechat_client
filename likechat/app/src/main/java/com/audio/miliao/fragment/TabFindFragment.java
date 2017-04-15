package com.audio.miliao.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.audio.miliao.adapter.ZoneAdapter;
import com.audio.miliao.algorithm.SortByDate;
import com.audio.miliao.algorithm.SortByFollow;
import com.audio.miliao.algorithm.SortByWatch;
import com.audio.miliao.entity.Zone;
import com.audio.miliao.util.DebugUtil;

import java.util.List;

public class TabFindFragment extends BaseFragment
{
    /** 界面中的root view */
    private View m_root;
    private ListView m_list;
    private ZoneAdapter m_adapter;
    /** 最新 */
    private SortByDate m_sortByDate = new SortByDate();
    /** 热门 */
    private SortByWatch m_sortByWatch = new SortByWatch();
    /** 关注 */
    private SortByFollow m_sortByFollow = new SortByFollow();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_find, container, false);

            initUI(m_root);
            updateData();
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
                            m_adapter.updateData(m_sortByDate.sort(m_adapter.getZones()));
                            m_adapter.notifyDataSetChanged();
                            break;
                        // 热门
                        case R.id.rdo_find_hot:
                            m_adapter.updateData(m_sortByWatch.sort(m_adapter.getZones()));
                            m_adapter.notifyDataSetChanged();
                            break;
                        // 关注
                        case R.id.rdo_find_follow:
                            m_adapter.updateData(m_sortByFollow.sort(m_adapter.getZones()));
                            m_adapter.notifyDataSetChanged();
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

    private void updateData()
    {
        try
        {
            List<Zone> zoneList = DebugUtil.getZonesFind();
            if (m_adapter == null)
            {
                m_adapter = new ZoneAdapter(getActivity(), zoneList);
                m_list.setAdapter(m_adapter);

                m_adapter.setOnClickListener(new ZoneAdapter.OnClickListener()
                {
                    @Override
                    public void onThumbClick(Zone zone, final int nPosition, final int nSize)
                    {
                        int nIndex = nPosition;
                        int nCount = nSize;
                        Intent intentText = new Intent(getActivity(), ImageBrowseActivity.class);
                        intentText.putExtra("pos", nIndex);
                        intentText.putExtra("count", nCount);
                        intentText.putExtra("urls", zone.photosUrl);
                        startActivity(intentText);

                    }
                    @Override
                    public void onVoiceClick(Zone zone)
                    {
                    }
                    @Override
                    public void onVideoClick(Zone zone)
                    {
                        Intent intentText = new Intent(getActivity(), WatchVideoActivity.class);
                        intentText.putExtra("url", zone.voiceUrl);
                        startActivity(intentText);
                    }
                });
            }
            else
            {
                m_adapter.updateData(zoneList);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 热门（按观看人数排序）
     */
    private void sortByWatch()
    {
        try
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    /**
     * 按照用户关注的主播发的动态，最新的放最上面
     */
    private void sortByFollow()
    {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
