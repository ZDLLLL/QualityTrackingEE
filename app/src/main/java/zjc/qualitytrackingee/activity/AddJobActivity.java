package zjc.qualitytrackingee.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
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
import zjc.qualitytrackingee.adapter.AllJobRecyclerViewAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class AddJobActivity extends AppCompatActivity implements AllJobRecyclerViewAdapter.onSlidingViewClickListener{
    private List<String> dataJob;     //姓名
    public static List<String> dataJobid=new ArrayList<String>();     //编号
    @BindView(R.id.add_job_back_ll)
    LinearLayout add_job_back_ll;
    @BindView(R.id.alljob_rv)
    RecyclerView alljob_rv;
    @BindView(R.id.show_alljob_bt)
    Button show_alljob_bt;
    @BindView(R.id.add_job_bt) Button add_job_bt;
    @BindView(R.id.add_job_et)
    EditText add_job_et;
    private AllJobRecyclerViewAdapter rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
      //  getSupportActionBar().hide();
        ButterKnife.bind(this);
       alljob_rv.setLayoutManager(new LinearLayoutManager(this));
        add_job_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(add_job_et.getText().toString())){
                    Toast.makeText(AddJobActivity.this,"您没有输入要增加的职务！！！",Toast.LENGTH_SHORT).show();
                }else {
                    uploadJob();
                }
            }
        });
        show_alljob_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllJob();
            }
        });
    }

    private void getAllJob() {
        dataJob = new ArrayList<String>();
          //职务名称（谁的消息）
        final String url= Net.GetAllJob+"?c_id="+ MyApplication.getC_id();
        VolleyRequest.RequestGet(getContext(),url, "getAllJob",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("JobByCid");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                dataJob.add(jsonObject1.getString("j_name"));
                                dataJobid.add(jsonObject1.getString("j_id"));
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

    private void uploadJob() {
        String jobName=add_job_et.getText().toString();
        String url= Net.UploadJob+"?c_id="+ MyApplication.getC_id()+"&j_name="+jobName;
        VolleyRequest.RequestGet(getContext(),url, "uploadJob",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("addJobBycid");
                            if(code.equals("no")){
                                Toast.makeText(AddJobActivity.this,"该职务已存在",Toast.LENGTH_LONG).show();
                            }else{
                                add_job_et.setText("");
                                Toast.makeText(AddJobActivity.this,"职务添加成功",Toast.LENGTH_LONG).show();
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


    @OnClick(R.id.add_job_back_ll)
    public void add_job_back_ll_Onclick(){
        finish();
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        rvAdapter = new AllJobRecyclerViewAdapter(this,
                dataJob);
        LinearLayoutManager lr=new LinearLayoutManager(this);
        lr.setOrientation(OrientationHelper.VERTICAL);
        alljob_rv.setLayoutManager(lr);

        //将适配的内容放入 mRecyclerView
        alljob_rv.setAdapter(rvAdapter);
        //控制Item增删的动画，需要通过ItemAnimator  DefaultItemAnimator -- 实现自定义动画
        alljob_rv.setItemAnimator(new DefaultItemAnimator());
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
}
