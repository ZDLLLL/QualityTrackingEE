package zjc.qualitytrackingee.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import zjc.qualitytrackingee.EEactivity.EEAnalysisActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.EEAnalysisAdapter;
import zjc.qualitytrackingee.adapter.GoodsDutyAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class GoodsDutyActivity extends AppCompatActivity {
    @BindView(R.id.goodsduty_back_ll)LinearLayout goodsduty_back_ll;
    @BindView(R.id.goodsduty_rv)
    RecyclerView goodsduty_rv;
    @BindView(R.id.goodsduty_srl)
    SwipeRefreshLayout goodsduty_srl;
    private List<String> processpeopletimeList;
    private List<String> processpeoplenameList;
    private List<String> processpeopleimageList;
    private List<String> inspectortimeList;
    private List<String> inspectornameList;
    private List<String> inspectorimageList;
    private  List<String> goodsidList;
    private  List<String> goodsnameList;
    private List<String> gxnameList;
    private GoodsDutyAdapter goodsDutyAdapter;
    private static String co_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_duty);
        ButterKnife.bind(this);
        co_id=getIntent().getStringExtra("co_id");
        goodsduty_rv.setLayoutManager(new LinearLayoutManager(this));
        initView();
        Scavenging();
        updateInterface();
    }
    @OnClick(R.id.goodsduty_back_ll)
    public void goodsduty_back_ll_Onclick(){
        finish();
    }
    public void initView(){
        goodsduty_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Scavenging();
                goodsduty_srl.setRefreshing(false);

            }
        });
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        goodsDutyAdapter = new GoodsDutyAdapter(this, processpeopletimeList,  processpeoplenameList
                , processpeopleimageList,  inspectortimeList, inspectornameList
                , inspectorimageList,  goodsidList,  goodsnameList, gxnameList, this);
        //将适配的内容放入 mRecyclerView
        goodsduty_rv.setAdapter(goodsDutyAdapter);
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
        final String url= Net.GetGoodsDuty+"?co_id="+co_id;
        VolleyRequest.RequestGet(getContext(),url, "selectHeadPerson",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("selectHeadPerson");
                                for (int i = 0; i < jsonArray.length(); i++) {
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
