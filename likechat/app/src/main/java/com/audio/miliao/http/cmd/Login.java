package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.LoginEvent;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.vo.UserRegisterVo;

import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 登录
 * 调用说明：
 * 1、当app采用qq和微信登录成功并且获得用户的基本信息后（微信UserInfo接口调用成功后）
 * 调用此接口，成功后服务器会返回用户的id；
 * <p>
 * 2、调用registerAndLogin接口成功后应在http头设置用户信息，
 * 以后每次请求http头都应传递用户id，服务器以此作为用户已经登录的依据
 * <p>
 * 3、如果失败应该提示用户重新登录.
 */
public class Login extends BaseReqRsp
{
    public UserRegisterVo reqUserRegisterVo;
    public int rspUserId;

    /**
     * 登录
     *
     * @param handler
     * @param userRegisterVo
     * @param tag
     */
    public Login(Handler handler, UserRegisterVo userRegisterVo, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.LOGIN, false, tag);
        this.reqUserRegisterVo = userRegisterVo;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "mine/registerAndLogin";

        return url;
    }

    @Override
    public String getReqBody()
    {
        return reqUserRegisterVo.toJsonString();
    }

    @Override
    public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
    {
        //theApp.showToast("Login " + httpStatusCode + ";" + httpBody);
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
                JSONObject data = jsonObject.optJSONObject("data");
                rspUserId = data.optInt("id");
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
        LoginEvent event = new LoginEvent(reqUserRegisterVo, rspUserId);
        //theApp.showToast("Login onFinish");
        if (rspResultCode == HttpUtil.Result.OK)
        {
            AppData.setUserInfo(reqUserRegisterVo);
            AppData.setOpenId(reqUserRegisterVo.getOpenId());
            AppData.setCurUserId(rspUserId);

            event.setIsSucceed(true);
        }

        EventBus.getDefault().post(event);
    }
}
