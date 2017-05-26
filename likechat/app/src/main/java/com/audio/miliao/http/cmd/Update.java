package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;

import org.json.JSONObject;

import java.util.List;


/**
 * 软件更新
 */
public class Update extends BaseReqRsp
{
    String reqVersion;
    /**
     * 软件更新
     *
     * @param handler
     * @param tag
     */
    public Update(Handler handler, String version, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.UPDATE, false, tag);
        reqVersion = version;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "app/appUpdate";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("version", reqVersion);
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
        theApp.showToast("Login;" + httpStatusCode + ":" + httpBody);
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
