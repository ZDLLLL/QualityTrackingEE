package zjc.qualitytrackingee.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.TimeCountUtil;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class ChangePhoneActivity extends AppCompatActivity {
    @BindView(R.id.changephone_oldphone_tv)
    TextView changephone_oldphone_tv;
    @BindView(R.id.changephone_ok_bt)
    Button changephone_ok_bt;
    @BindView(R.id.changephone_back_ll)
    LinearLayout changephone_back_ll;
    @BindView(R.id.changephone_yzm_bt) Button changephone_yzm_bt;
    @BindView(R.id.changephone_newphone_et)
    EditText changephone_newphone_et;
    @BindView(R.id.changephone_yzm_et)
    EditText changephone_yzm_et;
    private TimeCountUtil mTimeCountUtil; //按钮倒计时
    private static int code;
    private SharedPreferences pref;//共享偏好对象
    private SharedPreferences.Editor editor;//共享偏好编辑器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        changephone_oldphone_tv.setText(MyApplication.getE_phone());
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor=pref.edit();
        mTimeCountUtil = new TimeCountUtil(changephone_yzm_bt, 60000, 1000);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }
    @OnClick(R.id.changephone_back_ll)
    public  void changephone_back_ll_Onclick(){
        finish();
    }
    //验证手机号是否正确 身份过期 重新登录
    @OnClick(R.id.changephone_ok_bt)
    public void changephone_ok_bt_Onclick(){
        //提交短信验证码
        SMSSDK.submitVerificationCode("86",changephone_newphone_et.getText().toString(),changephone_yzm_et.getText().toString());
        if (TextUtils.isEmpty(changephone_newphone_et.getText().toString())) {
            Toast.makeText(ChangePhoneActivity.this, "请输入你的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }  else if (code==1) {
            Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
            return;
        }else if (code==2) {
            Toast.makeText(getApplicationContext(),"验证失败",Toast.LENGTH_LONG).show();
            return;
        }else{
            uploadPhone();

        }
    }
    //获取验证码
    @OnClick(R.id.changephone_yzm_bt)
    public void changephone_yzm_bt_Onclick(){
        mTimeCountUtil.start();
        SMSSDK.getVerificationCode("86", changephone_newphone_et.getText().toString());

    }
    public void uploadPhone(){
        final String url = Net.UploadPhone+"?Newe_phone="+changephone_newphone_et.getText().toString()+"&e_phone="+MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(), url, "UploadPhone",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.v("zjc", url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String status=jsonObject.getString("status");
                            if("1".equals(status)) {//是否成功 1成功 0失败
                                if("0".equals(MyApplication.getE_power())) {//管理员
                                    Toast.makeText(ChangePhoneActivity.this, "换绑成功，请重新登录", Toast.LENGTH_LONG).show();
                                    editor.putString("e_phone", "");
                                    //MyApplication.setE_phone(changephone_newphone_et.getText().toString());
                                    editor.apply();
                                    Intent intent = new Intent(ChangePhoneActivity.this, LoginActivity.class);
                                    MyApplication.destoryActivity("UserInfoActivity");
                                    MyApplication.destoryActivity("MainActivity");
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(ChangePhoneActivity.this, "换绑成功，请重新登录", Toast.LENGTH_LONG).show();
                                    editor.putString("e_phone", "");
                                    //MyApplication.setE_phone(changephone_newphone_et.getText().toString());
                                    editor.apply();
                                    Intent intent = new Intent(ChangePhoneActivity.this, LoginActivity.class);
                                    MyApplication.destoryActivity("UserInfoActivity");
                                    MyApplication.destoryActivity("EEMainActivity");
                                    startActivity(intent);
                                    finish();
                                }
                            }else {
                                Toast.makeText(ChangePhoneActivity.this, "换绑失败", Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {

                    }
                });
    }
    @SuppressLint("HandlerLeak")

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) { // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果

                    Toast.makeText(getContext(),"发送验证码成功",Toast.LENGTH_LONG).show();
                    code=0;
                } else {
                    // TODO 处理错误的结果
                    ((Throwable) data).printStackTrace();
                    code=1;
                    Toast.makeText(getContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                }
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证码验证通过的结果
                    Toast.makeText(getContext(),"验证成功",Toast.LENGTH_LONG).show();
                    code=0;
                } else {
                    // TODO 处理错误的结果
                    code=2;
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(getContext(),"验证失败",Toast.LENGTH_LONG).show();
                }
            }

        }

    };
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
