package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.app.library.util.Checker;
import com.app.library.util.LogUtil;
import com.app.library.vo.ChatMsg;
import com.app.library.vo.MessageStateVo;
import com.app.library.vo.MessageVo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.DBUtil;

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
					LogUtil.d("messageVo list:" + jsonData);
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
				List<MessageStateVo> messageStateVos = new ArrayList<>();
				for (MessageVo messageVo : rspMessageList)
				{
					MessageStateVo messageStateVo = new MessageStateVo();
					messageStateVo.setId(messageVo.getId());
					messageStateVo.setMessageId(messageVo.getId());
					messageStateVo.setIsRead(false);

					messageStateVos.add(messageStateVo);
				}

				DBUtil.insertOrReplaceMessageVos(rspMessageList);
				DBUtil.insertOrReplaceChatMsgs(createChatMsgs(rspMessageList));
				DBUtil.insertOrReplaceMessageStateVos(messageStateVos);
			}
		}
	}

	/**
	 * 根据messageVo生产ChatMsg<br/>
	 * getMessageList接口返回的MessageVo中只有message有值，chat没有值
	 * @param messageVos
	 * @return
	 */
	private List<ChatMsg> createChatMsgs(List<MessageVo> messageVos)
	{
		List<ChatMsg> chatMsgs = new ArrayList<>();

		if (Checker.isNotEmpty(messageVos))
		{
			for (MessageVo messageVo : messageVos)
			{
				ChatMsg chatMsg = new ChatMsg();
				chatMsg.setText(messageVo.getMessage());
				chatMsg.setActorId(messageVo.getActorId());
				chatMsg.setSenderId(messageVo.getActorId());
				chatMsg.setSenderAvatar(messageVo.getIcon());

				chatMsgs.add(chatMsg);
			}
		}

		return chatMsgs;
	}
}
