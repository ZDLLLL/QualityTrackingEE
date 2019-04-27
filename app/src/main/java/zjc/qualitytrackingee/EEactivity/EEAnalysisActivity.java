package zjc.qualitytrackingee.EEactivity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import zjc.qualitytrackingee.adapter.EEAnalysisAdapter;
import zjc.qualitytrackingee.adapter.StaffManageAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class EEAnalysisActivity extends AppCompatActivity {
    private String content;
    @BindView(R.id.eeanalysis_back_ll)
    LinearLayout eeanalysis_back_ll;
    @BindView(R.id.eeanalysis_srl)
    SwipeRefreshLayout eeanalysis_srl;
    @BindView(R.id.eeanalysis_rv)
    RecyclerView eeanalysis_rv;
    private List<String> processpeopletimeList;
    private List<String> processpeoplenameList;
    private List<String> processpeopleimageList;
    private List<String> inspectortimeList;
    private List<String> inspectornameList;
    private List<String> inspectorimageList;
    private  List<String> goodsidList;
    private  List<String> goodsnameList;
    private List<String> gxnameList;
    private EEAnalysisAdapter eeAnalysisAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eeanalysis);
        ButterKnife.bind(this);
        content=getIntent().getStringExtra("content");
        eeanalysis_rv.setLayoutManager(new LinearLayoutManager(this));
        initView();
        Scavenging();
        updateInterface();
    }
    @OnClick(R.id.eeanalysis_back_ll)
    public void eeanalysis_back_ll_Onclick(){
        finish();
    }
    public void initView(){
        eeanalysis_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Scavenging();

                eeanalysis_srl.setRefreshing(false);

            }
        });
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        eeAnalysisAdapter = new EEAnalysisAdapter(this, processpeopletimeList,  processpeoplenameList
                , processpeopleimageList,  inspectortimeList, inspectornameList
                , inspectorimageList,  goodsidList,  goodsnameList, gxnameList, this);
        //将适配的内容放入 mRecyclerView
        eeanalysis_rv.setAdapter(eeAnalysisAdapter);


    }
    public  void Scavenging(){
        processpeopletimeList=new ArrayList<>();
        processpeoplenameList=new ArrayList<>();
        processpeopleimageList=new ArrayList<>();
        inspectortimeList=new ArrayList<>();
        inspectornameList=new ArrayList<>();
        inspectorimageList=new ArrayList<>();
        goodsidList=new ArrayList<>();
        goodsnameList=new ArrayList<>();
        gxnameList=new ArrayList<>();
        final String url= Net.SaoYiSao+"?co_id="+content+"&e_phone="+ MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "saoyisao",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("EdecodeQr");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String e_img = jsonObject1.getString("e_img");
                                String inquisitor_img = jsonObject1.getString("inquisitor_img");
                                String m_time = jsonObject1.getString("m_time");
                                String p_name = jsonObject1.getString("p_name");
                                String inquisitor_time = jsonObject1.getString("inquisitor_time");
                                String inquisitor_name = jsonObject1.getString("inquisitor_name");
                                String co_id = jsonObject1.getString("co_id");
                                String e_name = jsonObject1.getString("e_name");
                                String co_name = jsonObject1.getString("co_name");
                                processpeopletimeList.add(m_time);
                                processpeoplenameList.add(e_name);
                                processpeopleimageList.add(e_img.replaceAll("\\\\", ""));
                                inspectortimeList.add(inquisitor_time);
                                inspectornameList.add(inquisitor_name);
                                inspectorimageList.add(inquisitor_img.replaceAll("\\\\", ""));
                                goodsidList.add(co_id);
                                goodsnameList.add(co_name);
                                gxnameList.add(p_name);
                            }
                                updateInterface();
                            //}

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
