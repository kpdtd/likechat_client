package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.EntityUtil;
import com.netease.nim.uikit.miliao.vo.ActorVo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 根据Tag获取20个随机主播
 */
public class FetchActorListByTag extends BaseReqRsp
{
	public String reqTag;
	public List<ActorVo> rspActorVos;

	/**
	 * 根据Tag获取20个随机主播
	 * @param handler
	 * @param strTag Tag
	 * @param tag
	 */
	public FetchActorListByTag(Handler handler, String strTag, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_ACTOR_LIST_BY_TAG, false, tag);

		reqTag = strTag;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "home/getActorListByTag";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("identifying", reqTag);
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
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				rspActorVos = new ArrayList<>();
				EntityUtil.parseList(jsonArray, rspActorVos, ActorVo.class);
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
