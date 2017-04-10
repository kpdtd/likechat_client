package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.adapter.ZoneAdapter;
import com.audio.miliao.entity.User;
import com.audio.miliao.entity.Zone;
import com.audio.miliao.util.DebugUtil;

import java.util.List;

/**
 * 我的动态
 */
public class UserZoneActivity extends BaseActivity
{
    private User m_user;
    private ListView m_list;
    private ZoneAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_zone);
        try
        {
            m_user = (User) getIntent().getSerializableExtra("user");

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
            if (m_user == null)
            {
                return;
            }

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);

            txtTitle.setText(m_user.name + getString(R.string.title_user_zone_at));

            List<Zone> lstZone = DebugUtil.getZonesByAnchor(m_user);
            if (m_adapter == null)
            {
                m_adapter = new ZoneAdapter(UserZoneActivity.this, lstZone);
                m_list.setAdapter(m_adapter);

                m_adapter.setOnThumbClickListener(new ZoneAdapter.OnThumbClickListener()
                {
                    @Override
                    public void onClick(Zone zone, final int nPosition, final int nSize)
                    {
                        int nIndex = nPosition;
                        int nCount = nSize;
                        Intent intentText = new Intent(UserZoneActivity.this, ImageBrowseActivity.class);
                        intentText.putExtra("pos", nIndex);
                        intentText.putExtra("count", nCount);
                        intentText.putExtra("urls", zone.photosUrl);
                        startActivity(intentText);

                    }
                });
            }
            else
            {
                m_adapter.updateData(lstZone);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
