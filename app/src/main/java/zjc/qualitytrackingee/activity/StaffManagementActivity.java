package zjc.qualitytrackingee.activity;

import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.CheckRecyclerViewAdapter;
import zjc.qualitytrackingee.adapter.StaffManageAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class StaffManagementActivity extends AppCompatActivity {
    @BindView(R.id.staff_manage_srl)
    SwipeRefreshLayout staff_manage_srl;
    @BindView(R.id.staff_back_ll)LinearLayout staff_back_ll;
    @BindView(R.id.staff_manage_rv)
    RecyclerView staff_manage_rv;
    private List<String> ImageList;
    private List<String> NameList;
    private List<String> JobList;
    public  static List<String> PhoneList;
//    private List<String> NameList;
//    private List<String> JobList;
    private StaffManageAdapter staffManageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);
     //   getSupportActionBar().hide();
        ButterKnife.bind(this);
        staff_manage_rv.setLayoutManager(new LinearLayoutManager(this));
        initView();
        datas();
        updateInterface();
    }
    @OnClick(R.id.staff_back_ll)
    public void staff_back_ll_Onclick(){
        finish();
    }
    public void initView(){
        staff_manage_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas();
                staff_manage_srl.setRefreshing(false);

            }
        });
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        staffManageAdapter = new StaffManageAdapter(this,
                ImageList, NameList, JobList,this);
        //将适配的内容放入 mRecyclerView
        staff_manage_rv.setAdapter(staffManageAdapter);


    }
    public void datas(){
        ImageList = new ArrayList<String>();//头像（谁的头像）
        NameList = new ArrayList<String>();     //姓名（谁的消息）
        JobList = new ArrayList<String>();  //职务（消息内容）
        PhoneList=new ArrayList<>();
        final String url= Net.GetEmployee+"?e_phone="+ MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "getEmployee",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("GetAllEmpBySC");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String image=jsonObject1.getString("e_img");
                                ImageList.add(image.replaceAll("\\\\","") );
                                NameList.add(jsonObject1.getString("e_name"));
                                JobList.add(jsonObject1.getString("e_job"));
                                PhoneList.add(jsonObject1.getString("e_phone"));
                            }
                            updateInterface();
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
