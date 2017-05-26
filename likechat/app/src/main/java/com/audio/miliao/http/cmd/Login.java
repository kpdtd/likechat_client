package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.UserInfo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.theApp;

import java.util.List;


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
    // 登录类型，qq
    public static final String TYPE_QQ = "qq";
    // 登录类型，微信
    public static final String TYPE_WEIXIN = "weixin";

    private UserInfo reqUserInfo;

    /**
     * 登录
     *
     * @param handler
     * @param userInfo
     * @param tag
     */
    public Login(Handler handler, UserInfo userInfo, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.LOGIN, false, tag);
        this.reqUserInfo = userInfo;
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
        return reqUserInfo.toJsonString();
    }

    @Override
    public void parseHttpResponse(int httpStatusCode, List<KeyValuePair> headers, String httpBody)
    {
        theApp.showToast("Login;" + httpStatusCode + ":" + httpBody);
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
            AppData.setUserInfo(reqUserInfo);
        }
    }
}
