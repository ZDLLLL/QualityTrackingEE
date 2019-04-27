package zjc.qualitytrackingee.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.AllGoodsRecyclerViewAdapter;
import zjc.qualitytrackingee.adapter.ShowGoodsBatchRecyclerViewAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.RecyclerItemView;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class ShowGoodsBatchActivity extends AppCompatActivity implements ShowGoodsBatchRecyclerViewAdapter.onSlidingViewClickListener{
    @BindView(R.id.showgoodsbatch_back_ll)
    LinearLayout showgoodsbatch_back_ll;
    @BindView(R.id.showgoodsbatch_list_sp)
    Spinner showgoodsbatch_list_sp;
    @BindView(R.id.showgoodsbatch_rv)
    RecyclerView showgoodsbatch_rv;
    @BindView(R.id.showgoodsbatch_srl)
    SwipeRefreshLayout showgoodsbatch_srl;
    private static String gname;
    private ArrayAdapter<String> batch_adapter;
    private ShowGoodsBatchRecyclerViewAdapter rvAdapter;
    private List<String> dataImage;     //商品图片
    private List<String> dataName;     //商品名
    private List<String> dataPrice;     //商品名
    private List<String> dataDescribe;//商品描述
    private List<String> dataId;//商品编号
    private List<String> datastatus;//商品状态

    private static String coc_id;
    private static String co_id;
    private static List<String> goodsbatchnameList;//商品类列表
    private static List<String> goodsbatchidList;
    public static String batch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goods_batch);
        ButterKnife.bind(this);
        gname=getIntent().getStringExtra("gname");
        coc_id=getIntent().getStringExtra("coc_id");
        co_id=getIntent().getStringExtra("co_id");
        loadSpinner();
        showgoodsbatch_rv.setLayoutManager(new LinearLayoutManager(this));

        initView();
        initSRL();
    }
    public void initSRL(){
        showgoodsbatch_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
                showgoodsbatch_srl.setRefreshing(false);
            }
        });
    }
    @OnClick(R.id.showgoodsbatch_back_ll)
    public void showgoodsbatch_back_ll_Onclick(){
        finish();
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        rvAdapter = new ShowGoodsBatchRecyclerViewAdapter(this,
                dataImage,dataName,dataPrice,dataDescribe,dataId,datastatus,this);
        //将适配的内容放入 mRecyclerView
        showgoodsbatch_rv.setAdapter(rvAdapter);
        //控制Item增删的动画，需要通过ItemAnimator  DefaultItemAnimator -- 实现自定义动画
        showgoodsbatch_rv.setItemAnimator(new DefaultItemAnimator());
        //设置滑动监听器 （侧滑）
        rvAdapter.setOnSlidListener(this);
    }
    public  void initView(){


        batch_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goodsbatchidList);
        batch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        showgoodsbatch_list_sp.setAdapter(batch_adapter);
        showgoodsbatch_list_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //teacher_college=position+1;
                batch=showgoodsbatch_list_sp.getSelectedItem().toString();
                loadList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void loadList() {
        dataImage = new ArrayList<>();
        dataName = new ArrayList<>();
        dataDescribe = new ArrayList<>();
        dataPrice = new ArrayList<>();
        dataId=new ArrayList<>();
        datastatus=new ArrayList<>();
        final String url = Net.GetCommodityByAcid + "?ac_id="+showgoodsbatch_list_sp.getSelectedItem().toString()+"&co_id="+co_id;
        Log.v("url",url);
        VolleyRequest.RequestGet(getContext(), url, "GetCommodityByAcid",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            Log.v("url",url);
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("getCommodityByAcid");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                dataName.add(jsonObject1.getString("co_name"));
                                dataImage.add(jsonObject1.getString("co_img"));
                                dataId.add(jsonObject1.getString("co_id"));
                                dataDescribe.add(jsonObject1.getString("co_describe"));
                                dataPrice.add(jsonObject1.getString("co_price"));
                                datastatus.add(jsonObject1.getString("co_status"));
                            }
                            updateInterface();
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
        public void loadSpinner(){
        goodsbatchnameList = new ArrayList<String>();     //批次//
        goodsbatchidList = new ArrayList<String>();     //批次id（谁的消息）
        goodsbatchnameList.add("所有批次");
        goodsbatchidList.add("所有批次");
        String url= Net.GetAllGoodsBacth+"?c_id="+ MyApplication.getC_id()+"&co_name="+gname;
        VolleyRequest.RequestGet(getContext(),url, "getAllAccessIdBycname",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getAllAccessIdBycname");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                goodsbatchnameList.add(jsonObject1.getString("ac_time"));
                                goodsbatchidList.add(jsonObject1.getString("ac_id"));

                            }
                            // updateInterface();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(ShowGoodsBatchActivity.this,"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        rvAdapter.removeData(position);
    }
}
