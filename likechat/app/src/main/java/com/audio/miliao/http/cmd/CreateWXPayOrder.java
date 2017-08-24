package com.audio.miliao.http.cmd;

import android.os.Handler;
import android.util.Log;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;
import com.netease.nim.uikit.miliao.vo.WeChatUnifiedOrderReqVo;
import com.netease.nim.uikit.miliao.vo.WeChatUnifiedOrderReturnVo;

import org.json.JSONObject;

import java.util.List;


/**
 * 创建微信支付订单
 * <p>
 * 调用说明：（用户需登录）
 * 在调用微信sdk时先向服务器发送消息，创建订单。
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
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.WX_PAY_CREATE_ORDER, false, tag);
        this.reqOrderInfo = orderInfo;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "/wechat/unifiedOrder";

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
        theApp.showToast(httpStatusCode + ":" + httpBody);
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
                JSONObject json = new JSONObject(httpBody);

                if (null != json && !json.has("retcode") && json.has("data"))
                {
                    json = json.getJSONObject("data");

                    rspOrderResult = WeChatUnifiedOrderReturnVo.parse(json, WeChatUnifiedOrderReturnVo.class);

//                    PayReq req = new PayReq();
//                    req.appId = json.getString("appid");
//                    req.partnerId = json.getString("partnerid");
//                    req.prepayId = json.getString("prepay_id");
//                    req.nonceStr = json.getString("noncestr");
//                    req.timeStamp = json.getString("timestamp");
//                    req.sign = json.getString("sign");
//                    req.packageValue = "Sign=WXPay";// json.getString("package");
//                    req.extData = "app data"; // optional
//                    //Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//
//                    WXUtil.api().sendReq(req);
                }
                else
                {
                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                    //Toast.makeText(PayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                    rspResultCode = HttpUtil.Result.ERROR_INVALID_CODE;
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
