package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.JSONUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 微信Oauth2
 */
public class WXOauth extends BaseReqRsp
{
	public String reqURL;
	public String rspResult;
	public String rspAccessToken;
	public int rspExpiresIn;
	public String rspRefreshToken;
	public String rspOpenId;

	/**
	 * 微信Oauth2
	 * @param handler
	 * @param tag
	 */
	public WXOauth(Handler handler, String url, Object tag)
	{
		super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.WX_OAUTH, false, tag);

		reqURL = url;
	}

	@Override
	public String getReqUrl()
	{
		return reqURL;
	}

	@Override
	public String getReqBody()
	{
		return "";
	}

	@Override
	public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
	{
		//UIUtil.showToastShort(theApp.CONTEXT, httpStatusCode + ";" + httpBody);
		rspResult = httpBody.toString();
		//theApp.showToast("WXOauth:" + httpStatusCode + ";" + httpBody.toString());
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
				rspAccessToken = JSONUtil.getString(jsonObject, "access_token");
				rspExpiresIn = JSONUtil.getInt(jsonObject, "expires_in");
				rspRefreshToken = JSONUtil.getString(jsonObject, "refresh_token");
				rspOpenId = JSONUtil.getString(jsonObject, "openid");
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
