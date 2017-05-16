package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;
import com.audio.miliao.util.JSONUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 微信获取用户信息
 */
public class WXFetchUserinfo extends BaseReqRsp
{
	private String reqAccessToken;
	private String reqOpenId;
	public String rspResult;
	public String rspNickname;
	public String rspProvince;
	public String rspCity;
	public String rspAvatar;
	public String rspGender;

	/**
	 * 微信Oauth2
	 * @param handler
	 * @param tag
	 */
	public WXFetchUserinfo(Handler handler, String accessToken, String openId, Object tag)
	{
		super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.WX_FETCH_USERINFO, false, tag);

		reqAccessToken = accessToken;
		reqOpenId = openId;
	}

	@Override
	public String getReqUrl()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("https://api.weixin.qq.com/sns/userinfo");
		sb.append("?access_token=" + reqAccessToken);
		sb.append("&openid=" + reqOpenId);

		return sb.toString();
	}

	@Override
	public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
	{
		theApp.showToast("WXFetchUserinfo:" + httpStatusCode + ";" + httpBody);
		rspResult = httpBody;
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
				rspNickname = JSONUtil.getString(jsonObject, "nickname");
				rspProvince = JSONUtil.getString(jsonObject, "province");
				rspCity = JSONUtil.getString(jsonObject, "city");
				rspAvatar = JSONUtil.getString(jsonObject, "headimgurl");
				rspGender = JSONUtil.getString(jsonObject, "sex");
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
