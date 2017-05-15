package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.UserInfo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 登录
 */
public class Login extends BaseReqRsp
{
	// 登录类型，qq
	public static final String TYPE_QQ = "qq";
	// 登录类型，微信
	public static final String TYPE_WEIXIN = "weixin";

	private String reqLoginType;
	private UserInfo reqUserInfo;

	/**
	 * 增加关注
	 * @param handler
	 * @param type 登录类型(weixin或qq)
	 * @param userInfo
	 * @param tag
	 */
	public Login(Handler handler, String type, UserInfo userInfo, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.LOGIN, false, tag);
		this.reqLoginType = type;
		this.reqUserInfo = userInfo;
	}

	public String getReqLoginType()
	{
		return reqLoginType;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "mine/login";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("type", reqLoginType);
			jsonObject.put("openId", reqUserInfo.openId);
			jsonObject.put("access_token", reqUserInfo.accessToken);
			jsonObject.put("expires_in", reqUserInfo.expiresIn);
			jsonObject.put("nickname", reqUserInfo.nickname);
			jsonObject.put("avatar", reqUserInfo.avatar);
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
			AppData.setUserInfo(reqUserInfo);
		}
	}
}
