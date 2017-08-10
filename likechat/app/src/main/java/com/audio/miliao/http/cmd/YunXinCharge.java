package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.JSONUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 在接通主播后，应立即调用此接口进行计费<br/>
 * 返回成功则继续通话，失败则立即挂断<br/>
 * 场景：<br/>
 * 1、用户接通主播后立即调用此接口-isFirstNotify = true<br/>
 * 2、通话每60秒调用此接口 isFirstNotify = false
 */
public class YunXinCharge extends BaseReqRsp
{
    public long reqActorId;
    /** 本次通话在服务器产生的记录id，第一次传0即可，可从结果得到余额和计费记录id。后面每到一分钟实时扣费传入这个id */
    public long reqRecordId;
    /** 本次通话在服务器产生的记录id，第一次传0即可，可从结果得到余额和计费记录id。后面每到一分钟实时扣费传入这个id */
    public long rspRecordId;
    /** 余额 */
    public long rspBalance;

    /**
     * 在接通主播后，应立即调用此接口进行计费<br/>
     * 返回成功则继续通话，失败则立即挂断<br/>
     * 场景：<br/>
     * 1、用户接通主播后立即调用此接口-isFirstNotify = true<br/>
     * 2、通话每60秒调用此接口 isFirstNotify = false
     *
     * @param handler
     * @param actorId  用户ID
     * @param recordId  本次通话在服务器产生的记录id，第一次传0即可，可从结果得到余额和计费记录id。后面每到一分钟实时扣费传入这个id
     * @param tag
     */
    public YunXinCharge(Handler handler, long actorId, long recordId, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.YUNXIN_CHARGE, false, tag);

        reqActorId = actorId;
        reqRecordId = recordId;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "yunxin/chargeNotify";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("actorId", reqActorId);
            jsonObject.put("recordId", reqRecordId);
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
                JSONObject json = new JSONObject(httpBody);
                JSONObject jsonData = json.optJSONObject("data");
                rspRecordId = JSONUtil.getLong(jsonData, "recordId");
                rspBalance = JSONUtil.getLong(jsonData, "balance");
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
