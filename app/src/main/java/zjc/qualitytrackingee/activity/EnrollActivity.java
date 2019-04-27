package zjc.qualitytrackingee.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import zjc.qualitytrackingee.EEactivity.EECompanyActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.MyPagerAdapter;
import zjc.qualitytrackingee.adapter.SearchCompanyAdapter;
import zjc.qualitytrackingee.adapter.SearchCompanyEEAdapter;
import zjc.qualitytrackingee.beans.SearchBean;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.service.SearchCompanyService;
import zjc.qualitytrackingee.service.imp.SearchCompanyServiceImp;
import zjc.qualitytrackingee.utils.MathTools;
import zjc.qualitytrackingee.utils.TimeCountUtil;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;


import static zjc.qualitytrackingee.MyApplication.getContext;

public class EnrollActivity extends AppCompatActivity {
    @BindView(R.id.enroll_tl)
    android.support.design.widget.TabLayout enroll_tl;
    @BindView(R.id.enroll_vp)
    ViewPager enroll_vp;
    private View enroll_employee;
    private View enroll_enterprise;
    private List<String> titleList;//标题 滑动布局
    private List<View> viewList;//视图 滑动布局
    private ArrayAdapter<String> post_adapter;
    private static  List<String> postList=new ArrayList<>();//职务列表
    private Spinner ee_post_spinner;
    private Button ee_bt;
    private Button er_bt;
    private EditText ee_password_et;
    private EditText ee_user_et;
    private EditText er_user_et;
    private EditText er_password_et;
    public EditText ee_company_et;
    public EditText er_company_et;
    private EECompanyActivity eeCompanyActivity;
    private SearchCompanyService searchCompanyService;
    private EditText ee_passwordagain_et;
    private EditText er_passwordagain_et;
    private EditText er_name_et;
    private EditText ee_name_et;
    private RelativeLayout er_rl;
    private RelativeLayout ee_rl;
    private EditText ee_yzm_et;
    private EditText er_yzm_et;
    private Button er_gainyzm_bt;
    private Button ee_gainyzm_bt;
    private String ee_phone;
    private String er_phone;

