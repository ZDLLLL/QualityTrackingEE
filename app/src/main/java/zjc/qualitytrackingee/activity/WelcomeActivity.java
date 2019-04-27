package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import zjc.qualitytrackingee.EEactivity.EEMainActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //getSupportActionBar().hide();
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                //进行字符串的非null判断
                //pref.getString("stu_id","");用于活动偏好选项stu_id的值，如果没有则返回""
                if(TextUtils.isEmpty(pref.getString("e_phone",""))){
                    intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                }else {
                    MyApplication.setE_power(pref.getString("e_power",""));
                    MyApplication.setC_id(pref.getString("c_id",""));
                    String id=pref.getString("c_id","");
                    String power=pref.getString("e_power","");
                    MyApplication.setE_phone(pref.getString("e_phone",""));
                    MyApplication.setE_token(pref.getString("e_password",""));
                   if(power.equals("1")) {
                        intent = new Intent(WelcomeActivity.this, EEMainActivity.class);
                    }
                    else {
                        intent=new Intent(WelcomeActivity.this,MainActivity.class);
                        intent.putExtra("c_id",id);
                    }
                }

                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
