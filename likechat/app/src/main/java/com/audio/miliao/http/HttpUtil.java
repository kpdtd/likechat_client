package com.audio.miliao.http;

/**
 * 网络操作结果
 */
public class HttpUtil
{
    public static final String UTF8 = "UTF-8";

    /** 网络操作方式 */
    public static class Method
    {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String HEAD = "HEAD";
    }

    /** 返回的结果 */
    public static class Result
    {
        public static final int CONTINUE = -2; // 请求无需执行网络操作，保留在数据库，下次启动继续执行
        public static final int LOCAL_DELETE = -1; // 请求无需执行网络操作，并且可以删除
        public static final int OK = 0;

        // 网络、系统层的错误。
        public static final int ERROR_UNKNOWN = 1;
        public static final int ERROR_CONNECT_FAILED = 2;
        public static final int ERROR_CONNECT_REFUSED = 3;
        public static final int ERROR_CONNECT_TIMEOUT = 4;
        public static final int ERROR_RECEIVING_DATA = 5;
        public static final int ERROR_PARSE_ERROR = 6;

        // 应用逻辑错误（服务器应答）。
        public static final int ERROR_UNKNOWN_STATUS = 9; // 服务器返回未知意义的状态码
        public static final int ERROR_PARSE_RESPONSE = 10; // 解析服务器的应答数据时出错。
        public static final int ERROR_NOT_FOUND_TOKEN = 11; // 没有找到Token对应的数据
        public static final int ERROR_COMMIT_NULL = 12; // 提交的数据有空内容
        public static final int ERROR_INVALID_CODE = 13; // 用户名或密码错误
        public static final int ERROR_INVALIDATE_CLIENT = 14; // 客户端AppId和Secret错误
        public static final int ERROR_MOBILE_EXIST = 15; // 手机号已经被注册了
        public static final int ERROR_OLD_PASSWORD_ERROR = 16; // 旧密码不正确
        public static final int ERROR_DISK_FULL = 17; // 用户的网络空间已满
        public static final int ERROR_REGISTER_DEVICE_FAILED = 18; // 注册用户设备失败（所有操作都需要携带deviceId，如果没有则需要先执行registerDevice从服务器获取deviceId）
        public static final int ERROR_UNAUTHORIZED = 19; // 未认证
        public static final int ERROR_FILE_EXIST = 20; // 文件或者文件夹已存在
        public static final int ERROR_CONTACT_OUT_OF_TENANT = 21; // 联系人不是同一个公司的不能添加
        public static final int ERROR_DENIAL_OF_SERVICE = 22; // 系统拒绝服务，可能是单个手机号发送次数超限。需要稍候再发送。
    }
    
    public static class RequestCode
    {
        /** 获取首页所有内容接口 */
        public static final int FETCH_HOME_CONTENT = 0;
        /** 增加关注 */
        public static final int ADD_ATTENTION = 1;
        /** 取消关注 */
        public static final int CANCEL_ATTENTION = 2;
        /** 获取主播详情 */
        public static final int FETCH_ACTOR_PAGE = 3;
        /** 根据tag获取20个随机的主播 */
        public static final int FETCH_ACTOR_LIST_BY_TAG = 4;
        /** 微信Oauth */
        public static final int WX_OAUTH = 5;
        /** 微信获取用户信息 */
        public static final int WX_FETCH_USERINFO = 6;
        /** 登录 */
        public static final int LOGIN = 7;
        /** 创建支付宝订单 */
        public static final int CREATE_ALIPAY_ORDER = 8;
        /** 获取获取发现页内容 */
        public static final int FETCH_FIND_LIST = 9;
        /** 获取我的登录信息 */
        public static final int FETCH_MINE_INFO = 10;
        /** 我的好友（我关注的） */
        public static final int FETCH_MY_FRIENDS = 11;
        /** 获取我的粉丝（关注我的） */
        public static final int FETCH_MY_FANS = 12;
        /** 软件更新 */
        public static final int UPDATE = 13;
        /** 微信创建订单 */
        public static final int WX_PAY_CREATE_ORDER = 14;
        /** 获取账户信息 */
        public static final int FETCH_ACCOUNT_INFO = 15;
        /** 获取账户余额，付费价格列表 */
        public static final int FETCH_ACCOUNT_BALANCE = 16;
        /** 获取Vip信息 */
        public static final int FETCH_VIP_MEMBER = 17;
        /** 修改用户信息 */
        public static final int UPDATE_USER_INFO = 18;
        /** 获取云信Token */
        public static final int FETCH_YX_TOKEN = 19;
        /** 云信开始计费 */
        public static final int YUNXIN_CHARGE = 20;
        /** 云信聊天挂断 */
        public static final int YUNXIN_HANG_UP = 21;
        /** 获取主播动态 */
        public static final int FETCH_ACTOR_DYNAMIC_LIST = 22;
        /** 获取客服信息 */
        public static final int FETCH_CUSTOM_SERVICE = 23;
        /** 查看动态时扣费 */
        public static final int CHARGE_DYNAMIC = 24;
        /** 获取消息界面的消息列表 */
        public static final int FETCH_MESSAGE_LIST = 25;
        /** 获取聊天界面的消息列表 */
        public static final int FETCH_CHAT_LIST = 26;
    }

    /** 请求类型 */
    public static class ReqType
    {
        public static final String FORM_URLENCODE = "application/x-www-form-urlencoded";
        public static final String APPLICATION_JSON = "application/json";
    }
}
