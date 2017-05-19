package com.audio.miliao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.audio.miliao.R;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.util.List;

/**
 * 个人对个人聊天界面
 */
public class P2PChatActivity extends BaseActivity
{
    private TextView mTxtTitle;
    private String sessionId;
    private UserInfoObservable.UserInfoObserver uinfoObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_p2p);
        sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);

        initUI();
        updateData();
    }

    private void initUI()
    {
        mTxtTitle = (TextView) findViewById(R.id.txt_title);

        addFragment();

        View.OnClickListener clickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                case R.id.img_back:
                    break;
                }
            }
        };

        findViewById(R.id.img_back).setOnClickListener(clickListener);
    }

    private void addFragment()
    {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.lay_fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.lay_fragment_container, fragment);
        try
        {
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateData()
    {
        requestUserInfo();
        registerUserInfoObserver();
    }

    private void requestUserInfo()
    {
        String strTitle = UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P);
        mTxtTitle.setText(strTitle);
    }

    private void registerUserInfoObserver()
    {
        if (uinfoObserver == null)
        {
            uinfoObserver = new UserInfoObservable.UserInfoObserver()
            {
                @Override
                public void onUserInfoChanged(List<String> accounts)
                {
                    if (accounts.contains(sessionId))
                    {
                        requestUserInfo();
                    }
                }
            };
        }

        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }
}
