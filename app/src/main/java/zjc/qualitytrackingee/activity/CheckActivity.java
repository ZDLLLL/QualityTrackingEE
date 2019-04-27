package zjc.qualitytrackingee.activity;

import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import zjc.qualitytrackingee.beans.EmployeeBean;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.GsonImpl;
import zjc.qualitytrackingee.utils.RecyclerUtils;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class CheckActivity extends AppCompatActivity  implements CheckRecyclerViewAdapter.onSlidingViewClickListener{
    @BindView(R.id.check_rv)
    RecyclerView check_rv;
    @BindView(R.id.check_srl)
    SwipeRefreshLayout check_srl;
    @BindView(R.id.check_back_ll)
    LinearLayout check_back_ll;
    //private RecyclerView recycler;              //在xml 中 RecyclerView 布局
    private CheckRecyclerViewAdapter rvAdapter;      //item_recycler 布局的 适配器
    private List<String> dataImage;  //头像
    private List<String> dataName;    //姓名
    private List<String> dataPost;  //职务
    private List<String> dataPhone;     //电话
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
     //   getSupportActionBar().hide();
        ButterKnife.bind(this);

        check_rv.setLayoutManager(new LinearLayoutManager(this));
        initView();
        datas();
        //updateInterface();
    }
    public void initView(){
        check_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas();
                check_srl.setRefreshing(false);

            }
        });
    }

    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        rvAdapter = new CheckRecyclerViewAdapter(this,
                dataImage, dataName, dataPost, dataPhone);
        //将适配的内容放入 mRecyclerView
        check_rv.setAdapter(rvAdapter);
        //控制Item增删的动画，需要通过ItemAnimator  DefaultItemAnimator -- 实现自定义动画
        check_rv.setItemAnimator(new DefaultItemAnimator());
        //设置滑动监听器 （侧滑）
        rvAdapter.setOnSlidListener(this);
    }

    //通过 position 区分点击了哪个 item
    @Override
    public void onItemClick(View view, int position) {
        //在这里可以做出一些反应（跳转界面、弹出弹框之类）
        // Toast.makeText(MainActivity.this,"点击了：" + position,Toast.LENGTH_SHORT).show();
    }

    //点击删除按钮时，根据传入的 position 调用 RecyclerAdapter 中的 removeData() 方法
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        rvAdapter.removeData(position);
    }
    @OnClick(R.id.check_back_ll)
    public void check_back_ll_Onclick(){
        finish();
    }
    public void datas(){
       // dataImage = new ArrayList<String>();
        dataImage = new ArrayList<String>();//头像（谁的头像）
        dataName = new ArrayList<String>();     //标题（谁的消息）
        dataPost = new ArrayList<String>();  //内容（消息内容）
        dataPhone = new ArrayList<String>();     //时间（消息时间）
      final String url= Net.CheckEmployee+"?e_phone="+MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "company",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("Admin");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String image=jsonObject1.getString("e_img");
                                dataImage.add(image.replaceAll("\\\\","") );
                                dataName.add(jsonObject1.getString("e_name"));
                                dataPhone.add(jsonObject1.getString("e_phone"));
                                dataPost.add(jsonObject1.getString("e_job"));
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
