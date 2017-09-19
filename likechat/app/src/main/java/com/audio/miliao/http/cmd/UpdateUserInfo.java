package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.app.library.vo.ActorVo;

import java.util.List;


/**
 * 修改个人信息
 */
public class UpdateUserInfo extends BaseReqRsp
{
    public ActorVo reqActorVo;

    /**
     * 软件更新
     *
     * @param handler
     * @param tag
     */
    public UpdateUserInfo(Handler handler, ActorVo actorVo, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.UPDATE_USER_INFO, false, tag);
        reqActorVo = actorVo;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "mine/updateMineInfo";

        return url;
    }

    @Override
    public String getReqBody()
    {
        return reqActorVo.toJsonString();
    }

    @Override
    public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
    {
        switch (httpStatusCode)
        {
        case 429:
            // 系统拒绝服务，可能是单个手机号发送次数超限。需要稍候再发送。
            rspResultCode = HttpUtil.Result.ERROR_DENIAL_OF_SERVICE;
            break;
        case 200:
            rspResultCode = HttpUtil.Result.OK;
            try
            {
            }
            catch (Exception e)
            {
                e.printStackTrace();
                rspResultCode = HttpUtil.Result.ERROR_UNKNOWN;
            }
            break;
        default:
            rspResultCode = HttpUtil.Result.ERROR_UNKNOWN;
            break;
        }
    }

    @Override
    public void onFinish()
    {
        if (rspResultCode == HttpUtil.Result.OK)
        {
        }
    }
}
