package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.Checker;
import com.app.library.vo.ActorPageVo;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取主播详情
 */
public class FetchActorPage extends BaseReqRsp
{
	public int reqActorId;
	public String reqYunxinId;
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

	/**
	 * 获取主播详情
	 * @param handler
	 * @param yunxinId 云信Id
	 * @param tag
	 */
	public FetchActorPage(Handler handler, String yunxinId, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_ACTOR_PAGE, false, tag);

		reqYunxinId = yunxinId;
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
			// 主播id和云信id二者传一个就行
			if (reqActorId > 0)
			{
				jsonObject.put("id", reqActorId);
			}
			else if (Checker.isNotEmpty(reqYunxinId))
			{
				jsonObject.put("accid", reqYunxinId);
			}
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
