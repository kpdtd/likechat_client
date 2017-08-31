package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.CancelAttentionEvent;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.AddAttention;
import com.audio.miliao.http.cmd.CancelAttention;
import com.audio.miliao.http.cmd.FetchActorPage;
import com.audio.miliao.util.DebugUtil;
import com.audio.miliao.util.ImageLoaderUtil;
import com.audio.miliao.util.MediaPlayerUtil;
import com.audio.miliao.util.StringUtil;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.miliao.util.UIUtil;
import com.netease.nim.uikit.miliao.util.ViewsUtil;
import com.netease.nim.uikit.miliao.vo.ActorPageVo;
import com.netease.nim.uikit.miliao.vo.ActorVo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.uikit.loader.avchat.AVChatActivity;
import com.uikit.loader.entity.LoaderAppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;


/**
 * 用户信息
 */
public class UserInfoActivity extends BaseActivity
{
    private int m_actorVoId;
    private ActorVo m_actorVo;
    private ActorPageVo m_actorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        try
        {
            if (getIntent().hasExtra("user"))
            {
                m_actorVo = (ActorVo) getIntent().getSerializableExtra("user");
                m_actorVoId = m_actorVo.getId();
            }
            else if (getIntent().hasExtra("sessionId"))
            {
                String sessionId = getIntent().getStringExtra("sessionId");
                m_actorVoId = Integer.valueOf(sessionId);
            }
            FetchActorPage fetchActor = new FetchActorPage(handler(), m_actorVoId, null);
            fetchActor.send();

            initUI();
            //updateData();
            //updatePhoto();
            //updateRecommand();
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
                        // 最新动态
                        case R.id.txt_latest_news:
                            Intent intentZone = new Intent(UserInfoActivity.this, UserZoneActivity.class);
                            intentZone.putExtra("actor_page", m_actorPage);
                            startActivity(intentZone);
                            break;
                        // 语音
                        case R.id.lay_voice:
                            if (MediaPlayerUtil.isPlaying())
                            {
                                MediaPlayerUtil.stopVoice();
                            }
                            else
                            {
                                MediaPlayerUtil.playVoice(m_actorPage.getVideoUrl(), null);
                            }
                            break;
                        // 嗨聊
                        case R.id.lay_voice_chat:
//                            Intent intentVoice = new Intent(UserInfoActivity.this, ChatVoiceCallOutActivity.class);
//                            intentVoice.putExtra("user", m_actorPage);
//                            startActivity(intentVoice);
                            if (m_actorVo != null)
                            {
                                //NimUIKit.startP2PSession(UserInfoActivity.this, m_actorVo.getToken());
                                AVChatActivity.launch(UserInfoActivity.this, m_actorVo.getToken(), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
                            }
                            else
                            {
                                //NimUIKit.startP2PSession(UserInfoActivity.this, String.valueOf(m_actorVoId));
                                AVChatActivity.launch(UserInfoActivity.this, String.valueOf(m_actorVoId), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
                            }
                            break;
                        // 文字聊天
                        case R.id.lay_text_chat:
//                            Intent intentText = new Intent(UserInfoActivity.this, ChatTextActivity.class);
//                            intentText.putExtra("user", m_actorPage);
//                            startActivity(intentText);
                            if (m_actorVo != null)
                            {
                                NimUIKit.startP2PSession(UserInfoActivity.this, m_actorVo.getToken().toLowerCase());
                            }
                            else
                            {
                                NimUIKit.startP2PSession(UserInfoActivity.this, String.valueOf(m_actorVoId));
                            }
                            break;
                        // 关注
                        case R.id.lay_follow:
                            onFollowClick();
                            break;
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

            findViewById(R.id.txt_latest_news).setOnClickListener(clickListener);
            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.lay_voice).setOnClickListener(clickListener);
            findViewById(R.id.lay_voice_chat).setOnClickListener(clickListener);
            findViewById(R.id.lay_text_chat).setOnClickListener(clickListener);
            findViewById(R.id.lay_follow).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 关注/取消关注 按钮点击
     */
    private void onFollowClick()
    {
        if (AppData.isLogin())
        {
            String follow = getString(R.string.txt_user_info_follow);
            String cancelFollow = getString(R.string.txt_user_info_cancel_follow);
            TextView textFollow = (TextView) findViewById(R.id.txt_user_info_follow);
            String text = textFollow.getText().toString();

            int userId = LoaderAppData.getCurUserId();
            int actorId = m_actorVoId;

            if (text.equals(follow))
            {
                AddAttention addAttention = new AddAttention(handler(), userId, actorId, null);
                addAttention.send();
                ;
            }
            else
            {
                CancelAttention cancelAttention = new CancelAttention(handler(), userId, actorId, null);
                cancelAttention.send();
            }
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void updatePhoto()
    {
        try
        {
            if (m_actorPage == null || UIUtil.isListEmpty(m_actorPage.getPicList()))
            {
                return;
            }

            final List<String> pootoUrlList = m_actorPage.getPicList();
            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        // 弹出大图浏览界面
                        if (null != v.getTag())
                        {
                            int nIndex = (int) v.getTag();
                            int nCount = 8;
                            Intent intentText = new Intent(UserInfoActivity.this, ImageBrowseActivity.class);
                            intentText.putExtra("pos", nIndex);
                            intentText.putExtra("count", nCount);
                            intentText.putExtra("urls", StringUtil.listToArray(pootoUrlList));
                            startActivity(intentText);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            int[] viewIds = new int[]{R.id.img_pic1, R.id.img_pic2, R.id.img_pic3,
                                      R.id.img_pic4, R.id.img_pic5, R.id.img_pic6,
                                      R.id.img_pic7, R.id.img_pic8};

            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < pootoUrlList.size(); i++)
            {
                View view = findViewById(viewIds[i]);
                view.setVisibility(View.VISIBLE);
                viewList.add(view);
            }

            ImageView imgPhoto;
            for (int i = 0; i < pootoUrlList.size(); i++)
            {
                imgPhoto = (ImageView) viewList.get(i);
                imgPhoto.setTag(i);
                imgPhoto.setOnClickListener(clickListener);
                ImageLoaderUtil.displayListAvatarImage(imgPhoto, pootoUrlList.get(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 第一期暂时取消推荐
     */
    private void updateRecommand()
    {
        try
        {
            if (m_actorPage == null)
            {
                return;
            }

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        // 主播界面
                        if (null != v.getTag())
                        {
                            // 关闭当前页
                            finish();

                            ActorPageVo actor = (ActorPageVo) v.getTag();
                            Intent intentUserInfo = new Intent(UserInfoActivity.this, UserInfoActivity.class);
                            intentUserInfo.putExtra("user", actor);
                            startActivity(intentUserInfo);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            List<ActorPageVo> actorList = DebugUtil.getUserList();
            List<ActorVo> actorPageVoList = DebugUtil.actorPageVos2Actors(actorList);

            // 去掉当前用户
            for (int i = 0; i < actorPageVoList.size(); i++)
            {
                if (actorPageVoList.get(i).getNickname().equals(m_actorPage.getNickname()))
                {
                    actorPageVoList.remove(i);
                    break;
                }
            }

            List<View> viewList = new ArrayList<>();
            viewList.add(findViewById(R.id.lay_anchor1));
            viewList.add(findViewById(R.id.lay_anchor2));
            viewList.add(findViewById(R.id.lay_anchor3));
            viewList.add(findViewById(R.id.lay_anchor4));
            viewList.add(findViewById(R.id.lay_anchor5));
            viewList.add(findViewById(R.id.lay_anchor6));
            viewList.add(findViewById(R.id.lay_anchor7));
            viewList.add(findViewById(R.id.lay_anchor8));

            // 这是生成的随机数
            Random rand = new Random(System.currentTimeMillis());
            java.util.HashSet<Integer> setExist = new java.util.HashSet<>();
            LinearLayout layAnchor = null;
            int nSize = actorList.size() < 8 ? actorList.size() : 8;
            ActorVo actorVo = null;

            // 椭机数产生主播
            for (int i = 0; i < 8; i++)
            {
                actorVo = null;
                layAnchor = (LinearLayout) viewList.get(i);
                if (i < nSize)
                {
                    int nIndex = -1;
                    while (nIndex == -1 || setExist.contains(nIndex))
                    {
                        nIndex = rand.nextInt(actorList.size());
                    }

                    setExist.add(nIndex);
                    actorVo = actorPageVoList.get(nIndex);
                    layAnchor.setTag(actorVo);
                    layAnchor.setOnClickListener(clickListener);
                }
                else
                {
                    layAnchor.setTag(null);
                    layAnchor.setOnClickListener(null);
                }

                int chSize = layAnchor.getChildCount();
                for (int j = 0; j < chSize; j++)
                {
                    View vChile = layAnchor.getChildAt(j);
                    if (vChile instanceof ImageView)
                    {
                        if (null == actorVo)
                        {
                            ((ImageView) vChile).setImageResource(0);
                        }
                        else
                        {
                            ImageLoaderUtil.displayListAvatarImageFromAsset((ImageView) vChile, actorVo.getIcon());
                        }
                    }
                    else if (vChile instanceof TextView)
                    {
                        ((TextView) vChile).setText(null == actorVo ? "" : actorVo.getNickname());
                    }
                }
            }
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
            if (m_actorPage == null)
            {
                return;
            }

            ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            TextView txtName = (TextView) findViewById(R.id.txt_name);
            TextView txtAge = (TextView) findViewById(R.id.txt_age);
            TextView txtId = (TextView) findViewById(R.id.txt_id);
            TextView txtCity = (TextView) findViewById(R.id.txt_city);
            TextView txtFansFollow = (TextView) findViewById(R.id.txt_fans_follow);
            TextView txtIntro = (TextView) findViewById(R.id.txt_intro);
            TextView txtCallRate = (TextView) findViewById(R.id.txt_call_rate);
            TextView txtTalkTime = (TextView) findViewById(R.id.txt_talk_time);
            TextView txtVoiceSec = (TextView) findViewById(R.id.txt_voice_intro);

            String icon = m_actorPage.getIcon();
            ImageLoaderUtil.displayListAvatarImage(imgAvatar, icon);
            //imgAvatar.setImageResource(m_user.avatar_res);
            txtName.setText(m_actorPage.getNickname());
            txtAge.setText(String.valueOf(m_actorPage.getAge()));
            String strId = getString(R.string.txt_user_info_like_chat_id);
            txtId.setText(strId + m_actorVoId);
            txtCity.setText(m_actorPage.getCity());
            String strFansFollow = getString(R.string.txt_user_info_fans_count);
            strFansFollow += m_actorPage.getFans() + "  ";
            strFansFollow += getString(R.string.txt_user_info_follow_count);
            strFansFollow += m_actorPage.getAttention();
            txtFansFollow.setText(strFansFollow);
            txtIntro.setText(m_actorPage.getIntroduction());
            txtCallRate.setText(m_actorPage.getPrice());
            txtTalkTime.setText(m_actorPage.getCallTime());
            if (m_actorPage.getVoiceSec() != null && m_actorPage.getVoiceSec() > 0)
            {
                findViewById(R.id.lay_voice).setVisibility(View.VISIBLE);
                txtVoiceSec.setText(formatVoiceSec(m_actorPage.getVoiceSec()));
            }
            else
            {
                findViewById(R.id.lay_voice).setVisibility(View.INVISIBLE);
            }

            updateFollowButtonState(m_actorPage.getIsAttention());

            ViewsUtil.setActorGenderDrawable(txtAge, m_actorPage.getSex(), true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String formatVoiceSec(int voiceSec)
    {
        if (voiceSec > 0 && voiceSec < 60)
        {
            return voiceSec + "″";
        }
        else if (voiceSec > 60 && voiceSec < 3600)
        {
            return String.valueOf(voiceSec / 60) + "'" + String.valueOf(voiceSec % 60) + "″";
        }
        else if (voiceSec > 3600 && voiceSec < 24 * 3600)
        {
            return String.valueOf(voiceSec / 3600) + "h";
        }

        return "60'+";
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_ACTOR_PAGE:
            FetchActorPage fetchActorPage = (FetchActorPage) msg.obj;
            if (FetchActorPage.isSucceed(fetchActorPage))
            {
                m_actorPage = fetchActorPage.rspActorPageVo;
                m_actorPage.setId(fetchActorPage.reqActorId);
                updateData();
                updatePhoto();
            }
            break;
        case HttpUtil.RequestCode.ADD_ATTENTION:
            AddAttention addAttention = (AddAttention) msg.obj;
            if (addAttention.isSucceed(addAttention))
            {
                updateFollowButtonState(true);
            }
            break;
        case HttpUtil.RequestCode.CANCEL_ATTENTION:
            CancelAttention cancelAttention = (CancelAttention) msg.obj;
            if (CancelAttention.isSucceed(cancelAttention))
            {
                updateFollowButtonState(false);
                EventBus.getDefault().post(new CancelAttentionEvent(m_actorVo));
            }
            break;
        }
    }

    /**
     * 更新关注/取消关注 按钮状态
     *
     * @param follow 当前是关注还是未关注状态
     */
    private void updateFollowButtonState(boolean follow)
    {
        TextView textFollow = (TextView) findViewById(R.id.txt_user_info_follow);

        if (follow)
        {
            textFollow.setText(R.string.txt_user_info_cancel_follow);
        }
        else
        {
            textFollow.setText(R.string.txt_user_info_follow);
        }
    }
}
