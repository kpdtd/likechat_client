package com.audio.miliao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.audio.miliao.R;
import com.audio.miliao.event.FetchHomeContentEvent;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.UIUtil;
import com.audio.miliao.vo.BannerVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 主界面的Banner
 */
public class BannerFragment extends BaseFragment
{
    /**
     * 界面中的root view
     */
    private View m_root;
    private ViewPager mPager;
    private RadioGroup mRadioGroup;
    private PagerAdapter mAdapter;
    private List<View> mViewList;
    private List<View> mRadioList;
    private List<BannerVo> mBannerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_banner, container, false);
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

    private void initUI(View root)
    {
        try
        {
            mPager = (ViewPager) root.findViewById(R.id.view_pager);
            mRadioGroup = (RadioGroup) root.findViewById(R.id.rgp_indicator);

            mViewList = new ArrayList<>();
            mRadioList = new ArrayList<>();

            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
            {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                {

                }

                @Override
                public void onPageSelected(int position)
                {
                    // 一个新页被调用时执行,仍为原来的page时，该方法不被调用
                    try
                    {
                        RadioButton radioButton = (RadioButton) mRadioList.get(position);
                        radioButton.setChecked(true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state)
                {
                    /**
                     * SCROLL_STATE_IDLE: pager处于空闲状态<br/>
                     * SCROLL_STATE_DRAGGING： pager处于正在拖拽中 <br/>
                     * SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                     */
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
        resetViews();

        if (mAdapter == null)
        {
            if (UIUtil.isListNotEmpty(mBannerList))
            {
                mAdapter = new PagerAdapter()
                {
                    @Override
                    public int getCount()
                    {
                        return (mBannerList != null ? mBannerList.size() : 0);
                    }

                    @Override
                    public boolean isViewFromObject(View view, Object object)
                    {
                        return view == object;
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object)
                    {
                        //super.destroyItem(container, position, object);

                        //删除页卡
                        container.removeView(mViewList.get(position));
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position)
                    {
                        //return super.instantiateItem(container, position);
                        if (mViewList != null && mViewList.size() > 0)
                        {
                            BannerVo bannerVo = mBannerList.get(position);
                            ImageView view = (ImageView) mViewList.get(position);
                            ImageLoaderUtil.displayListAvatarImageFromAsset(view, bannerVo.getIcon());

                            //添加页卡
                            container.addView(view, 0);
                            return view;
                        }

                        return null;
                    }
                };
                mPager.setAdapter(mAdapter);
            }
        }
        else
        {
            mAdapter.notifyDataSetChanged();
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
            mBannerList = event.getBannerVos();
            updateData();
        }
    }

    private void resetViews()
    {
        if (UIUtil.isListNotEmpty(mBannerList))
        {
            int width = UIUtil.dip2px(getContext(), 8);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(width, width);
            params.rightMargin = width / 2;
            for (int i = 0; i < mBannerList.size(); i++)
            {
                ImageView imageView = (ImageView) View.inflate(getContext(), R.layout.layout_banner, null);
                mViewList.add(imageView);
                RadioButton radio = (RadioButton) View.inflate(getContext(), R.layout.layout_indicator, null);
                mRadioList.add(radio);

                mRadioGroup.addView(radio, params);
            }

            if (UIUtil.isListNotEmpty(mRadioList))
            {
                RadioButton radioButton = (RadioButton) mRadioList.get(0);
                radioButton.setChecked(true);
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    try
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    int curItem = mPager.getCurrentItem();
                                    if (curItem == mBannerList.size() - 1)
                                    {
                                        mPager.setCurrentItem(0);
                                    }
                                    else
                                    {
                                        mPager.setCurrentItem(curItem + 1);
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 3000, 3000);
        }
    }
}
