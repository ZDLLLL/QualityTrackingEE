package zjc.qualitytrackingee.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class StaffManagementInfoActivity extends AppCompatActivity {
    @BindView(R.id.staffinfo_back_ll)
    LinearLayout staffinfo_back_ll;
    @BindView(R.id.staff_info_company_tv)
    TextView staff_info_company_tv;
    @BindView(R.id.staff_info_iv)
    ImageView staff_info_iv;
    @BindView(R.id.staff_info_name_tv) TextView staff_info_name_tv;
    @BindView(R.id.staff_info_phone_tv) TextView staff_info_phone_tv;
    @BindView(R.id.staff_info_job_tv) TextView staff_info_job_tv;
    @BindView(R.id.delete_staff_bt)
    Button delete_staff_bt;
    private static String phone;
    private  String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management_info);
        ButterKnife.bind(this);
        phone=getIntent().getStringExtra("c_phone");
        image=getIntent().getStringExtra("cimage");
//        if(MyApplication.getE_power().equals("0")){
//            delete_staff_tv.setVisibility(View.GONE);
//        }
        getUser();
    }
    @OnClick(R.id.delete_staff_bt)
    public void delete_staff_tv(){
        StaffManagementInfoActivity.ConfirmDialog.newInstance("delete")
                .setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        //finish();
    }
    @OnClick(R.id.staffinfo_back_ll)
    public void staffinfo_back_ll_Onclick(){
        finish();
    }
    public void getUser() {
        String url = Net.GetUser + "?e_phone=" + phone;
        VolleyRequest.RequestGet(getContext(), url, "getuser",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("personInfo");
                            String e_name = jsonObject1.getString("e_name");
                            String e_img = jsonObject1.getString("e_img");
                            String e_job = jsonObject1.getString("j_name");
                            String e_compamy = jsonObject1.getString("c_name");
                            String power=jsonObject1.getString("e_power");
                            //image = e_img.replaceAll("\\\\", "");
                            if(power.equals("0")){
                                delete_staff_bt.setVisibility(View.GONE);
                            }
                            staff_info_phone_tv.setText(phone);
                            staff_info_job_tv.setText(e_job);
                            staff_info_company_tv.setText(e_compamy);
                            Glide.with(getApplicationContext())
                                    .load(e_img)
                                    .asBitmap()
                                    .error(R.drawable.head1)
                                    .into(staff_info_iv);
                            staff_info_name_tv.setText(e_name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(), "╮(╯▽╰)╭连接不上了", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static class ConfirmDialog extends BaseNiceDialog {
        private String type;

        public static StaffManagementInfoActivity.ConfirmDialog newInstance(String type) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            StaffManagementInfoActivity.ConfirmDialog dialog = new StaffManagementInfoActivity.ConfirmDialog();
            dialog.setArguments(bundle);
            return dialog;
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle == null) {
                return;
            }
            type = bundle.getString("type");
        }
        @Override
        public int intLayoutId() {
            return  R.layout.confirm_layout;
        }

        @Override
        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
            if ("delete".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "确定删除这位员工?");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        deleteEmployee();
                        getActivity().finish();
                    }
                });
            }
        }
    }
    public static void deleteEmployee() {
        String url = Net.DeleteEmployee + "?e_phone=" + phone;
        VolleyRequest.RequestGet(getContext(), url, "getuser",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("deleteEmp");
                            if("yes".equals(code)){
                                Toast.makeText(getContext(), "成功删除员工", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "删除员工失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(), "╮(╯▽╰)╭连接不上了", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
