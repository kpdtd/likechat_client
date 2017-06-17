package com.audio.miliao.adapter;


import android.support.v7.widget.RecyclerView;

import com.audio.miliao.R;
import com.netease.nim.uikit.recent.RecentContactsCallback;
import com.netease.nim.uikit.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.recent.holder.CommonRecentViewHolder;
import com.netease.nim.uikit.recent.holder.TeamRecentViewHolder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * 最近联系人消息
 */
public class LikechatRecentMessageAdapter extends RecentContactAdapter
{
    interface ViewType
    {
        int VIEW_TYPE_COMMON = 1;
        int VIEW_TYPE_TEAM = 2;
    }

    private RecentContactsCallback callback;

    public LikechatRecentMessageAdapter(RecyclerView recyclerView, List<RecentContact> data)
    {
        super(recyclerView, data);
        addItemType(ViewType.VIEW_TYPE_COMMON, R.layout.likechat_recent_contact_list_item, CommonRecentViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_TEAM, R.layout.nim_recent_contact_list_item, TeamRecentViewHolder.class);
    }

    @Override
    protected int getViewType(RecentContact item)
    {
        return item.getSessionType() == SessionTypeEnum.Team ? ViewType.VIEW_TYPE_TEAM : ViewType.VIEW_TYPE_COMMON;
    }

    @Override
    protected String getItemKey(RecentContact item)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getSessionType().getValue()).append("_").append(item.getContactId());

        return sb.toString();
    }

    public RecentContactsCallback getCallback()
    {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback)
    {
        this.callback = callback;
    }
}
