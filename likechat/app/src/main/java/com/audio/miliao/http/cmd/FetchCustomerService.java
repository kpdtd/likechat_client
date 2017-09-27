package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.app.library.util.JSONUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取客服信息
 */
public class FetchCustomerService extends BaseReqRsp
{
    public String rspCustomInfo;

    /**
     * 获取客服信息
     *
     * @param handler
     * @param tag
     */
    public FetchCustomerService(Handler handler, Object tag)
    {
        super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.FETCH_CUSTOM_SERVICE, false, tag);
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "home/getCustomerService";

        return url;
    }

    @Override
    public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
    {
        switch (httpStatusCode)
        {
        case 429:
            rspResultCode = HttpUtil.Result.ERROR_DENIAL_OF_SERVICE;
            break;
        case 200:
            rspResultCode = HttpUtil.Result.OK;
            try
            {
                JSONObject jsonObject = new JSONObject(httpBody);
                if (jsonObject.optInt("code") == 0)
                {
                    JSONObject jsonData = jsonObject.optJSONObject("data");
                    rspCustomInfo = JSONUtil.getString(jsonData, "info");
                }
                else
                {
                    rspResultCode = HttpUtil.Result.ERROR_PARSE_ERROR;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                rspResultCode = HttpUtil.Result.ERROR_PARSE_ERROR;
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
