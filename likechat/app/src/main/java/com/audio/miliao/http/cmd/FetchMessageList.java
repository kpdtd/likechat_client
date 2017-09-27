package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.app.library.util.Checker;
import com.app.library.util.DBUtil;
import com.app.library.vo.MessageVo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 获取消息界面的消息列表
 */
public class FetchMessageList extends BaseReqRsp
{
	public List<MessageVo> rspMessageList;

	/**
	 * 获取消息界面的消息列表
	 * @param handler
	 * @param tag
	 */
	public FetchMessageList(Handler handler, Object tag)
	{
		super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.FETCH_MESSAGE_LIST, false, tag);
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "message/getMessageList";

		return url;
	}

	@Override
	public String getReqBody()
	{
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
				JSONArray jsonData = jsonObject.optJSONArray("data");
				rspMessageList = new ArrayList<>();
				if (Checker.isNotEmpty(jsonData))
				{
					for (int i = 0; i < jsonData.length(); i++)
					{
						MessageVo messageVo = MessageVo.parse(jsonData.optJSONObject(i), MessageVo.class);
						rspMessageList.add(messageVo);
					}
				}
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
			if (Checker.isNotEmpty(rspMessageList))
			{
				DBUtil.save(rspMessageList);
			}
		}
	}
}
