package zjc.qualitytrackingee.utils;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public abstract class VolleyInterface {
    //请求成功、请求失败的抽象类
    public Context mContext;//上下文环境
    public static Listener<String> mListener;//请求成功的监听
    public static ErrorListener mErrorListener;//请求失败的监听
    public VolleyInterface(Context Context, Listener<String> listener, ErrorListener errorListener){
        this.mContext = Context;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public abstract void onMySuccess(String result);
    public abstract void onMyError(VolleyError error);

    //实例化请求成功的方法
    public Listener<String> loadingListener(){
        mListener = new Listener<String>() {
            @Override
            public void onResponse(String arg0) {
                //传递回去
                onMySuccess(arg0);
            }
        };
        //返回
        return mListener;
    }

    //实例化请求失败的方法
    public ErrorListener errorListener(){
        mErrorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                //传递回去
                onMyError(arg0);
            }
        };
        return mErrorListener;
    }
}
