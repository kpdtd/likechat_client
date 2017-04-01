package com.likechat.likechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.likechat.likechat.R;
import com.likechat.likechat.activity.CustomerServiceActivity;
import com.likechat.likechat.activity.UserInfoActivity;
import com.likechat.likechat.adapter.AnchorAdapter;
import com.likechat.likechat.entity.Anchor;

import java.util.ArrayList;
import java.util.List;

public class TabMainFragment extends BaseFragment
{
    /** 界面中的root view */
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
                        Anchor anchor = (Anchor) m_adapter.getItem(position);
                        intentUserInfo.putExtra("anchor", anchor);
                        startActivity(intentUserInfo);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
            int[] avatars = new int[]{
                    R.mipmap.avatar1,
                    R.mipmap.avatar2,
                    R.mipmap.avatar3
                };
            List<Anchor> anchorList = new ArrayList<>();
            for (int i = 0; i < 20; i++)
            {
                Anchor anchor = new Anchor();
                anchor.name = "我是直播主播" + (i + 1);
                anchor.age = 20;
                anchor.gender = "female";
                anchor.intro = "介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍介绍";
                anchor.avatar_res = avatars[i % 3];
                anchorList.add(anchor);
            }

            if (m_adapter == null)
            {
                m_adapter = new AnchorAdapter(getActivity(), anchorList);
                m_gridView.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(anchorList);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
