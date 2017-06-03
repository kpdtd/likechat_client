package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.entity.GsonObj;
import com.audio.miliao.event.FetchHomeContentEvent;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;
import com.audio.miliao.vo.ActorVo;
import com.audio.miliao.vo.BannerVo;
import com.audio.miliao.vo.TagVo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 获取首页所有内容接口
 */
public class FetchHomeContent extends BaseReqRsp
{
	public List<TagVo> rspTagVos;
	public List<BannerVo> rspBannerVos;
	public List<ActorVo> rspActorVos;

	/**
	 * 获取首页所有内容接口
	 * @param handler
	 * @param tag
	 */
	public FetchHomeContent(Handler handler, Object tag)
	{
		super(HttpUtil.Method.GET, handler, HttpUtil.RequestCode.FETCH_HOME_CONTENT, false, tag);
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "home/getHomePageContent";

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
		theApp.showToast(httpStatusCode + ";" + httpBody);
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
				JSONArray jsonTagVo = jsonData.optJSONArray("tagsVo");
				JSONArray jsonBannerVo = jsonData.optJSONArray("bannerVo");
				JSONArray jsonActorsVo = jsonData.optJSONArray("actorsVo");

				rspTagVos = new ArrayList<>();
				rspBannerVos = new ArrayList<>();
				rspActorVos = new ArrayList<>();
				parse(jsonTagVo, rspTagVos, TagVo.class);
				parse(jsonBannerVo, rspBannerVos, BannerVo.class);
				parse(jsonActorsVo, rspActorVos, ActorVo.class);
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
		FetchHomeContentEvent event = new FetchHomeContentEvent(rspTagVos, rspBannerVos, rspActorVos);
		if (rspResultCode == HttpUtil.Result.OK)
		{
			event.setIsSucceed(true);
		}
		else
		{
			event.setIsSucceed(false);
		}

		EventBus.getDefault().post(event);
	}

	private <T extends GsonObj<T>> void parse(JSONArray jsonArray, List<T> list, Class<T> cls)
	{
		int len = jsonArray.length();
		for (int i= 0; i < len; i++)
		{
			JSONObject jsonObject = jsonArray.optJSONObject(i);
			T obj = T.parse(jsonObject.toString(), cls);
			list.add(obj);
		}
	}
}