    private static int ercode;
    private static int eecode;
    private TimeCountUtil erTimeCountUtil; //按钮倒计时
    private TimeCountUtil eeTimeCountUtil; //按钮倒计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        //getSupportActionBar().hide();
        ButterKnife.bind(this);
        searchCompanyService = new SearchCompanyServiceImp(this);
        initView();
        MyApplication.addDestoryActivity(EnrollActivity.this,"EnrollActivity");
        initemployee();
        initenterprise();
        erTimeCountUtil = new TimeCountUtil(er_gainyzm_bt, 60000, 1000);
        eeTimeCountUtil = new TimeCountUtil(ee_gainyzm_bt, 60000, 1000);
        er_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //boolean flag=false;
                //提交短信验证码
                SMSSDK.submitVerificationCode("86",er_user_et.getText().toString(),er_yzm_et.getText().toString());
                if (TextUtils.isEmpty(er_name_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入你的真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(er_user_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入你的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (ercode==1) {

                    Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();

                    return;
                }else if (ercode==2) {
                    Toast.makeText(getApplicationContext(),"验证失败",Toast.LENGTH_LONG).show();

                    return;
                }else if (TextUtils.isEmpty(er_password_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(er_passwordagain_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(er_password_et.getText().toString().equals(er_passwordagain_et.getText().toString()))) {
                    Toast.makeText(EnrollActivity.this, "两次密码输入的不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(er_company_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入公司", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    erloading();
                    Intent intent = new Intent(EnrollActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ee_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMSSDK.submitVerificationCode("86",ee_user_et.getText().toString(),ee_yzm_et.getText().toString());
                if (TextUtils.isEmpty(ee_name_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入你的真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(ee_user_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入你的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (eecode==1) {

                    Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();

                    return;
                }else if (eecode==2) {
                    Toast.makeText(getApplicationContext(),"验证失败",Toast.LENGTH_LONG).show();

                    return;
                }else if (TextUtils.isEmpty(ee_password_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(ee_passwordagain_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(ee_password_et.getText().toString().equals(ee_passwordagain_et.getText().toString()))) {
                    Toast.makeText(EnrollActivity.this, "两次密码输入的不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(ee_company_et.getText().toString())) {
                    Toast.makeText(EnrollActivity.this, "请输入公司", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    eeloading();
                    Intent intent = new Intent(EnrollActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //滑动布局
    public void initView() {
        titleList = new ArrayList<>();
        titleList.add("员工注册");
        titleList.add("企业注册");
        enroll_employee = getLayoutInflater().inflate(R.layout.enroll_employee, null);
        enroll_enterprise = getLayoutInflater().inflate(R.layout.enroll_enterprise, null);
        viewList = new ArrayList<>();
        viewList.add(enroll_employee);
        viewList.add(enroll_enterprise);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(viewList, titleList);
        enroll_vp.setAdapter(pagerAdapter);
        enroll_tl.setupWithViewPager(enroll_vp);

    }

    //员工的公司，职务，spinner控件
    public void initemployee() {

        ee_post_spinner = enroll_employee.findViewById(R.id.ee_post_spinner);
        ee_bt = enroll_employee.findViewById(R.id.ee_bt);
        ee_user_et = enroll_employee.findViewById(R.id.ee_user_et);
        ee_password_et = enroll_employee.findViewById(R.id.ee_password_et);
        ee_passwordagain_et = enroll_employee.findViewById(R.id.ee_passwordagain_et);
        ee_name_et = enroll_employee.findViewById(R.id.ee_name_et);
        ee_gainyzm_bt=enroll_employee.findViewById(R.id.ee_gainyzm_bt);
        ee_company_et = enroll_employee.findViewById(R.id.ee_company_et);
        ee_yzm_et=enroll_employee.findViewById(R.id.ee_yzm_et);
        showSoftInputFromWindow(this,ee_company_et);
        ee_gainyzm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // eeTimeCountUtil.start();
                SMSSDK.getVerificationCode("86", ee_user_et.getText().toString());
                ee_phone = ee_user_et.getText().toString();
            }
        });

        EventHandler eeeh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                eemHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eeeh);
        ee_company_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EnrollActivity.this,EECompanyActivity.class);
                intent.putExtra("ee_name",ee_name_et.getText().toString());
                intent.putExtra("ee_password",ee_password_et.getText().toString());
                intent.putExtra("ee_phone",ee_user_et.getText().toString());
                intent.putExtra("ee_passwordagain",ee_passwordagain_et.getText().toString());
                intent.putExtra("ee_yzm",ee_yzm_et.getText().toString());
                startActivity(intent);
            }
        });

        ee_name_et.setText(getIntent().getStringExtra("ee_name"));
        ee_yzm_et.setText(getIntent().getStringExtra("ee_yzm"));
        ee_password_et.setText(getIntent().getStringExtra("ee_password"));
        ee_passwordagain_et.setText(getIntent().getStringExtra("ee_passwordagain"));
        ee_user_et.setText(getIntent().getStringExtra("ee_phone"));
        ee_company_et.setText(getIntent().getStringExtra("cname"));

if((getIntent().getIntExtra("eecode",0))==1) {

    loadListJobSpinner();

    post_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, postList);
    post_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //加载适配器
    ee_post_spinner.setAdapter(post_adapter);

}else {
    postList.add("请选择你的职务");
   // loadListJobSpinner();
    post_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, postList);
    post_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //加载适配器
    ee_post_spinner.setAdapter(post_adapter);
}

    }
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public void initenterprise() {
        er_bt = enroll_enterprise.findViewById(R.id.er_bt);
        er_user_et = enroll_enterprise.findViewById(R.id.er_user_et);
        er_password_et = enroll_enterprise.findViewById(R.id.er_password_et);
        er_company_et = enroll_enterprise.findViewById(R.id.er_company_et);
        er_passwordagain_et = enroll_enterprise.findViewById(R.id.er_passwordagain_et);
        er_name_et = enroll_enterprise.findViewById(R.id.er_name_et);
        er_rl = enroll_enterprise.findViewById(R.id.er_rl);
        er_yzm_et=enroll_enterprise.findViewById(R.id.er_yzm_et);
        er_gainyzm_bt = enroll_enterprise.findViewById(R.id.er_gainyzm_bt);
        showSoftInputFromWindow(this,er_company_et);
        er_gainyzm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // erTimeCountUtil.start();
                SMSSDK.getVerificationCode("86", er_user_et.getText().toString());
                er_phone = er_user_et.getText().toString();
            }
        });

        EventHandler ereh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                ermHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(ereh);
        er_company_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EnrollActivity.this,CompanyActivity.class);
                intent.putExtra("e_name",er_name_et.getText().toString());
                intent.putExtra("e_password",er_password_et.getText().toString());
                intent.putExtra("e_phone",er_user_et.getText().toString());
                intent.putExtra("e_passwordagain",er_passwordagain_et.getText().toString());
                intent.putExtra("e_yzm",er_yzm_et.getText().toString());
                startActivity(intent);
            }
        });
        int page;
        page=getIntent().getIntExtra("page",0);
        if(page==1){
         enroll_vp.setCurrentItem(R.layout.enroll_enterprise);
       }
        er_name_et.setText(getIntent().getStringExtra("e_name"));
        er_yzm_et.setText(getIntent().getStringExtra("e_yzm"));
        er_password_et.setText(getIntent().getStringExtra("e_password"));
        er_passwordagain_et.setText(getIntent().getStringExtra("e_passwordagain"));
        er_user_et.setText(getIntent().getStringExtra("e_phone"));
        er_company_et.setText(getIntent().getStringExtra("name"));

    }

