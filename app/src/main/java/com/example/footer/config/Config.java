package com.example.footer.config;

/**
 * Created by android on 11/12/15.
 */
public class Config {

    public static boolean DEBUG = true;


    //正式环境下的端口
    //public static final String BASE_URL="http://192.168.0.198:8080/spoor";
    //远程服务器
    public static final String BASE_URL="http://42.123.126.78:8999/spoor";
    //public static final String BASE_URL="http://192.168.0.100:8080/spoor";

    //版本获取接口
    public static final String VERSION_URL=BASE_URL+"/version/findVersion";

    //版本上传接口
    public static final String UPDATEVERSION=BASE_URL+"/version/updateVersion";

    public static final String UPDATE_SAVENAME="file";
    //注册
    public static final String REGISTER_URL =BASE_URL+"/user/register" ;
    //登录
    public static final String USER_LOGIN_URL = BASE_URL + "/user/userLogin";
    //忘记密码
    public static final String FORGET_PASS_URL=BASE_URL+"/user/forgetPassword";
    //更改密码
    public static final String PASSWORD_MODIFY_URL = BASE_URL + "/user/modifyPassword";
    //注册短信验证码
    public static final String REGISTER_YANZHENGMA=BASE_URL+"/user/obtainRegisterMessage";
    //短信验证码
    public static final String DUANXIN_YANZHENGMA=BASE_URL+"/user/obtainMessage";
    //获取用户个人信息
    public static final String FIND_USER=BASE_URL+"/user/findUser";
    //修改个人信息
    public static final String MODIFYUSER_URL=BASE_URL+"/user/modifyUser";
    //足迹设置
    public static final String SET_TAGS=BASE_URL+"/user/setTags";
    //上传头像
    public static final String UPLOAD_USERICON=BASE_URL+"/user/modifyUserIcon";
    //昵称
    public static final String USERNICKNAME_URL=BASE_URL+"/user/modifyUserNickName";
    //性别
    public static final String USERGENDER_URL=BASE_URL+"/user/modifyUserGender";
    //生日
    public static final String USERBIRTHDAY_URL=BASE_URL+"/user/modifyUserBirthday";
    //添加足迹发布
    public  static final String INSERTSPOORR_URL=BASE_URL+"/spoor/insertSpoor";
    //足迹首页的列表显示
    public  static final String FINDSPOORBYUSEr_URL=BASE_URL+"/spoor/findSpoorByUser";
    //足迹的收藏
    public static final String SPOORCOLLECT_URL=BASE_URL+"/spoorCollect/spoorCollect";
    //我的收藏
    public static final String FINDSPOORCOLLECT_URL=BASE_URL+"/spoorCollect/findSpoorCollect";
    //收益接口
    public static final String SPOORBENEFIT_URL=BASE_URL+"/spoorBenefit/spoorBenefit";
    //我的足迹
    public static final String FINDSPOORBYLOGINID_URL=BASE_URL+"/spoor/findSpoorByLoginId";
    //搜索  关键字搜索
    public static final String FINDSPOORBYTAG_URL=BASE_URL+"/spoor/findSpoorByTag";
    //寻迹标签列表
    public static final String ALLTAG_URL=BASE_URL+"/spoorTag/findTag";
    //足迹详情
    public static final String FINDSPOORDETAIL_URL=BASE_URL+"/spoor/findSpoorDetail";
    //发表评论
    public static final String ADDCOMMENT_URL=BASE_URL+"/comment/addComment";
    //我的评论列表
    public static final String FINDCOMMENTLIST_URL=BASE_URL+"/comment/findCommentList";
    //删除
    public static final String DELETESPOOR_URL=BASE_URL+"/spoor/deleteSpoor";
    //极光绑定接口
    public static final String BANDJUSHID=BASE_URL+"/user/bandRegistrationId";
    //极光解绑接口
    public static final String UNBINDJUSHID=BASE_URL+"/user/removeRegistrationId";
    //发现未读消息
    public static final String FINDUNREAD_URL=BASE_URL+"/user/findUnreadMessage";
    //清空消息
    public static final String REMOVESEND_URL=BASE_URL+"/user/removeSendMessage";
    //上传背景图片
    public static final String MODIFY_USER_ICON=BASE_URL+"/user/modifyUserHomeIcon";
}
