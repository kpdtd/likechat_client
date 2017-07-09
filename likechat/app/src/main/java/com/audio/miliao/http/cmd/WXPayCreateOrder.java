package com.audio.miliao.http.cmd;

import android.os.Handler;
import android.util.Log;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;
import com.audio.miliao.util.WXUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONObject;

import java.util.List;


/**
 * 微信支付 生成支付订单
 */
public class WXPayCreateOrder extends BaseReqRsp
{
	/**
	 * 微信Oauth2
	 * @param handler
	 * @param tag
	 */
	public WXPayCreateOrder(Handler handler, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.WX_PAY_CREATE_ORDER, false, tag);
	}

	@Override
	public String getReqUrl()
	{
		// return "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String url = getPrevBaseURL() + "/wechat/unifiedOrder";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("appid", WXUtil.app_id());
			json.put("mch_id", WXUtil.mch_id());
			json.put("device_info", "WEB"); // 非必须
			json.put("nonce_str", "" + System.currentTimeMillis()); // 随机字符串
			json.put("body", "充值"); // 商品描述
			json.put("out_trade_no", ""); // 商户订单号
			json.put("total_fee", ""); // 总金额
			json.put("spbill_create_ip", ""); // 终端IP
			json.put("notify_url", ""); // 通知地址
			json.put("trade_type", ""); // 交易类型
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return json.toString();
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

				if (null != json && !json.has("retcode"))
				{
					PayReq req = new PayReq();
					// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					//Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					WXUtil.api().sendReq(req);
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
