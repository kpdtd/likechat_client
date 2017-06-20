package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.vo.AccountVo;

import org.json.JSONObject;

import java.util.List;


/**
 * 获取首页所有内容接口
 */
public class FetchAccountInfo extends BaseReqRsp
{
	public AccountVo rspAccountVo;

	/**
	 * 获取首页所有内容接口
	 * @param handler
	 * @param tag
	 */
	public FetchAccountInfo(Handler handler, Object tag)
	{
		super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.FETCH_ACCOUNT_INFO, false, tag);
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "accounting/getAccountInfo";

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
				JSONObject jsonData = jsonObject.optJSONObject("data");
				rspAccountVo = AccountVo.parse(jsonData, AccountVo.class);
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
	}
}