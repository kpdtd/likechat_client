package com.uikit.loader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.uikit.loader.LoaderApp;
import com.uikit.loader.R;
import com.uikit.loader.entity.Account;
import com.uikit.loader.service.YXService;

public class LoginActivity extends AppCompatActivity
{
    private Account mCurAccount = LoaderApp.getCurAccount();
    private YXService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mService = new YXService(this);

        View.OnClickListener clickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int viewId = v.getId();
                if (viewId == R.id.btn_login)
                {
                    onLogin();
                }
                else if (viewId == R.id.btn_chat)
                {
                    onChat();
                }
                else if (viewId == R.id.rdo_test3)
                {
                    onLoginResult(false);
                    mCurAccount = LoaderApp.TEST3;
                }
                else if (viewId == R.id.rdo_test4)
                {
                    onLoginResult(false);
                    mCurAccount = LoaderApp.TEST4;
                }
                else if (viewId == R.id.btn_main)
                {
                    onMain();
                }
                else if (viewId == R.id.btn_contacts)
                {
                    onContacts();
                }
                else if (viewId == R.id.btn_recent_contact)
                {
                    onRecentContact();
                }
//                switch (v.getId())
//                {
//                case R.id.btn_login:
//                    onLogin();
//                    break;
//                case R.id.btn_chat:
//                    onChat();
//                    break;
//                case R.id.rdo_test3:
//                    onLoginResult(false);
//                    mCurAccount = theApp.TEST3;
//                    break;
//                case R.id.rdo_test4:
//                    onLoginResult(false);
//                    mCurAccount = theApp.TEST4;
//                    break;
//                case R.id.btn_main:
//                    onMain();
//                    break;
//                case R.id.btn_contacts:
//                    onContacts();
//                    break;
//                case R.id.btn_recent_contact:
//                    onRecentContact();
//                    break;
//                }
            }
        };

        findViewById(R.id.btn_login).setOnClickListener(clickListener);
        findViewById(R.id.btn_chat).setOnClickListener(clickListener);
        findViewById(R.id.btn_main).setOnClickListener(clickListener);
        findViewById(R.id.btn_recent_contact).setOnClickListener(clickListener);
        findViewById(R.id.btn_contacts).setOnClickListener(clickListener);
        findViewById(R.id.rdo_test3).setOnClickListener(clickListener);
        findViewById(R.id.rdo_test4).setOnClickListener(clickListener);

        findViewById(R.id.rdo_test3).performClick();
    }

    private void onLogin()
    {
        //onYunXinLogin(mCurAccount);
        //mService.login(mCurAccount);
    }

    private void onChat()
    {
        String strAccount;
        if (mCurAccount.getAccount().equals(LoaderApp.TEST3.getAccount()))
        {
            strAccount = LoaderApp.TEST4.getAccount();
        }
        else
        {
            strAccount = LoaderApp.TEST3.getAccount();
        }
        //chat(strAccount);
        mService.chat(strAccount);
    }

    private void onMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onContacts()
    {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    private void onRecentContact()
    {
        Intent intent = new Intent(this, com.uikit.loader.activity.RecentContactActivity.class);
        startActivity(intent);
    }

    /**
     * 登录结果处理UI
     * @param succeed 成功还是失败
     */
    private void onLoginResult(boolean succeed)
    {
        findViewById(R.id.btn_login).setEnabled(!succeed);
        findViewById(R.id.btn_chat).setEnabled(succeed);

        LoaderApp.setCurAccount(succeed ? mCurAccount : null);
    }
}
