package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;
import com.audio.miliao.util.JSONUtil;
import com.audio.miliao.vo.WeChatUnifiedOrderReqVo;
import com.audio.miliao.vo.WeChatUnifiedOrderReturnVo;

import org.json.JSONObject;

import java.util.List;


/**
 * 创建微信支付订单
 * <p>
 * 调用说明：（用户需登录）
 * 在调用支付宝sdk时先向服务器发送消息，创建订单。
 * 主要同步用户openid和订单号这两个重要信息。如果不同步，
 * 最终支付的异步订返回后也无法为用户充值
 */
public class CreateWXPayOrder extends BaseReqRsp
{
    public WeChatUnifiedOrderReqVo reqOrderInfo;
    public WeChatUnifiedOrderReturnVo rspOrderResult;

    /**
     * 增加关注
     *
     * @param handler
     * @param orderInfo
     * @param tag
     */
    public CreateWXPayOrder(Handler handler, WeChatUnifiedOrderReqVo orderInfo, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.CREATE_ALIPAY_ORDER, false, tag);
        this.reqOrderInfo = orderInfo;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "accounting/createOder";

        return url;
    }

    @Override
    public String getReqBody()
    {
        return reqOrderInfo.toJsonString();
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
            try
            {
                JSONObject jsonObject = new JSONObject(httpBody);
                int code = JSONUtil.getInt(jsonObject, "code");
                if (code == 0)
                {
                    rspResultCode = HttpUtil.Result.OK;
                    JSONObject jsonData = jsonObject.optJSONObject("data");
                    rspOrderResult = WeChatUnifiedOrderReturnVo.parse(jsonData, WeChatUnifiedOrderReturnVo.class);
                }
                else
                {
                    rspResultCode = HttpUtil.Result.ERROR_UNKNOWN;
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
