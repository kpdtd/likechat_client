package com.audio.miliao.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.activity.AccountBalanceActivity;
import com.audio.miliao.activity.EditUserInfoActivity;
import com.audio.miliao.activity.LoginActivity;
import com.audio.miliao.activity.SettingsActivity;
import com.audio.miliao.activity.UserFriendActivity;
import com.audio.miliao.activity.UserZoneActivity;
import com.audio.miliao.activity.VipActivity;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchMineInfo;
import com.audio.miliao.theApp;
import com.audio.miliao.util.ImageLoaderUtil;
import com.netease.nim.uikit.miliao.vo.ActorVo;
import com.netease.nim.uikit.miliao.util.ViewsUtil;

/**
 * 主界面中的我的界面
 */
public class TabMeFragment extends BaseFragment
{
    private static final int REQ_EDIT_USER_INFO = 1;
    /**
     * 界面中的root view
     */
    private View m_root;
    private ActorVo m_actorVo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (m_root == null)
        {
            m_root = inflater.inflate(R.layout.fragment_tab_me, container, false);
            initUI(m_root);
            FetchMineInfo fetchMineInfo = new FetchMineInfo(handler(), null);
            fetchMineInfo.send();
            //updateData();
        }

        return m_root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQ_EDIT_USER_INFO)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                FetchMineInfo fetchMineInfo = new FetchMineInfo(handler(), null);
                fetchMineInfo.send();
            }
        }
    }

    private void initUI(final View root)
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
                        case R.id.img_avatar:
                            if (AppData.isLogin())
                            {
                                Intent intentEditUserInfo = new Intent(getActivity(), EditUserInfoActivity.class);
                                startActivityForResult(intentEditUserInfo, REQ_EDIT_USER_INFO);
                            }
                            else
                            {
                                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intentLogin);
                            }
                            break;
                        case R.id.lay_avatar:
                            if (AppData.isLogin())
                            {
                                Intent intentEditUserInfo = new Intent(getActivity(), EditUserInfoActivity.class);
                                startActivityForResult(intentEditUserInfo, REQ_EDIT_USER_INFO);
                            }
                            else
                            {
                                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intentLogin);
                            }
                            break;
                        case R.id.txt_me_account_balance:
                            Intent intentAccountBalance = new Intent(getActivity(), AccountBalanceActivity.class);
                            startActivity(intentAccountBalance);
                            break;
                        case R.id.txt_me_friend:
                            Intent intentFriend = new Intent(getActivity(), UserFriendActivity.class);
                            startActivity(intentFriend);
                            break;
                        case R.id.txt_me_zone:
                            Intent intentZone = new Intent(getActivity(), UserZoneActivity.class);
                            intentZone.putExtra("user", AppData.getCurUser());
                            startActivity(intentZone);
                            break;
                        case R.id.txt_me_vip:
                            Intent intentVip = new Intent(getActivity(), VipActivity.class);
                            startActivity(intentVip);
                            break;
                        case R.id.txt_me_settings:
                            Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                            startActivity(intentSettings);
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            root.findViewById(R.id.img_avatar).setOnClickListener(clickListener);
            root.findViewById(R.id.lay_avatar).setOnClickListener(clickListener);
            root.findViewById(R.id.txt_me_account_balance).setOnClickListener(clickListener);
            root.findViewById(R.id.txt_me_friend).setOnClickListener(clickListener);
            root.findViewById(R.id.txt_me_zone).setOnClickListener(clickListener);
            root.findViewById(R.id.txt_me_vip).setOnClickListener(clickListener);
            root.findViewById(R.id.txt_me_settings).setOnClickListener(clickListener);
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
            View layInfo = m_root.findViewById(R.id.lay_user_info);
            View txtLogin = m_root.findViewById(R.id.txt_me_login);

            if (AppData.isLogin())
            {
                layInfo.setVisibility(View.VISIBLE);
                txtLogin.setVisibility(View.GONE);

                ImageView imgAvatar = (ImageView) m_root.findViewById(R.id.img_avatar);
                TextView txtName = (TextView) m_root.findViewById(R.id.txt_name);
                TextView txtAge = (TextView) m_root.findViewById(R.id.txt_age);
                TextView txtId = (TextView) m_root.findViewById(R.id.txt_id);
                TextView txtSigh = (TextView) m_root.findViewById(R.id.txt_sign);

                //ActorVo actor = DebugUtil.actorPageVo2ActorVo(AppData.getCurUser());
                if (m_actorVo != null)
                {
                    ImageLoaderUtil.displayListAvatarImage(imgAvatar, m_actorVo.getIcon());
                    txtName.setText(m_actorVo.getNickname());
                    txtAge.setText(m_actorVo.getAge());
                    ViewsUtil.setActorGenderDrawable(txtAge, m_actorVo.getSex(), true);
                    String strId = getString(R.string.txt_user_info_like_chat_id) + m_actorVo.getId();
                    txtId.setText(strId);
                    txtSigh.setText(m_actorVo.getSignature());
                }
            }
            else
            {
                layInfo.setVisibility(View.GONE);
                txtLogin.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_MINE_INFO:
            FetchMineInfo fetchMineInfo = (FetchMineInfo) msg.obj;
            if (FetchMineInfo.isSucceed(fetchMineInfo))
            {
                m_actorVo = fetchMineInfo.rspActorVo;
                updateData();
            }
            else
            {
                theApp.showToast("获取个人信息失败！！！");
            }
            break;
        }
    }
}
