package com.audio.miliao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.app.library.util.Checker;
import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.ActorVo;
import com.app.library.vo.TagVo;
import com.audio.miliao.R;
import com.audio.miliao.activity.UserInfoActivity;
import com.audio.miliao.adapter.ActorAdapter;
import com.audio.miliao.event.FetchHomeContentEvent;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchActorListByTag;
import com.audio.miliao.http.cmd.FetchHomeContent;
import com.audio.miliao.widget.GridViewWithHeaderAndFooter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主界面中的第一个界面
 */
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
    private String m_curTag;
    // private View m_footer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_main, container, false);
        }

        return m_root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (m_root != null)
        {
            initUI(m_root);

            FetchHomeContent fetchHomeContent = new FetchHomeContent(null, null);
            fetchHomeContent.send();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            EventBus.getDefault().register(this);
            updateData();
            updateTitleTag();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

            m_gridView.setOnScrollListener(ImageLoaderUtil.getPauseListener());
            m_gridView.setOnScrollListener(new AbsListView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState)
                {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
                    {
                        if (view.getLastVisiblePosition() == view.getCount() - 1)
                        {
                            fetchActorList();
                        }
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
            if (m_adapter == null)
            {
                //View headerView = View.inflate(getActivity(), R.layout.list_header_main_banner, null);
//                m_footer = View.inflate(getActivity(), R.layout.footer_load_more, null);
//                m_footer.findViewById(R.id.lay_footer).setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        v.findViewById(R.id.btn_click_load_more).setVisibility(View.GONE);
//                        v.findViewById(R.id.loading).setVisibility(View.VISIBLE);
//                        fetchActorList();
//                    }
//                });
//
//                //m_gridView.addHeaderView(headerView);
//                m_gridView.addFooterView(m_footer);

                m_adapter = new ActorAdapter(getActivity(), m_actorVoList);
                m_gridView.setAdapter(m_adapter);
                //m_gridView.setOnScrollListener(ImageLoaderUtil.getPauseListener());
            }
            else
            {
                m_adapter.updateData(m_actorVoList);
                m_adapter.notifyDataSetChanged();

//                m_footer.findViewById(R.id.btn_click_load_more).setVisibility(View.VISIBLE);
//                m_footer.findViewById(R.id.loading).setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<ActorVo> getActorVoList()
    {
        return m_actorVoList;
    }

    private void updateTitleTag()
    {
        if (Checker.isNotEmpty(m_tagVoList))
        {
            mRadioGroupTag.removeAllViews();

            //int width = UIUtil.dip2px(getContext(), 30);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    1.0f);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    checkView(v);
                    m_actorVoList = new ArrayList<>();
                    TagVo tagVo = (TagVo) v.getTag();
                    if (tagVo != null)
                    {
                        //theApp.showToast(tagVo.toString());
                        FetchActorListByTag fetchActorListByTag = new FetchActorListByTag(handler(), tagVo.getIdentifying(), null);
                        fetchActorListByTag.send();
                    }
                }
            };

            for (TagVo tagVo : m_tagVoList)
            {
                View view = View.inflate(getContext(), R.layout.layout_tag, null);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.chk_tag);

                checkBox.setText(tagVo.getTagName());
                checkBox.setTag(tagVo);
                //view.setOnClickListener(clickListener);
                checkBox.setOnClickListener(clickListener);

                mRadioGroupTag.addView(view, params);
            }

            int curTagIndex = getCurTagIndex();
            View firstChild = mRadioGroupTag.getChildAt(curTagIndex);
            checkView(firstChild);
            m_curTag = m_tagVoList.get(curTagIndex).getIdentifying();
        }
    }

    private int getCurTagIndex()
    {
        if (Checker.isNotEmpty(m_tagVoList))
        {
            int index = 0;
            for (TagVo tagVo : m_tagVoList)
            {
                if (tagVo.getIdentifying().equals(m_curTag))
                {
                    return index;
                }

                index++;
            }
        }

        return 0;
    }

    private void checkView(View checkedBoxRoot)
    {
        try
        {
            int nCount;
            if (mRadioGroupTag != null && (nCount = mRadioGroupTag.getChildCount()) > 0)
            {
                for (int i = 0; i < nCount; i++)
                {
                    View child = mRadioGroupTag.getChildAt(i);
                    CheckBox checkBox = (CheckBox) child.findViewById(R.id.chk_tag);
                    checkBox.setChecked(false);
                }

                CheckBox checkedBox = (CheckBox) checkedBoxRoot.findViewById(R.id.chk_tag);
                checkedBox.setChecked(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event 获取主界面内容结果
     */
    public void onEventMainThread(FetchHomeContentEvent event)
    {
        if (event.getIsSucceed())
        {
            m_actorVoList = event.getActorVos();
            m_tagVoList = event.getTagVos();
            updateData();
            updateTitleTag();
        }
    }

    private void fetchActorList()
    {
        //看当前是哪个tag
        FetchActorListByTag fetchActorListByTag = new FetchActorListByTag(handler(), m_curTag, null);
        fetchActorListByTag.send();
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_ACTOR_LIST_BY_TAG:
            FetchActorListByTag fetchActorListByTag = (FetchActorListByTag) msg.obj;
            if (FetchActorListByTag.isSucceed(fetchActorListByTag))
            {
                m_actorVoList.addAll(fetchActorListByTag.rspActorVos);
                updateData();
            }
            else
            {
                if (m_curTag != null &&
                        !m_curTag.equals(fetchActorListByTag.reqTag))
                {
                    // 查看另外的tag，获取失败，显示空白界面
                    m_actorVoList.clear();
                    updateData();
                }
            }

            m_curTag = fetchActorListByTag.reqTag;
            break;
        }
    }
}
