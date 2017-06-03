package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.JSONUtil;
import com.audio.miliao.vo.ActorVo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的好友（我关注的）
 * 返回关注数、粉丝数以及我的好友列表,默认显示关注列表。（图片、昵称、性别、年龄、签名）
 */
public class FetchMyFriends extends BaseReqRsp
{
	public String reqStamp;
	// 关注数
	public int rspAttentionCount;
	// 我的粉丝数
	public int rspFansCount;
	// 我的好友列表
	public List<ActorVo> rspFriends;
	// 分页戳，数字值 用Object类型返回，取下一页内容列表时传给服务器
	public String rspStamp;
	// 是否有下一页
	public boolean rspHasNext;

	/**
	 * 返回关注数、粉丝数以及我的好友列表,默认显示关注列表。（图片、昵称、性别、年龄、签名）
	 * @param handler
	 * @param stamp 分页戳，第一次传0或null
	 * @param tag
	 */
	public FetchMyFriends(Handler handler, String stamp, Object tag)
	{
		super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_MY_FRIENDS, false, tag);

		reqStamp = stamp;
	}

	@Override
	public String getReqUrl()
	{
		String url = getPrevBaseURL() + "mine/getMyFriends";

		return url;
	}

	@Override
	public String getReqBody()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("stamp", reqStamp);
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
				rspStamp = JSONUtil.getString(jsonObject, "stamp");
				rspHasNext = JSONUtil.getBoolean(jsonObject, "has_next", false);
				JSONObject jsonData = jsonObject.optJSONObject("data");
				rspAttentionCount = JSONUtil.getInt(jsonData, "attentionCount");
				rspFansCount = JSONUtil.getInt(jsonData, "fansCount");
				JSONArray jsonArray = jsonData.optJSONArray("dataList");
				rspFriends = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject json = jsonArray.getJSONObject(i);
					ActorVo actorVo = ActorVo.parse(json.toString(), ActorVo.class);
					rspFriends.add(actorVo);
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
		}
	}
}
