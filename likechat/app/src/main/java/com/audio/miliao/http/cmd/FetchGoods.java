package com.audio.miliao.http.cmd;

import android.os.Handler;

import com.app.library.vo.GoodsVo;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.util.EntityUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 获取后台配置的产品列表：
 * 参数传入goodsType，
 * 当值 =1时，指定获取购买hi币的列表。
 *     =2时，获取vip产品列表 ，
 *     =3时获取随机红包产品列表。
 * (必须传值根据id获取，暂时不支持获取所有产品列表)
 */
public class FetchGoods extends BaseReqRsp
{
    public static final int HI_COIN = 1; // hi币
    public static final int VIP = 2; // vip充值
    public static final int RED_PACKET = 3; // 红包

    // hi币-HI_COIN  vip充值-VIP  红包-RED_PACKET
    public int reqGoodsType;

    public List<GoodsVo> rspGoodsVoList;

    /**
     * 获取发现页内容：
     * 1、当动态价格>0时说明是付费动态。
     *
     * @param handler
     * @param goodsType    hi币-HI_COIN  vip充值-VIP  红包-RED_PACKET
     * @param tag
     */
    public FetchGoods(Handler handler, int goodsType, Object tag)
    {
        super(HttpUtil.Method.POST, handler, HttpUtil.RequestCode.FETCH_GOODS, false, tag);

        reqGoodsType = goodsType;
    }

    @Override
    public String getReqUrl()
    {
        String url = getPrevBaseURL() + "accounting/getGoods";

        return url;
    }

    @Override
    public String getReqBody()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("goodsType", reqGoodsType);
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
                rspGoodsVoList = new ArrayList<>();
                if (jsonArray != null)
                {
                    EntityUtil.parseList(jsonArray, rspGoodsVoList, GoodsVo.class);
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
