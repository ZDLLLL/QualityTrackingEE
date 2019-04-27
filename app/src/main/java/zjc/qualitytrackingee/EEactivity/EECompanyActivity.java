package zjc.qualitytrackingee.EEactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.EESortAdapter;
import zjc.qualitytrackingee.adapter.SortAdapter;
import zjc.qualitytrackingee.beans.CompanyBean;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.SideBar;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class EECompanyActivity extends AppCompatActivity {
    private ListView eeacvitivity_company_lv;
    private SideBar eeactivity_company_sb;
    public static String c_id;
    private ArrayList<CompanyBean> list=new ArrayList<>();
    public static ArrayList<CompanyBean> list2=new ArrayList<>();
    public String e_name;
    public String e_phone;
    public String e_password;
    public String e_passwordagain;
    public String e_yzm;
    private LinearLayout eecompanylist_back_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eecompany);
      //  getSupportActionBar().hide();
        e_name=getIntent().getStringExtra("ee_name");
        e_password=getIntent().getStringExtra("ee_password");
        e_passwordagain=getIntent().getStringExtra("ee_passwordagain");
        e_phone=getIntent().getStringExtra("ee_phone");
        e_yzm=getIntent().getStringExtra("ee_yzm");

        initView();
        loadingList();
    }
    private void initView() {
        eeacvitivity_company_lv = (ListView) findViewById(R.id.acvitivity_eecompany_lv);
        eecompanylist_back_ll=findViewById(R.id.eecompanylist_back_ll);
        eeactivity_company_sb = (SideBar) findViewById(R.id.activity_eecompany_sb);
        eeactivity_company_sb.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        eeacvitivity_company_lv.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        eecompanylist_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData() {
        // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        EESortAdapter adapter = new EESortAdapter(this, list,this);
        eeacvitivity_company_lv.setAdapter(adapter);

    }
    public void loadingList(){
        final String url= Net.AllCompany;
        list2=new ArrayList<>();
        list=new ArrayList<>();
        VolleyRequest.RequestGet(getContext(),url, "company",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        Log.v("event",result);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("AllCompany");//web端
                            CompanyBean companyBean;
                            // List<CompanyBean> img_list=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                companyBean = new CompanyBean();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String cname = jsonObject1.getString("c_name");
                                String cid = jsonObject1.getString("cid");
                                //    String c_img = jsonObject1.getString("c_img");
                                   companyBean.setC_name(cname);
                                    companyBean.setCid(cid);
                                // img_list.add(companyBean);
                               // list2.add(companyBean);
                                list2.add(new CompanyBean(cname,cid));
                                list.add(new CompanyBean(cname));

                            }
                            //Collections.sort(list2.get(1).getC_name());
                            Collections.sort(list2);
                            Collections.sort(list);
                            initData();
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
}
