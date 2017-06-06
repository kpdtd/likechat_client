package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.vo.ActorPageVo;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取主播详情
 */
public class FetchActorPage extends BaseReqRsp
{
	public int reqActorId;
	public ActorPageVo rspActorPageVo;

	/**
	 * 获取主播详情
	 * @param handler
	 * @param actorId 用户ID
	 * @param tag
	 */
	public FetchActorPage(Handler handler, int actorId, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_ACTOR_PAGE, false, tag);

		reqActorId = actorId;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "home/getActorPage";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("id", reqActorId);
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
				JSONObject jsonData = jsonObject.optJSONObject("data");
				rspActorPageVo = ActorPageVo.parse(jsonData, ActorPageVo.class);
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
