package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.app.library.util.StringUtil;
import com.app.library.vo.ActorDynamicVo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.EntityUtil;
import com.app.library.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 获取主播动态列表
 */
public class FetchActorDynamicList extends BaseReqRsp
{
    // 分页戳，第一次传0或null。访问接口后服务器会返回下一次分页数字。
    // 当用户下拉取新数据时带上回传的stamp
    // JSON示例：{"tag":1,"stamp":"N"}
    public String reqStamp;
    /** 主播id */
    public int reqActorId;

    public List<ActorDynamicVo> rspActorDynamicVos;

    // 分页戳，第一次传0或null。访问接口后服务器会返回下一次分页数字。
    // 当用户下拉取新数据时带上回传的stamp
    // JSON示例：{"tag":1,"stamp":"N"}
    public String rspStamp;

    public boolean rspHasNextPage = false;

    /**
     * 获取主播动态列表
     *
     * @param handler
     * @param actorId 主播id
     * @param stamp   分页戳 分页戳，第一次传0或null。访问接口后服务器会返回下一次分页数字
     * @param tag
     */
    public FetchActorDynamicList(Handler handler, int actorId, String stamp, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_ACTOR_DYNAMIC_LIST, false, tag);

        reqActorId = actorId;
        reqStamp = stamp;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "find/getActorDynamicList";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("id", reqActorId);
            if (StringUtil.isNotEmpty(reqStamp))
            {
                jsonObject.put("stamp", reqStamp);
            }
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
                rspStamp = JSONUtil.getString(jsonObject, "stamp");
                rspHasNextPage = JSONUtil.getBoolean(jsonObject, "nextPage", false);
                rspActorDynamicVos = new ArrayList<>();
                if (jsonArray != null)
                {
                    EntityUtil.parseList(jsonArray, rspActorDynamicVos, ActorDynamicVo.class);
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
