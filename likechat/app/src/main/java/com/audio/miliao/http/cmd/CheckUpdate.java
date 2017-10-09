package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.app.library.vo.AppUpdateVo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 软件更新
 */
public class CheckUpdate extends BaseReqRsp
{
    public int reqVersionCode;
    public AppUpdateVo rspAppUpdate;

    /**
     * 软件更新
     *
     * @param handler
     * @param tag
     */
    public CheckUpdate(Handler handler, int versionCode, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.CHECK_UPDATE, false, tag);
        reqVersionCode = versionCode;
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
            jsonObject.put("versionCode", reqVersionCode);
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
        //theApp.showToast("Login;" + httpStatusCode + ":" + httpBody);
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
                if (jsonData != null)
                {
                    rspAppUpdate = AppUpdateVo.parse(jsonData, AppUpdateVo.class);
                }
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
