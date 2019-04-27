package zjc.qualitytrackingee.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import zjc.qualitytrackingee.MyApplication;

public class VolleyRequest {
    //请求对象
    public static StringRequest stringRequest;
    public static Context context;

    //GET Function
    public static void RequestGet(Context mContext, String url, String tag, VolleyInterface vif){
        //获取全局请求队列，并且取消掉tag标签，防止重复请求
        MyApplication.getHttpRequestQueue().cancelAll(tag);
        //创建请求对象，使用GET请求方式，进行实例化
        //url 访问地址
        stringRequest = new StringRequest(Request.Method.GET, url, vif.loadingListener(), vif.errorListener());
        //为请求对象设置标签，使其能在加入全局队列后，能被寻找到
        stringRequest.setTag(tag);
        //添加到全局队列中
        MyApplication.getHttpRequestQueue().add(stringRequest);
    }

    //Post Function
    public static void RequestPost(Context mContext, String url,
                                   String tag, final Map<String, Object> params, VolleyInterface vif){

        MyApplication.getHttpRequestQueue().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.POST, url, vif.loadingListener(), vif.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return super.getParams();
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getHttpRequestQueue().add(stringRequest);
    }

}
