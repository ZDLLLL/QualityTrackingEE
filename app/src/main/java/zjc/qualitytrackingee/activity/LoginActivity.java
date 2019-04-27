package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import zjc.qualitytrackingee.EEactivity.EEMainActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.MathTools;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;


public class LoginActivity extends AppCompatActivity {
    final NiceDialog progressDialog=NiceDialog.init();
    @BindView(R.id.enroll) Button enroll;
    @BindView(R.id.enter) Button enter;
    @BindView(R.id.textlogin_skip_bt) Button text_login_skip_bt;
    @BindView(R.id.login_user_et)
    EditText login_user_et;
    @BindView(R.id.login_password_et) EditText login_password_et;
    private SharedPreferences pref;//共享偏好对象
    private SharedPreferences.Editor editor;//共享偏好编辑器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      //  getSupportActionBar().hide();
        ButterKnife.bind(this);
       // MyApplication.destoryActivity("EnrollActivity");
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor=pref.edit();
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,EnrollActivity.class);
                startActivity(intent);
            }
        });
//        enter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }
//        @OnClick(R.id.enter)
//    public void enter_Click(){
//            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//            loading();
//        }
    @OnClick(R.id.textlogin_skip_bt)
    public void text_login_skip_bt_Click(){
        Intent intent=new Intent(LoginActivity.this,TextLoginActivity.class);
        startActivity(intent);
    }

        @OnClick(R.id.enter)
    public void enter_Click(){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           // intent.putExtra("c_id",c_id);
            startActivity(intent);
//        boolean flag=false;
//        if(TextUtils.isEmpty(login_user_et.getText().toString())){
//            flag=true;
//        }
//        if(TextUtils.isEmpty(login_password_et.getText().toString())){
//            flag=true;
//        }
//        if(flag){
//            Toast.makeText(this,"请填写账号/密码后再进行登陆",Toast.LENGTH_SHORT).show();
//        }else {
//            progressDialog.setLayoutId(R.layout.loading_layout)
//                    .setWidth(100)
//                    .setHeight(100)
//                    .setDimAmount(0.5f)
//                    .setOutCancel(true).show(this.getSupportFragmentManager());
//            loading();
//           // progressDialog.dismiss();
//
//        }
    }
    public void loading(){
        //身份验证 e_power 0是管理员 1是员工 e_status -1审核失败 0是审核通过的 1是需要审核的
        String pwd= MathTools.getMd5(login_password_et.getText().toString());
        final String url= Net.LOGIN+"?e_phone="+login_user_et.getText().toString()+"&e_password="+pwd;
        VolleyRequest.RequestGet(getContext(),url, "login",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",result);
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONObject Firstlogin=jsonObject.getJSONObject("Firstlogin");
                            try {
                                String user_number = Firstlogin.getString("e_phone");
                                String user_pwd = Firstlogin.getString("e_password");
                                String power=Firstlogin.getString("e_power");
                                String status=Firstlogin.getString("e_status");
                                String c_id=Firstlogin.getString("c_id");
                               // String code=jsonObject.getString("e_status");
                                if(power.equals("0")) {//管理员登录
                                    Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_SHORT).show();

                                    MyApplication.setE_phone(user_number);
                                    MyApplication.setE_token(user_pwd);
                                    MyApplication.setC_id(c_id);
                                    MyApplication.setE_power(power);

                                    editor.putString("c_id",c_id);
                                    editor.putString("e_power",power);
                                    editor.putString("e_phone", user_number);
                                    editor.putString("e_password", user_pwd);
                                    editor.apply();
                                    String id=MyApplication.getC_id();
                                    Log.v("公司编号：",MyApplication.getC_id());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("c_id",c_id);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    if (status.equals("0")) {
                                        Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                                        MyApplication.setE_phone(user_number);
                                        MyApplication.setE_token(user_pwd);
                                        MyApplication.setC_id(c_id);
                                        MyApplication.setE_power(power);
                                        editor.putString("c_id",c_id);
                                        editor.putString("e_power",power);
                                        editor.putString("e_phone", user_number);
                                        editor.putString("e_password", user_pwd);
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, EEMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getContext(), "该账号未通过审核", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }catch (org.json.JSONException j){
                                Toast.makeText(getContext(),"账号/密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(),"登陆失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
