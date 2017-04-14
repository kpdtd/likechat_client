package com.audio.miliao.http;

import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.Map;

public abstract class BaseReqRsp
{
	/**
	 * 
	 */
	// private static final long serialVersionUID = -5310653172761581155L;

	public String reqContentType;
	public int reqConnTimeout;
	public int reqSoTimeout;
	public String reqHttpMethod;
	public String reqPostFileName;

	public int rspResultCode;
	public Object rspCallBackTag;

	/**
	 * 是否需要发送身份验证，发往外部服务器的连接包含Authorization可能会导致错误
	 * */
	public boolean isNeedAuthorization = true;

	/**
	 * 命令是否后台自动运行，后台自动运行的命令和用户执行的命令使用不同的线程执行
	 * */
	public boolean isBackgroundCommand;

	public Handler callback_handler;
	public int callback_notifyCode;

	public static final String FORM_URLENCODE = "application/x-www-form-urlencoded";
	public static final String APPLICATION_JSON = "application/json";

	public static final String BASE_URL = "http://114.215.221.15:8080/likechat_server_service/";
	// public static final String BASE_URL = "https://service.motoilet.com";

	public static class KeyValuePair
	{
		private String mKey;
		private String mValue;

		public KeyValuePair(String key, String value)
		{
			mKey = key;
			mValue = value;
		}

		public String getName()
		{
			return mKey;
		}

		public String getValue()
		{
			return mValue;
		}
	}

	/**
	 * 服务前缀
	 * @return
	 */
	public static String getBaseURL()
	{
		return BASE_URL;
	}

	public static String getPrevBaseURL()
	{
		return getBaseURL() + "iot/v1/";
	}

	public static boolean isSucceed(BaseReqRsp baseReqRsp)
	{
		if (baseReqRsp != null && baseReqRsp.rspResultCode == HttpResult.RESULT_OK)
		{
			return true;
		}

		return false;
	}

	public BaseReqRsp(String reqMethod, Handler handler, int notifyCode, boolean backgroundCommand, Object tag)
	{
		init(reqMethod, handler, notifyCode, backgroundCommand, tag);
	}

	/**
	 * 本地方法，用户主动执行，一般为处理本地逻辑，不需要访问网络
	 * */
	public void localExecute()
	{
		// do nothing
	}

	/**
	 * 每个命令可以自定义自己的headers
	 * */
	public Map<String, String> getHeaders()
	{
		return null;
	}

	/**
	 * 在send()方法之前被调用，提供一个处理机会。
	 * 
	 * @return true：执行网络操作；false：不执行网络操作
	 * */
	public boolean onPrepare()
	{
		// do nothing.
		return true;
	}

	// 无论网络操作成功还是失败，提供一个处理机会。
	public void onFinish()
	{
		// do nothing.
	}

	public void send()
	{
		try
		{
			// mWebService.sendRequest(this);
			OKHttps.call(this, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void sendSync()
	{
		try
		{
			// mWebService.sendRequestSync(this);
			OKHttps.callSync(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 设置超时时间，单位（秒）
	 */
	public void setTimeout(int connTimeout, int soTimeout)
	{
		this.reqConnTimeout = connTimeout;
		this.reqSoTimeout = soTimeout;
	}

	private void init(String reqMethod, Handler handler, int notifyCode, boolean backgroundCommand, Object tag)
	{
		this.reqHttpMethod = reqMethod;

		this.callback_handler = handler;
		this.callback_notifyCode = notifyCode;
		this.rspCallBackTag = tag;

		this.reqContentType = "application/json";
		// this.reqContentType = "application/x-www-form-urlencoded";
		// this.reqConnTimeout = 30;
		// this.reqSoTimeout = 30;
		this.setTimeout(120, 120);
		this.reqPostFileName = null;
		this.isBackgroundCommand = backgroundCommand;
	}
	
	public int getRequestCode()
	{
		return callback_notifyCode;
	}

	public String getReqBody()
	{
		return null;
	}

	public List<KeyValuePair> getReqBodyPairs()
	{
		return null;
	}

	public abstract String getReqUrl();

	// public abstract void parseHttpResponse(int httpStatusCode, Header[]
	// HttpHeaders, String httpBody);

	public abstract void parseHttpResponse(int statusCode, List<KeyValuePair> headers, String body);

	public void setError(int errorCode)
	{
		rspResultCode = errorCode;
	}

	public void onNotify()
	{
		if (callback_handler != null)
		{
			Message msg = callback_handler.obtainMessage(callback_notifyCode);
			msg.obj = this;
			msg.sendToTarget();
		}
	}
}
