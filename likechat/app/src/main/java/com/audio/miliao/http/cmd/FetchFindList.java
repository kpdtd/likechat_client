package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.StringUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取发现页内容：
 * 1、当动态价格>0时说明是付费动态。
 */
public class FetchFindList extends BaseReqRsp
{
    // 最新-1  热门-2  关注-3
    public int reqTag;
    // 分页戳，第一次传0或null。访问接口后服务器会返回下一次分页数字。
    // 当用户下拉取新数据时带上回传的stamp
    // JSON示例：{"tag":1,"stamp":"N"}
    public String reqStamp;

    /**
     * 获取发现页内容：
     * 1、当动态价格>0时说明是付费动态。
     *
     * @param handler
     * @param nTag    最新-1  热门-2  关注-3
     * @param stamp   分页戳 分页戳，第一次传0或null。访问接口后服务器会返回下一次分页数字
     * @param tag
     */
    public FetchFindList(Handler handler, int nTag, String stamp, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_ACTOR_LIST_BY_TAG, false, tag);

        reqTag = nTag;
        reqStamp = stamp;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "find/getFindList";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("tag", reqTag);
            if (StringUtil.isNotEmpty(reqStamp))
            {
                jsonObject.put("stamp", reqStamp);
            }
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
