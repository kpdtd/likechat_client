package com.audio.miliao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.audio.miliao.R;
import com.audio.miliao.activity.UserInfoActivity;
import com.audio.miliao.event.CancelAttentionEvent;
import com.audio.miliao.vo.ActorVo;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 底部显示点击加载更多的界面基类
 */
public abstract class BaseFootableFragment<E> extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private ListView m_list;
    private BaseUpdateAdapter<E> m_adapter;
    private View m_footer;
    private String mStamp = "0";
    private boolean m_bHasNextPage;

    public static abstract class BaseUpdateAdapter<E> extends BaseAdapter
    {
        public abstract void updateData(List<E> list);
    }

    public void setHasNextPage(boolean hasNextPage)
    {
        m_bHasNextPage = hasNextPage;
    }

    public void setStamp(String stamp)
    {
        mStamp = stamp;
    }

    public abstract int getLayoutId();

    /**
     * 获取数据
     * @param stamp
     */
    public abstract void fetchData(String stamp);

    /**
     * 返回数据
     * @param
     * @return
     */
    public abstract List<E> datas();

    /**
     * 返回adapter
     * @return
     */
    public abstract BaseUpdateAdapter getAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(getLayoutId(), container, false);
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
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        fetchData(mStamp);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 取消挂住某人
     *
     * @param event 取消挂住某人
     */
    public void onEventMainThread(CancelAttentionEvent event)
    {
        List<ActorVo> actorVos = (List<ActorVo>) datas();
        for (ActorVo actorVo : actorVos)
        {
            if (actorVo.getId().equals(event.getActorVo().getId()))
            {
                actorVos.remove(actorVo);
                break;
            }
        }
        m_adapter.updateData((List<E>) actorVos);
        m_adapter.notifyDataSetChanged();
    }

    private void initUI(final View root)
    {
        m_list = (ListView) root.findViewById(R.id.list);

        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    ActorVo actor = (ActorVo) m_adapter.getItem(position);
                    Intent intentUserInfo = new Intent(getActivity(), UserInfoActivity.class);
                    intentUserInfo.putExtra("user", actor);
                    startActivity(intentUserInfo);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateData()
    {
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

                    fetchData(mStamp);
                }
            });

            m_list.addFooterView(m_footer);
            m_adapter = getAdapter();
            m_list.setAdapter(m_adapter);
        }
        else
        {
            m_adapter.updateData(datas());
            m_adapter.notifyDataSetChanged();
        }

        if (m_bHasNextPage)
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
