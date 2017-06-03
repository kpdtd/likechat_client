package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.vo.ActorVo;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取我的登录信息，需要登录用户才能访问服务器
 */
public class FetchMineInfo extends BaseReqRsp
{
    public ActorVo rspActorVo;

    /**
     * 获取我的登录信息，需要登录用户才能访问服务器
     *
     * @param handler
     * @param tag
     */
    public FetchMineInfo(Handler handler, Object tag)
    {
        super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.FETCH_MINE_INFO, false, tag);
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "mine/getMineInfo";

        return url;
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
                JSONObject jsonObject = new JSONObject(httpBody);
                JSONObject jsonData = jsonObject.optJSONObject("data");
                rspActorVo = ActorVo.parse(jsonData.toString(), ActorVo.class);
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
