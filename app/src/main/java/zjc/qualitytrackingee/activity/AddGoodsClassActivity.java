package zjc.qualitytrackingee.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import zjc.qualitytrackingee.adapter.AllGoodsClassRecyclerViewAdapter;
import zjc.qualitytrackingee.adapter.CheckRecyclerViewAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class AddGoodsClassActivity extends AppCompatActivity implements  AllGoodsClassRecyclerViewAdapter.onSlidingViewClickListener{
    @BindView(R.id.add_goodsclass_back_ll)
    LinearLayout add_goodsclass_back_ll;
    private List<String> dataName;    //姓名
    @BindView(R.id.add_goodsclass_et)
    EditText add_goodsclass_et;
    @BindView(R.id.show_allgoodsclass_bt)
    Button show_allgoodsclass_bt;
    @BindView(R.id.add_goodsclass_rv)
    RecyclerView add_goodsclass_rv;
    @BindView(R.id.add_goodsclass_bt)Button add_goodsclass_bt;
    private AllGoodsClassRecyclerViewAdapter rvAdapter;
    private SharedPreferences pref;
    public static List<String> dataid=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_class);
       // getSupportActionBar().hide();
        ButterKnife.bind(this);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        add_goodsclass_rv.setLayoutManager(new LinearLayoutManager(this));

        show_allgoodsclass_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datas();
            }
        });
        add_goodsclass_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(add_goodsclass_et.getText().toString())){
                    Toast.makeText(AddGoodsClassActivity.this,"您没有输入要增加的商品类！！！",Toast.LENGTH_SHORT).show();

                }else {
                    uploadGoodsClass();

                }
            }
        });
    }
    @OnClick(R.id.add_goodsclass_back_ll)
    public void add_goodsclass_back_ll_Onclick(){
        finish();
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        rvAdapter = new AllGoodsClassRecyclerViewAdapter(this,
                 dataName,dataid);
        //将适配的内容放入 mRecyclerView
        add_goodsclass_rv.setAdapter(rvAdapter);
        //控制Item增删的动画，需要通过ItemAnimator  DefaultItemAnimator -- 实现自定义动画
        add_goodsclass_rv.setItemAnimator(new DefaultItemAnimator());
        //设置滑动监听器 （侧滑）
        rvAdapter.setOnSlidListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        rvAdapter.removeData(position);
    }
    public void datas(){

        dataName = new ArrayList<String>();//商品类名（谁的消息）
        dataid=new ArrayList<>();
        final String url= Net.GetAllGoodsClass+"?c_id="+ MyApplication.getC_id();
        VolleyRequest.RequestGet(getContext(),url, "getallGoodsclass",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getAllCommodityClassByCid");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                dataName.add(jsonObject1.getString("coc_name"));
                                dataid.add(jsonObject1.getString("coc_id"));

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
    public void uploadGoodsClass(){
        String goodsClassName=add_goodsclass_et.getText().toString();
        String id=pref.getString("c_id","");
        String url= Net.UploadGoodsClass+"?c_id="+id+"&coc_name="+goodsClassName;
        VolleyRequest.RequestGet(getContext(),url, "uploadGoodsClass",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("addCommodityClass");
                            if(code.equals("1")){
                                Toast.makeText(AddGoodsClassActivity.this,"该商品类别已存在",Toast.LENGTH_LONG).show();

                            }else{
                                add_goodsclass_et.setText("");
                                Toast.makeText(AddGoodsClassActivity.this,"该商品类别添加成功",Toast.LENGTH_LONG).show();
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
}
