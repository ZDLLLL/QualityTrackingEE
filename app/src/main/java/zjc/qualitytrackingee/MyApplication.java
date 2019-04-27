package zjc.qualitytrackingee;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    private static String e_phone;//账号
    private static RequestQueue queue;
    private static String e_power;//权限
    private static String c_id;//公司id
    private static String e_token;//密码
    private  SharedPreferences pref;//共享偏好对象
    private  SharedPreferences.Editor editor;//共享偏好编辑器对象
    private static Context context;//上下文环境变量，用于多个活动之间的变量共享

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();
        queue = Volley.newRequestQueue(getApplicationContext());
        //极光推送
        JPushInterface.setDebugMode(true);
        if(pref.getBoolean("notify",true)){

            JPushInterface.init(this);
        }


    }


    public static String getE_phone() {
        return e_phone;
    }

    public static void setE_phone(String e_phone) {
        MyApplication.e_phone = e_phone;
    }

    public static String getE_token() {
        return e_token;
    }

    public static void setE_token(String e_token) {
        MyApplication.e_token = e_token;
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void setPref(SharedPreferences pref) {
        this.pref = pref;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }
    public static RequestQueue getHttpRequestQueue(){
        return queue;
    }

    public static String getE_power() {
        return e_power;
    }

    public static void setE_power(String e_power) {
        MyApplication.e_power = e_power;
    }

    public static String getC_id() {
        return c_id;
    }

    public static void setC_id(String c_id) {
        MyApplication.c_id = c_id;
    }
    private static Map<String,Activity> destoryMap = new HashMap<>();


    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName,activity);
    }
    /**
     *销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet=destoryMap.keySet();
        for (String key:keySet){
            destoryMap.get(key).finish();
        }
        destoryMap.remove(activityName);
    }

}
