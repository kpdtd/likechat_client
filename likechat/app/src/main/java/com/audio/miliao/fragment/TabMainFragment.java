package com.audio.miliao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.audio.miliao.R;
import com.audio.miliao.activity.CustomerServiceActivity;
import com.audio.miliao.activity.UserInfoActivity;
import com.audio.miliao.adapter.ActorAdapter;
import com.audio.miliao.event.FetchHomeContentEvent;
import com.audio.miliao.theApp;
import com.audio.miliao.util.UIUtil;
import com.audio.miliao.vo.ActorVo;
import com.audio.miliao.vo.TagVo;
import com.audio.miliao.widget.GridViewWithHeaderAndFooter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class TabMainFragment extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private GridViewWithHeaderAndFooter m_gridView;
    private ActorAdapter m_adapter;
    private List<ActorVo> m_actorVoList = new ArrayList<>();
    // 标题栏上的tag
    private RadioGroup mRadioGroupTag;
    private List<TagVo> m_tagVoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_main, container, false);
            initUI(m_root);
            updateData();
            EventBus.getDefault().register(this);
        }

        return m_root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initUI(final View root)
    {
        try
        {
            m_gridView = (GridViewWithHeaderAndFooter) root.findViewById(R.id.grid);
            mRadioGroupTag = (RadioGroup) root.findViewById(R.id.rgp_tag);

            m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        Intent intentUserInfo = new Intent(getActivity(), UserInfoActivity.class);
                        ActorVo actor = (ActorVo) m_adapter.getItem(position);
                        intentUserInfo.putExtra("user", actor);
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
            if (m_adapter == null)
            {
                View headerView = View.inflate(getActivity(), R.layout.list_header_main_banner, null);
                m_gridView.addHeaderView(headerView);

                m_adapter = new ActorAdapter(getActivity(), m_actorVoList);
                m_gridView.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(m_actorVoList);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateTitleTag()
    {
        if (UIUtil.isListNotEmpty(m_tagVoList))
        {
            mRadioGroupTag.removeAllViews();

            int width = UIUtil.dip2px(getContext(), 30);
            RadioGroup.LayoutParams params =  new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    1.0f);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    TagVo tagVo = (TagVo) v.getTag();
                    if (tagVo != null)
                    {
                        theApp.showToast(tagVo.toString());
                    }
                }
            };

            for (TagVo tagVo : m_tagVoList)
            {
                RadioButton radio = (RadioButton) View.inflate(getContext(), R.layout.layout_tag, null);

                radio.setText(tagVo.getTagName());
                radio.setTag(tagVo);
                radio.setOnClickListener(clickListener);
                mRadioGroupTag.addView(radio, params);
            }

            RadioButton radio = (RadioButton) mRadioGroupTag.getChildAt(0);
            radio.setChecked(true);
        }
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event 获取主界面内容结果
     */
    public void onEventMainThread(FetchHomeContentEvent event)
    {
        if (event.isSucceed())
        {
            m_actorVoList = event.getActorVos();
            m_tagVoList = event.getTagVos();
            updateData();
            updateTitleTag();
        }
    }
}
