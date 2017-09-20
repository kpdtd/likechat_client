package com.audio.miliao.util;

/**
 * 网易云信相关操作
 */
public class YunXinUtil
{
//    /**
//     * 云信登录
//     * @param username 云信账户account
//     * @param token 云信账户token
//     */
//    public static void login(final String username, final String token)
//    {
//        try
//        {
//            // String strToken = MD5.getStringMD5("123456");
//            String strAccount = username;
//            String strToken = token;
//            LoginInfo info = new LoginInfo(strAccount, strToken, YXConfig.APP_KEY); // config...
//            RequestCallback<LoginInfo> callback =
//                    new RequestCallback<LoginInfo>()
//                    {
//                        @Override
//                        public void onSuccess(LoginInfo loginInfo)
//                        {
//                            theApp.showToast("onSuccess");
//
//                            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
//                            //NimUIKit.setAccount(loginInfo.getAccount());
//                            AppData.setYunXinAccount(loginInfo.getAccount());
//                            AppData.setYunXinToken(loginInfo.getToken());
//                        }
//
//                        @Override
//                        public void onFailed(int i)
//                        {
//                            theApp.showToast("onFailed " + i);
//                        }
//
//                        @Override
//                        public void onException(Throwable throwable)
//                        {
//                            theApp.showToast("onException " + throwable.toString());
//                        }
//                    };
//            NIMClient.getService(AuthService.class).login(info).setCallback(callback);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            theApp.showToast("Exception " + e.toString());
//        }
//    }
}
