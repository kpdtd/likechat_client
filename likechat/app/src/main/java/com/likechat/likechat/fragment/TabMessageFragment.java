package com.likechat.likechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likechat.likechat.R;

public class TabMessageFragment extends BaseFragment
{
    /** 界面中的root view */
    private View m_root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_message, container, false);
        }

        return m_root;
    }
}
