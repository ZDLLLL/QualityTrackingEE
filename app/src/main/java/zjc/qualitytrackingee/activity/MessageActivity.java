package zjc.qualitytrackingee.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.MessageAdapter;
import zjc.qualitytrackingee.adapter.UserFeedBackAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView message_rv;
    private List<String> list;
    private MessageAdapter messageAdapter;
    private SwipeRefreshLayout message_srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message_rv=(RecyclerView) findViewById(R.id.message_rv);
        message_rv.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.message_back_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            datas();
        message_srl=findViewById(R.id.message_srl);
        message_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                message_srl.setRefreshing(false);
                datas();
            }
        });
    }
    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
        messageAdapter= new MessageAdapter(this,
                list);
        //将适配的内容放入 mRecyclerView
        message_rv.setAdapter(messageAdapter);
    }
    public void datas(){
        list=new ArrayList<>();
        final String url= Net.GetJpush+"?e_phone="+ MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "getJpush",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("getJpush");
                            if("no".equals(code)){
                                Toast.makeText(getContext(),"暂无推送",Toast.LENGTH_SHORT).show();
                            }else {
                                JSONArray jsonArray = jsonObject.getJSONArray("getJpush");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    list.add(jsonObject1.getString("j_message"));
                                }
                                updateInterface();
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
