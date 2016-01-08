package com.topad.util;


public class Constants {
    // 内部版本号
    public static final int INTERNAL_VERSION = 1;
    public static final String T_CHANNEL = "HD_COMMON";

    public static String softVersionName;
    /*********
     * 接口
     *********/
    // 登录
    public static final String URL_LOGIN = "/user/login";
    // 注册
    public static final String URL_REGISTER = "/user/register";
    // 获取验证码
    public static final String URL_GETCODE = "/user/getcode";
    // 重置密码
    public static final String URL_RESETPWD = "/user/resetpwd";

    // 个人资料-上传图片（头像、身份证、毕业证、名片、公司认证营业执照、媒体代理证明）
    public static final String UPLOAD_PHOTO = "/user/uploadphoto";

    // 个人资料-上传头像
    public static final String UPLOAD_HEAD = "/user/updatehead";
    // 获取个人资料
    public static final String GETINFO = "/user/getinfo";
    // 提交个人资料
    public static final String SAVEINFO = "/user/savebasicinfo";
    // 更新个人简介
    public static final String UPDATE_INTRO = "/user/updateintro";
    // 获取广告服务产品列表（查询或我的产品列表）
    public static final String URL_AD_SERVICE_GETLIST = "/adservice/getlist";
    /**
     * 更新职业
     */
    public static final String URL_UPDATEJOB = "/user/updatejob";

    /*********
     * 下载图片拼接*/
    public static final String IMAGE_URL_HEADER = "/userimg/";
    /**
     * 测试环境
     */
    public static final String URL_TEST = "http://topad.uput.cn";
    /**
     * 生产环境
     */
    public static final String URL_PUBLISH = "http://topad.uput.cn";


    // 当前环境，默认测试环境
    public static String CURR_URL = URL_TEST;

    // 钱包的环境  true 表示线上环境  false 测试环境
    public static boolean WALLET_ONLINE = false;
    // 支付的环境  true 表示线上环境  false 测试环境
    public static boolean PAY_ONLINE = false;

    /**
     * 获取当前环境
     *
     * @return 当前环境URL
     */
    public static String getCurrUrl() {
        // debug状态为false时，返回生产环境
        return (CURR_URL);
    }

    /**
     * 切换正式还是测试环境
     */
    public static void setUrlOnline(boolean isOnline) {
        if (isOnline) {
            CURR_URL = URL_PUBLISH;
            WALLET_ONLINE = true;
            PAY_ONLINE = true;
        } else {
            CURR_URL = URL_TEST;
            WALLET_ONLINE = false;
            PAY_ONLINE = false;
        }
    }


    /**
     * 广播Action
     **/
    public static final String BroadCast_Action_GETZHIYE = "BroadCast_Action_Login";   //获得职业
    public static final String BroadCast_Action_LogOut = "BroadCast_Action_Logout"; //登出
    public static final String BroadCast_Action_Clear_Token = "BroadCast_Action_Clear_Token";   //清除token
    public static final String BroadCast_Action_SUCCEED_Login_INDEX = "BroadCast_Action_SUCCEED_Login_INDEX";   //登录成功后跳转的tabindex


}