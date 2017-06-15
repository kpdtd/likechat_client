package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.vo.AccountBalanceVo;

import org.json.JSONObject;

import java.util.List;


/**
 * AccountBalanceVo对象
 1、余额：数字
 2、产品列表
 */
public class FetchAccountBalance extends BaseReqRsp
{
	public AccountBalanceVo rspAccountBalanceVo;

	/**
	 * 获取首页所有内容接口
	 * @param handler
	 * @param tag
	 */
	public FetchAccountBalance(Handler handler, Object tag)
	{
		super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.FETCH_ACCOUNT_BALANCE, false, tag);
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "accounting/accountBalance";

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
				rspAccountBalanceVo = AccountBalanceVo.parse(jsonData, AccountBalanceVo.class);
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
