package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 我的好友（我关注的）
 * 返回关注数、粉丝数以及我的好友列表,默认显示关注列表。（图片、昵称、性别、年龄、签名）
 */
public class FetchMyFriends extends BaseReqRsp
{
	public String reqStamp;
	/**
	 * 返回关注数、粉丝数以及我的好友列表,默认显示关注列表。（图片、昵称、性别、年龄、签名）
	 * @param handler
	 * @param stamp 分页戳，第一次传0或null
	 * @param tag
	 */
	public FetchMyFriends(Handler handler, String stamp, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_MY_FRIENDS, false, tag);

		reqStamp = stamp;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "mine/getMyFriends";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("stamp", reqStamp);
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
		}
	}
}
