package zjc.qualitytrackingee.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import zjc.qualitytrackingee.adapter.ReleasePushAdapter;
import zjc.qualitytrackingee.beans.ReleasePushBean;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.Json;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class ReleasePushActivity extends AppCompatActivity {
    public String Phone="";
    @BindView(R.id.releasepush_back_ll)
    LinearLayout releasepush_back_ll;
    @BindView(R.id.releasepush_result_et)
    EditText releasepush_result_et;
    @BindView(R.id.releasepush_publish_tv)
    TextView releasepush_publish_tv;
    @BindView(R.id.releasepush_all_cb)
    CheckBox releasepush_all_cb;
    @BindView(R.id.releasepush_rv)
    RecyclerView releasepush_rv;
    public static  Boolean isSelectAll=false;
    private  ReleasePushAdapter releasePushAdapter;
    private List<String> NameList;
    private List<String> PhoneList;
    private List<ReleasePushBean.ReleasePushsBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_push);
        ButterKnife.bind(this);
        releasepush_rv.setLayoutManager(new LinearLayoutManager(this));
        isSelectAll=false;
        datas();
        updateInterface();
        releasepush_all_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsSelect) {
                if(IsSelect){

                    isSelectAll=true;
                    for(int i=0;i<list.size();i++) {
                        list.get(i).setSelected(true);
                    }
                    releasePushAdapter.notifyDataSetChanged();
                }else {
                    isSelectAll=false;
                    for(int i=0;i<list.size();i++){
                        list.get(i).setSelected(false);
                    }
                    releasePushAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    @OnClick(R.id.releasepush_back_ll)
    public void releasepush_back_ll_Onclick(){
        finish();
    }
    @OnClick(R.id.releasepush_publish_tv)
    public void releasepush_publish_tv_Onclik(){
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSelected()){
                Phone+=list.get(i).getReleasepushPhone();
            }
        }
        isSelectAll=false;
        //finish();
        ReleasePush();

        finish();
    }
    public void ReleasePush(){
        final String url=Net.PushJiGuang+"?message="+releasepush_result_et.getText().toString()+"&e_phone="+Phone;
        VolleyRequest.RequestGet(getContext(),url, "pushMessage",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Log.v("url",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("pushMessage");
                            if("yes".equals(code)){
                                Toast.makeText(ReleasePushActivity.this,"推送发布成功",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(ReleasePushActivity.this,"推送发布失败",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {

                    }
                });
    }

    public void updateInterface(){
        //实例化 RecyclerViewAdapter 并设置数据
//        releasePushAdapter = new ReleasePushAdapter(this,
//                PhoneList, NameList);
        releasePushAdapter = new ReleasePushAdapter(this,
                list);
        //将适配的内容放入 mRecyclerView
        releasepush_rv.setAdapter(releasePushAdapter);

    }
    public void datas(){
        NameList = new ArrayList<String>();     //姓名（谁的消息）
        PhoneList=new ArrayList<>();
        list=new ArrayList<>();
        final String url= Net.GetEmployee+"?e_phone="+ MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "getEmployee",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        Log.d("zjc",url);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("GetAllEmpBySC");
                            ReleasePushBean.ReleasePushsBean releasePushsBean;
                            for(int i=0;i<jsonArray.length();i++){
                                releasePushsBean=new ReleasePushBean.ReleasePushsBean();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                NameList.add(jsonObject1.getString("e_name"));
                                PhoneList.add(jsonObject1.getString("e_phone"));
                                releasePushsBean.setReleasepushName(jsonObject1.getString("e_name"));
                                releasePushsBean.setReleasepushPhone(jsonObject1.getString("e_phone"));
                                list.add(releasePushsBean);
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