    public void erloading() {
        String phone = er_user_et.getText().toString();
        String name = er_name_et.getText().toString();
        String pwd = MathTools.getMd5(er_password_et.getText().toString());
        String company = er_company_et.getText().toString();
        String code = "0";//0为企业 1为员工
        final String url = Net.ENROLL + "?e_name=" + name + "&e_phone=" + phone + "&e_password=" + pwd + "&c_name=" + company + "&e_power=" + code;
        VolleyRequest.RequestGet(getContext(), url, "registered",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.v("zjc", url);
                        Toast.makeText(EnrollActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMyError(VolleyError error) {

                    }
                });
    }

    public void eeloading() {
        String name=ee_name_et.getText().toString();
        String phone = ee_user_et.getText().toString();
        String pwd = MathTools.getMd5(ee_password_et.getText().toString());
        String company = ee_company_et.getText().toString();
        String code = "1";//0为企业 1为员工
        String status="0";//0为未审核 1为已审核
        String post=ee_post_spinner.getSelectedItem().toString();
        final String url = Net.ENROLL + "?e_phone=" + phone+"&e_name="+name + "&e_password=" + pwd + "&e_job=" + post + "&c_name=" + company + "&e_power=" + code+"&e_status="+status;
        VolleyRequest.RequestGet(getContext(), url, "enroll",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);

                            String code=jsonObject.getString("registered");
                            if(code.equals("1")){
                                Toast.makeText(EnrollActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(EnrollActivity.this, "注册失败", Toast.LENGTH_LONG).show();
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
    public void loadListJobSpinner(){
        final String url=Net.GetAllJob+"?c_id="+eeCompanyActivity.c_id;
        postList=new ArrayList<>();
        postList.add("请选择你的职务");
        VolleyRequest.RequestGet(getContext(), url, "enroll",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                           // postList = new ArrayList<>();
//                            postList=new ArrayList<>();
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("JobByCid");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String jname=jsonObject1.getString("j_name");
                                postList.add(jname);
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
    public void showSearchER(List<SearchBean.search> searchList) {
        SearchCompanyAdapter searchAdapter = new SearchCompanyAdapter(searchList, this);
       // er_searchcompany_rv.setAdapter(searchAdapter);
    }

    public void showSearchEE(List<SearchBean.search> searchList) {
        SearchCompanyEEAdapter searchAdapter = new SearchCompanyEEAdapter(searchList, this);
        //ee_searchcompany_rv.setAdapter(searchAdapter);
    }

    @SuppressLint("HandlerLeak")

    Handler eemHandler = new Handler() {
        public void handleMessage(Message msg) { // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    eeTimeCountUtil.start();
                    Toast.makeText(getContext(),"发送验证码成功",Toast.LENGTH_LONG).show();
                    eecode=0;
                } else {
                    // TODO 处理错误的结果
                    ((Throwable) data).printStackTrace();
                    eecode=1;
                    Toast.makeText(getContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                }
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证码验证通过的结果
                    Toast.makeText(getContext(),"验证成功",Toast.LENGTH_LONG).show();
                      eecode=0;
                } else {
                    // TODO 处理错误的结果
                    eecode=2;
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(getContext(),"验证失败",Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")

    Handler ermHandler = new Handler() {
        public void handleMessage(Message msg) { // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                     erTimeCountUtil.start();
                    Toast.makeText(getContext(),"发送验证码成功",Toast.LENGTH_LONG).show();
                    ercode=0;
                } else {
                    // TODO 处理错误的结果
                    ((Throwable) data).printStackTrace();
                    ercode=1;
                    Toast.makeText(getContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                }
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证码验证通过的结果
                    Toast.makeText(getContext(),"验证成功",Toast.LENGTH_LONG).show();
                    ercode=0;
                } else {
                    // TODO 处理错误的结果
                    ercode=2;
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
