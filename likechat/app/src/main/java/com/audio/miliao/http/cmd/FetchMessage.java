package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.util.DBUtil;
import com.app.library.vo.MessageVo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取聊天界面的消息列表
 */
public class FetchMessage extends BaseReqRsp
{
	public int reqActorId;
	public MessageVo rspMessageVo;

	/**
	 * 获取聊天界面的消息列表
	 * @param handler
	 * @param tag
	 */
	public FetchMessage(Handler handler, int actorId, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_MESSAGE, false, tag);
		reqActorId = actorId;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "message/getChat";

		return url;
	}

	@Override
	public String getReqBody()
	{
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("actorId", reqActorId);
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
	{
		//theApp.showToast(httpStatusCode + ";" + httpBody);
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
				rspMessageVo = MessageVo.parse(jsonData, MessageVo.class);
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
			if (rspMessageVo != null)
			{
				DBUtil.insertOrReplace(rspMessageVo);
			}
		}
	}
}
