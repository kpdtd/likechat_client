package com.audio.miliao.http;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.util.LogUtil;
import com.audio.miliao.util.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttps
{
	private static void init()
	{
		if (OkHttpUtils.getInstance() == null)
		{
			HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
			
			OkHttpClient okHttpClient = new OkHttpClient.Builder()
			// .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
				// 其他配置
				.build();

			OkHttpUtils.initClient(okHttpClient);
		}
	}

	/**
	 * 异步执行请求<br/>
	 * 请求被放入队列执行
	 * 
	 * @param req
	 * @param tag 可以根据tag取消操作
	 */
	public static void call(BaseReqRsp req, Object tag)
	{
		try
		{
			init();

			if (req.onPrepare())
			{
				String strMethod = req.reqHttpMethod;
				if (strMethod.equals("GET"))
				{
					get(req, tag);
				}
				else if (strMethod.equals("POST"))
				{
					post(req, tag);
				}
				else if (strMethod.equals("PUT"))
				{
					put(req, tag);
				}
				else if (strMethod.equals("DELETE"))
				{
					delete(req, tag);
				}
				else if (strMethod.equals("HEAD"))
				{
					head(req, tag);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			req.onFinish();
			req.onNotify();
		}
	}
	
	/**
	 * 同步执行请求
	 * 
	 * @param req
	 */
	public static void callSync(BaseReqRsp req)
	{
		try
		{
			init();

			if (req.onPrepare())
			{
				String strMethod = req.reqHttpMethod;
				if (strMethod.equals("GET"))
				{
					getSync(req);
				}
				else if (strMethod.equals("POST"))
				{
					postSync(req);
				}
				else if (strMethod.equals("PUT"))
				{
					putSync(req);
				}
				else if (strMethod.equals("DELETE"))
				{
					deleteSync(req);
				}
				else if (strMethod.equals("HEAD"))
				{
					headSync(req);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			req.onFinish();
			req.onNotify();
		}
	}

	private static void get(final BaseReqRsp req, Object tag)
	{
		try
		{
			String url = req.getReqUrl();
			addHeaders(OkHttpUtils.get().url(url).tag(tag), req).build().execute(new RspCallback(req));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void post(final BaseReqRsp req, Object tag)
	{
		try
		{
			String url = req.getReqUrl();
			String strReqBody = req.getReqBody();
			Callback callback = new RspCallback(req);
			
			if (StringUtil.isNotEmpty(strReqBody))
			{
				MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
				addHeaders(OkHttpUtils.postString().url(url).tag(tag).content(strReqBody).mediaType(mediaType), req)
				    .build()
				    .execute(callback);
			}
			else
			{
				List<BaseReqRsp.KeyValuePair> listPair = req.getReqBodyPairs();
				Map<String, String> params = pairToMap(listPair);
				OkHttpUtils.post().url(url).tag(tag).params(params).build().execute(callback);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void put(BaseReqRsp req, Object tag)
	{
		try
		{
			String url = req.getReqUrl();
			String strReqBody = req.getReqBody();
			addHeaders(OkHttpUtils.put().url(url).tag(tag).requestBody(RequestBody.create(null, strReqBody)), req).build().execute(
					new RspCallback(req));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void delete(BaseReqRsp req, Object tag)
	{
		try
		{
			String url = req.getReqUrl();
			addHeaders(OkHttpUtils.delete().url(url).tag(tag), req).build().execute(new RspCallback(req));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void head(BaseReqRsp req, Object tag)
	{
		try
		{
			String url = req.getReqUrl();
			addHeaders(OkHttpUtils.head().url(url).tag(tag), req).build().execute(new RspCallback(req));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// =================================================================
	// 同步操作
	private static void getSync(final BaseReqRsp req)
	{
		try
		{
			String url = req.getReqUrl();
			Response response = addHeaders(OkHttpUtils.get().url(url), req).build().execute();
			handleSyncResponse(req, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void postSync(final BaseReqRsp req)
	{
		try
		{
			String url = req.getReqUrl();
			String strReqBody = req.getReqBody();
			Response response = null;

			if (StringUtil.isNotEmpty(strReqBody))
			{
				MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
				response = addHeaders(OkHttpUtils.postString().url(url).content(strReqBody).mediaType(mediaType), req)
							    .build()
							    .execute();
			}
			else
			{
				List<BaseReqRsp.KeyValuePair> listPair = req.getReqBodyPairs();
				Map<String, String> params = pairToMap(listPair);
				response = addHeaders(OkHttpUtils.post().url(url).params(params), req).build().execute();
			}

			handleSyncResponse(req, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void putSync(final BaseReqRsp req)
	{
		try
		{
			String url = req.getReqUrl();
			String strReqBody = req.getReqBody();
			Response response = addHeaders(OkHttpUtils.put().url(url).requestBody(strReqBody), req).build().execute();
			handleSyncResponse(req, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void deleteSync(final BaseReqRsp req)
	{
		try
		{
			String url = req.getReqUrl();
			Response response = addHeaders(OkHttpUtils.delete().url(url), req).build().execute();
			handleSyncResponse(req, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void headSync(final BaseReqRsp req)
	{
		try
		{
			String url = req.getReqUrl();
			Response response = addHeaders(OkHttpUtils.head().url(url), req).build().execute();
			handleSyncResponse(req, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 处理同步请求的返回结果
	 * 
	 * @param req
	 * @param response
	 */
	private static void handleSyncResponse(final BaseReqRsp req, final Response response)
	{
		try
		{
			if (response != null)
			{
				int code = response.code();
				String body = response.body().toString();
				Headers headers = response.headers();

				req.parseHttpResponse(code, headersToPair(headers), body);
			}
			else
			{
				req.setError(1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			req.setError(1);
		}
		finally
		{
			req.onFinish();
		}
	}

	private static OkHttpRequestBuilder addHeaders(OkHttpRequestBuilder builder, BaseReqRsp req)
	{
		builder.addHeader("Accept", "application/json");
		builder.addHeader("Content-Type", req.reqContentType);
		String strToken = AppData.getToken();
		if (StringUtil.isNotEmpty(strToken))
		{
			builder.addHeader("Authorization", "Bearer " + strToken);
		}

		return builder;
	}

	// ========================================================================

	private static List<BaseReqRsp.KeyValuePair> headersToPair(Headers headers)
	{
		if (headers != null && headers.size() > 0)
		{
			List<BaseReqRsp.KeyValuePair> pairs = new ArrayList<>();
			for (int i = 0; i < headers.size(); i++)
			{
				pairs.add(new BaseReqRsp.KeyValuePair(headers.name(i), headers.value(i)));
			}

			return pairs;
		}

		return null;
	}

	private static Map<String, String> pairToMap(List<BaseReqRsp.KeyValuePair> listPair)
	{
		if (listPair != null && listPair.size() > 0)
		{
			Map<String, String> params = new HashMap<>();
			for (int i = 0; i < listPair.size(); i++)
			{
				BaseReqRsp.KeyValuePair pair = listPair.get(i);
				params.put(pair.getName(), pair.getValue());
			}

			return params;
		}

		return null;
	}

	// =====================================================================
	private static class RspEntity
	{
		String body;
		int code;
		List<BaseReqRsp.KeyValuePair> headers;
	}

	private static class RspCallback extends Callback<RspEntity>
	{
		BaseReqRsp mReq = null;

		RspCallback(BaseReqRsp req)
		{
			mReq = req;
		}

		@Override
		public void onError(Call call, Exception ex, int id)
		{
			try
			{
				String strEx = ex.getMessage();
				int index = strEx.indexOf(":");
				if (index >= 0)
				{
					String strCode = strEx.substring(index + 1, strEx.length()).trim();
					mReq.parseHttpResponse(Integer.valueOf(strCode), null, "");
				}
				else
				{
					mReq.setError(1);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				mReq.setError(1);
			}
			finally
			{
				mReq.onFinish();
				mReq.onNotify();
			}
		}

		@Override
		public void onResponse(RspEntity entity, int id)
		{
			try
			{
				if (entity != null)
				{
					mReq.parseHttpResponse(entity.code, entity.headers, entity.body);
				}
				else
				{
					mReq.setError(1);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				mReq.setError(1);
			}
			finally
			{
				mReq.onFinish();
				mReq.onNotify();
			}
		}

		@Override
		public RspEntity parseNetworkResponse(Response response, int id) throws Exception
		{
			try
			{
				String strBody = response.body().string();
				int code = response.code();
				Headers headers = response.headers();

				LogUtil.i(mReq.getClass().getSimpleName() + " statusCode : " + code);
				LogUtil.i(mReq.getClass().getSimpleName() + " resp : " + strBody);

				RspEntity entity = new RspEntity();
				entity.body = strBody;
				entity.code = code;
				entity.headers = headersToPair(headers);

				return entity;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return null;
		}
	}
}
