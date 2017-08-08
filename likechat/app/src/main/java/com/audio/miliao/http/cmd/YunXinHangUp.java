package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 挂断和主播的通话后立即调用，为了是更新通话记录相关的信息（如，本次时长等）
 */
public class YunXinHangUp extends BaseReqRsp
{
    /** 本次通话在服务器产生的记录id，第一次传0即可，可从结果得到余额和计费记录id。后面每到一分钟实时扣费传入这个id */
    public long reqRecordId;

    /**
     * 挂断和主播的通话后立即调用，为了是更新通话记录相关的信息（如，本次时长等）
     *
     * @param handler
     * @param recordId  本次通话在服务器产生的记录id
     * @param tag
     */
    public YunXinHangUp(Handler handler, long recordId, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.YUNXIN_HANG_UP, false, tag);

        reqRecordId = recordId;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "yunxin/hangUpNotify";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
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
