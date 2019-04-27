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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.othershe.nicedialog.NiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import zjc.qualitytrackingee.EEactivity.EEMainActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.TimeCountUtil;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class
TextLoginActivity extends AppCompatActivity {
    final NiceDialog progressDialog=NiceDialog.init();
    @BindView(R.id.text_login_yzm_bt)
    Button text_login_yzm_bt;
    @BindView(R.id.textlogin_user_et) EditText textlogin_user_et;
    @BindView(R.id.textlogin_yzm_et) EditText textlogin_yzm_et;
    @BindView(R.id.textlogin_bt) Button textlogin_bt;
    private String phone;
    private TimeCountUtil mTimeCountUtil; //按钮倒计时
    private SharedPreferences pref;//共享偏好对象
    private SharedPreferences.Editor editor;//共享偏好编辑器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_login_activity);
       // getSupportActionBar().hide();
        ButterKnife.bind(this);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor=pref.edit();
        initView();
        mTimeCountUtil = new TimeCountUtil(text_login_yzm_bt, 60000, 1000);

    }
    public void  initView(){
        text_login_yzm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMSSDK.getVerificationCode("86", textlogin_user_et.getText().toString());
                phone = textlogin_user_et.getText().toString();
            }
            });


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



    @OnClick(R.id.textlogin_bt)
    public void textlogin_bt_Onclick(){
        boolean flag=false;
        SMSSDK.submitVerificationCode("86",textlogin_user_et.getText().toString(),textlogin_yzm_et.getText().toString());

        if(TextUtils.isEmpty(textlogin_user_et.getText().toString())){
            flag=true;
        }
        if(TextUtils.isEmpty(textlogin_yzm_et.getText().toString())){
            flag=true;
        }
        if(flag){
            Toast.makeText(this,"请填写账号/验证码后再进行登陆",Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setLayoutId(R.layout.loading_layout)
                    .setWidth(100)
                    .setHeight(100)
                    .setDimAmount(0.5f)
                    .setOutCancel(true).show(this.getSupportFragmentManager());
            loading();

        }
    }
    public void loading(){
        //身份验证 e_power 0是管理员 1是员工 e_status
        final String url= Net.TextLogin+"?e_phone="+textlogin_user_et.getText().toString();
        VolleyRequest.RequestGet(getContext(),url, "textlogin",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONObject jsonObject1=jsonObject.getJSONObject("FirstloginM");
                            String power=jsonObject1.getString("e_power");
                            String status=jsonObject1.getString("e_status");
                            String cid=jsonObject1.getString("c_id");
                            if(power.equals("0")){
                                Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                                MyApplication.setC_id(cid);
                                MyApplication.setE_power(power);
                                MyApplication.setE_phone(textlogin_user_et.getText().toString());
                                editor.putString("e_phone",textlogin_user_et.getText().toString());
                                editor.putString("c_id",cid);
                                editor.putString("e_power",power);
                                editor.apply();
                                Intent intent = new Intent(TextLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                if (status.equals("0")) {
                                    Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                                    MyApplication.setC_id(cid);
                                    MyApplication.setE_power(power);
                                    MyApplication.setE_phone(textlogin_user_et.getText().toString());
                                    editor.putString("e_phone",textlogin_user_et.getText().toString());
                                    editor.putString("c_id",cid);
                                    editor.putString("e_power",power);
                                    editor.apply();
                                    Intent intent = new Intent(TextLoginActivity.this, EEMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getContext(), "该账号未通过审核", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                    }
                });
    }
//    public void initView() {
//
//                // mTimeCountUtil.start();
//                SMSSDK.getVerificationCode("86", textlogin_user_et.getText().toString());
//                phone = textlogin_user_et.getText().toString();
//            }
//        });
//
//        EventHandler eh = new EventHandler() {
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                mHandler.sendMessage(msg);
//            }
//        };
//        SMSSDK.registerEventHandler(eh);
//    }
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
                    mTimeCountUtil.start();
                    Toast.makeText(getApplicationContext(),"发送验证码成功",Toast.LENGTH_LONG).show();
                   // ercode=0;
                } else {
                    // TODO 处理错误的结果
                    ((Throwable) data).printStackTrace();
                   // ercode=1;
                    Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                }
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证码验证通过的结果
                    Toast.makeText(getApplicationContext(),"验证成功",Toast.LENGTH_LONG).show();
                   // ercode=0;
                } else {
                    // TODO 处理错误的结果
                    //ercode=2;
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(getApplicationContext(),"验证失败",Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

}
