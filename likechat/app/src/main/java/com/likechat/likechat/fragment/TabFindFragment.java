package com.likechat.likechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.likechat.likechat.R;
import com.likechat.likechat.activity.ChatTextActivity;
import com.likechat.likechat.adapter.ZoneAdapter;
import com.likechat.likechat.algorithm.SortByDate;
import com.likechat.likechat.algorithm.SortByFollow;
import com.likechat.likechat.algorithm.SortByWatch;
import com.likechat.likechat.entity.ChatMessage;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.entity.Zone;
import com.likechat.likechat.util.DebugUtil;

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
                        Intent intentChat = new Intent(getActivity(), ChatTextActivity.class);
                        User user = (m_adapter.getItem(position) == null ? null : ((ChatMessage) m_adapter.getItem(position)).from);
                        intentChat.putExtra("user", user);
                        startActivity(intentChat);
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
            List<Zone> zoneList = DebugUtil.getZoneList();
            if (m_adapter == null)
            {
                m_adapter = new ZoneAdapter(getActivity(), zoneList);
                m_list.setAdapter(m_adapter);
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
