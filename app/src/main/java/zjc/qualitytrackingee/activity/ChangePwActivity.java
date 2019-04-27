package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.MathTools;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class ChangePwActivity extends AppCompatActivity {
    @BindView(R.id.erchangepw_old_et)
    EditText erchangePw_old_et;
    @BindView(R.id.erchangepw_new_et) EditText erchangePw_new_et;
    @BindView(R.id.erchangepw_new2_et) EditText erchangePw_new2_et;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        ButterKnife.bind(this);
       // getSupportActionBar().hide();
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor=pref.edit();

    }
    @OnClick(R.id.erchangepw_back_ll)
    public void changepw_back_ll_Click(){
        finish();
    }

    @OnClick(R.id.erchangepw_confirm_ll)
    public void changepw_confirm_ll_Click(){
        boolean flag=false;
        if(TextUtils.isEmpty(erchangePw_old_et.getText().toString())){
            flag=true;
        }
        if(TextUtils.isEmpty(erchangePw_new_et.getText().toString())){
            flag=true;
        }
        if(TextUtils.isEmpty(erchangePw_new2_et.getText().toString())){
            flag=true;
        }
        if(flag){
            Toast.makeText(getApplication(),"您还没有填写完整的信息",Toast.LENGTH_SHORT).show();
        }else if(!erchangePw_new2_et.getText().toString().equals(erchangePw_new_et.getText().toString())){
            Toast.makeText(getApplication(),"您输入的两次新密码不相同",Toast.LENGTH_SHORT).show();
        }else {

            String old_pw= MathTools.getMd5(erchangePw_old_et.getText().toString());
            String new_pw= MathTools.getMd5(erchangePw_new_et.getText().toString());

            String URL= Net.CHANGE_PASSWD+"?e_phone="+ MyApplication.getE_phone()+"&e_password="+MyApplication.getE_token()
                    +"&Newe_password="+new_pw;
            VolleyRequest.RequestGet(getContext(),URL,"change_pw",
                    new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                        @Override
                        public void onMySuccess(String result) {
                            try {

                                JSONObject jsonObject = new JSONObject(result);
                                String code=jsonObject.getString("changePassword");
                                if(code.equals("yes")){
                                    Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();


                                    //清楚所有信息，回到登陆界面
                                    MyApplication.setE_phone("");
                                    MyApplication.setE_token("");

                                    editor.putString("stu_id","");
                                    editor.putString("token","");
                                    editor.apply();




                                    Intent intent=new Intent(ChangePwActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(getContext(),"修改失败",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onMyError(VolleyError error) {
                            Toast.makeText(getContext(),"评价失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}
