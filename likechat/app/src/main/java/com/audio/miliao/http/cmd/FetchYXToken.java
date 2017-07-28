package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取云信Token<br/>
 * 云信安全模式：客户端需要AppKey和一个Token来完成认证进行实时通话。 <br/>
 * 其中Token需要第三方服务器向网易云通信服务器获取后传回客户端。<br/>
 * 调用这个接口可以从服务器获取云信token。（同时也会创建云信用户accid）
 */
public class FetchYXToken extends BaseReqRsp
{
    public String reqOpenId;

    /**
     * 获取云信Token<br/>
     * 云信安全模式：客户端需要AppKey和一个Token来完成认证进行实时通话。 <br/>
     * 其中Token需要第三方服务器向网易云通信服务器获取后传回客户端。<br/>
     * 调用这个接口可以从服务器获取云信token。（同时也会创建云信用户accid）
     *
     * @param handler
     * @param openId  用户ID
     * @param tag
     */
    public FetchYXToken(Handler handler, String openId, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_YX_TOKEN, false, tag);

        reqOpenId = openId;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "yunxin/getYXToken";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("openId", reqOpenId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
    {
        switch (httpStatusCode)
        {
        case 429:
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
