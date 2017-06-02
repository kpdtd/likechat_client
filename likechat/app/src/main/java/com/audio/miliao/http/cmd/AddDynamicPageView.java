package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 1、当用户点击播放音频或视频时，访问服务器增加一次点击量；
 * 2、点击一条动态的多个照片，只访问一次服务器（点击量仅算一次）；
 */
public class AddDynamicPageView extends BaseReqRsp
{
    // 动态ID
    public int reqDynamicId;

    /**
     * 1、当用户点击播放音频或视频时，访问服务器增加一次点击量；
     * 2、点击一条动态的多个照片，只访问一次服务器（点击量仅算一次）；
     *
     * @param handler
     * @param dynamicId  动态ID
     * @param tag
     */
    public AddDynamicPageView(Handler handler, int dynamicId, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.ADD_ATTENTION, false, tag);

        reqDynamicId = dynamicId;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "find/addDynamicPageView";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("id", reqDynamicId);
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
