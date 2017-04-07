package com.likechat.likechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.likechat.likechat.R;
import com.likechat.likechat.activity.CustomerServiceActivity;
import com.likechat.likechat.activity.UserInfoActivity;
import com.likechat.likechat.adapter.AnchorAdapter;
import com.likechat.likechat.entity.User;
import com.likechat.likechat.util.DebugUtil;

import java.util.List;

public class TabMainFragment extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private GridView m_gridView;
    private AnchorAdapter m_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_main, container, false);
            initUI(m_root);
            updateData();
        }

        return m_root;
    }

    private void initUI(final View root)
    {
        try
        {
            m_gridView = (GridView) root.findViewById(R.id.grid);

            m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        Intent intentUserInfo = new Intent(getActivity(), UserInfoActivity.class);
                        User user = (User) m_adapter.getItem(position);
                        intentUserInfo.putExtra("user", user);
                        startActivity(intentUserInfo);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            m_gridView.setOnScrollListener(new AbsListView.OnScrollListener()
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
                        case R.id.img_customer_service:
                            Intent intentCustomerService = new Intent(getActivity(), CustomerServiceActivity.class);
                            startActivity(intentCustomerService);
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            root.findViewById(R.id.img_customer_service).setOnClickListener(clickListener);
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
            List<User> userList = DebugUtil.getUserList();

            if (m_adapter == null)
            {
                m_adapter = new AnchorAdapter(getActivity(), userList);
                m_gridView.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(userList);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
