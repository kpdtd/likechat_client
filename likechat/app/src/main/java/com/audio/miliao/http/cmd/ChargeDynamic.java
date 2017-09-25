package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 查看动态时扣费
 */
public class ChargeDynamic extends BaseReqRsp
{
	public int reqActorId, reqPrice;

	/**
	 * 查看动态时扣费
	 * @param handler
	 * @param actorId
	 * @param price 价格（分）
	 * @param tag
	 */
	public ChargeDynamic(Handler handler, int actorId, int price, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.CHARGE_DYNAMIC, false, tag);
		reqActorId = actorId;
		reqPrice = price;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "accounting/callPay2";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("actorId", reqActorId);
			jsonObject.put("price", reqPrice);
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
				JSONObject jsonObject = new JSONObject(httpBody);
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
