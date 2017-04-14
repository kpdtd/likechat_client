package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 取消关注
 */
public class CancelAttention extends BaseReqRsp
{
	public int reqUserId;
	public int reqActorId;
	/**
	 * 取消关注
	 * @param handler
	 * @param userId 用户ID
	 * @param actorId 主播ID
	 * @param tag
	 */
	public CancelAttention(Handler handler, int userId, int actorId, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.CANCEL_ATTENTION, false, tag);

		reqUserId = userId;
		reqActorId = actorId;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "cancelAttention";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("userId", reqUserId);
			jsonObject.put("actorId", reqActorId);
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
