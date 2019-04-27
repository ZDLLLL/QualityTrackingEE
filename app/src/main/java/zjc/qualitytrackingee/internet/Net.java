package zjc.qualitytrackingee.internet;


public class Net {
    public final static String HEAD = "https://sdsy.zzjc.edu.cn/SDSY/";//学校
    public final  static String STUDENT="http://192.168.43.52:8080/QianLiZhuiZong/";//服务器
    public final  static String HEAD1="http://169.254.107.225:8080/QianLiZhuiZong/";//本机
    public final  static String HEAD2="http://192.168.43.64:8080/QianLiZhuiZong/";//本机
    public final  static String HEAD3="http://192.168.0.100:8080/QianLiZhuiZong/";//316wifi
    public final  static String HEAD4="http://www.mcartoria.com/QianLiZhuiZong/";//316服务器

    public final  static String LOGIN=HEAD4+"FirstLogin";//登录
    public final static String ENROLL=HEAD4+"registered";//企业注册
    public final static String Search = HEAD4+"/Search";//公司搜索框
    public final  static String AllCompany=HEAD4+"GetAllCompany";//登录
    public final  static String CHANGE_PASSWD=HEAD4+"changePassword";//修改密码
    public final  static String TextLogin=HEAD4+"FirstLoginM";//短信验证登录
    public final  static String CheckEmployee=HEAD4+"AdminAudit";//员工审核列表
    public final  static String AgreeCheckEmployee=HEAD4+"AdminAuditRes";//同意
    public final  static String DisAgreeCheckEmployee=HEAD4+"AdminAuditRes";//不同意
    public final  static String GetEmployee=HEAD4+"GetAllEmpBySC";//员工列表
    public final  static String UploadGoods=HEAD4+"addCommodity";//上传产品信息
    public final  static String GetAllGoodsClass=HEAD4+"getAllCommodityClassByCid";//获得所有商品类
    public final  static String UploadGoodsClass=HEAD4+"addCommodityClass";//上传商品类
    public final  static String DeleteGoodsClass=HEAD4+"deleteCommodityClass";//删除某个商品类
    public final  static String GetAllJob=HEAD4+"GetAllJobByCid";//获得所有职务类
    public final  static String UploadJob=HEAD4+"addJobBycid";//上传职务类
    public final  static String DeleteJob=HEAD4+"deleteJob";//删除某个职务
    public final  static String GetAllUserFeedBack=HEAD4+"getAllFeedbackBycid";//获得所有的用户反馈差评
    public final  static String GetAllBadUserFeedBack=HEAD4+"ChaFeedback";//获得所有的用户反馈差评

    public final  static String GetUser=HEAD4+"GetAllPersonInfo";//获得所有的用户反馈
    public final  static String UploadImg=HEAD4+"EupImg";//修改头像
    public final  static String GetAllGoodsbyGoodsClassName=HEAD4+"getCommodityByClass";//获得所有商品类
    public final  static String UploadPhone=HEAD4+"upadateEmp";//获得所有商品类
    public final  static String GetAllGoodsBacth=HEAD4+"getAllAccessIdBycname";//获得所有商品批次
    public final  static String DeleteEmployee=HEAD4+"deleteEmp";//删除员工
    public final  static String DeleteGoodsBatch=HEAD4+"deleteEmp";//删除某个批次里面的商品
    public final  static String GetCommodityByAcid=HEAD4+"getCommodityByAcid";//根据批次获得商品
    public final  static String PushJiGuang=HEAD4+"pushMessage";//根据批次获得商品
    public final  static String SubmitRead=HEAD4+"ReadFeedback";//根据批次获得商品
    public final  static String GetGoodsFeedBack=HEAD4+"getFeedbackBycoid";//根据物品编号获得反馈
    public final  static String SaoYiSao=HEAD4+"EdecodeQr";//扫码
    public final  static String GetAllGoodsNumber=HEAD4+"getCompanyNum";//扫码
    public final  static String GetGoodsDuty=HEAD4+"selectHeadPerson";//扫码
    public final  static String GetJpush=HEAD4+"getJpush";//推送列表
    public final  static String GetAllSchedule=HEAD4+"getScheduleByphone";//推送列表

//    public final  static String LOGIN=HEAD4+"FirstLogin";//登录
//    public final static String ENROLL=HEAD4+"registered";//企业注册
//    public final static String Search = HEAD4+"/Search";//公司搜索框
//    public final  static String AllCompany=HEAD4+"GetAllCompany";//登录
//    public final  static String CHANGE_PASSWD=HEAD4+"changePassword";//修改密码
//    public final  static String TextLogin=HEAD4+"FirstLoginM";//短信验证登录
//    public final  static String CheckEmployee=HEAD4+"AdminAudit";//员工审核列表
//    public final  static String AgreeCheckEmployee=HEAD4+"AdminAuditRes";//同意
//    public final  static String DisAgreeCheckEmployee=HEAD4+"AdminAuditRes";//不同意
//    public final  static String GetEmployee=HEAD4+"GetAllEmpBySC";//员工列表
//    public final  static String UploadGoods=HEAD4+"addCommodity";//上传产品信息
//    public final  static String GetAllGoodsClass=HEAD4+"getAllCommodityClassByCid";//获得所有商品类
//    public final  static String UploadGoodsClass=HEAD4+"addCommodityClass";//上传商品类
//    public final  static String DeleteGoodsClass=HEAD4+"deleteCommodityClass";//删除某个商品类
//    public final  static String GetAllJob=HEAD4+"GetAllJobByCid";//获得所有职务类
//    public final  static String UploadJob=HEAD4+"addJobBycid";//上传职务类
//    public final  static String DeleteJob=HEAD4+"deleteJob";//删除某个职务
//    public final  static String GetAllUserFeedBack=HEAD4+"getAllFeedbackBycid";//获得所有的用户反馈差评
//    public final  static String GetAllBadUserFeedBack=HEAD4+"ChaFeedback";//获得所有的用户反馈差评
//
//    public final  static String GetUser=HEAD4+"GetAllPersonInfo";//获得所有的用户反馈
//    public final  static String UploadImg=HEAD4+"EupImg";//修改头像
//    public final  static String GetAllGoodsbyGoodsClassName=HEAD4+"getCommodityByClass";//获得所有商品类
//    public final  static String UploadPhone=HEAD4+"upadateEmp";//获得所有商品类
//    public final  static String GetAllGoodsBacth=HEAD4+"getAllAccessIdBycname";//获得所有商品批次
//    public final  static String DeleteEmployee=HEAD4+"deleteEmp";//删除员工
//    public final  static String DeleteGoodsBatch=HEAD4+"deleteEmp";//删除某个批次里面的商品
//    public final  static String GetCommodityByAcid=HEAD4+"getCommodityByAcid";//根据批次获得商品
//    public final  static String PushJiGuang=HEAD4+"pushMessage";//根据批次获得商品
//    public final  static String SubmitRead=HEAD4+"ReadFeedback";//根据批次获得商品
//    public final  static String GetGoodsFeedBack=HEAD4+"getFeedbackBycoid";//根据物品编号获得反馈
//    public final  static String SaoYiSao=HEAD4+"EdecodeQr";//扫码
//    public final  static String GetAllGoodsNumber=HEAD4+"getCompanyNum";//扫码
//    public final  static String GetGoodsDuty=HEAD4+"selectHeadPerson";//扫码
//    public final  static String GetJpush=HEAD4+"getJpush";//推送列表
//    public final  static String GetAllSchedule=HEAD4+"getScheduleByphone";//推送列表
}
